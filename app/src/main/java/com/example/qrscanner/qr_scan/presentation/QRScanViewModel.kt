package com.example.qrscanner.qr_scan.presentation

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrscanner.R
import com.example.qrscanner.core.presentation.util.UiText
import com.example.qrscanner.qr_scan.data.QrCodeAnalyzer
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class QRScanViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(QRScanState())
    val state = _state.onStart {
        if (!hasLoadedInitialData) {
            /** Load initial data here **/
            hasLoadedInitialData = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = QRScanState()
    )

    private val _event = Channel<QRScanEvent>()
    val event = _event.receiveAsFlow()

    private val cameraPreviewUseCase = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            _state.update {
                it.copy(
                    surfaceRequest = newSurfaceRequest
                )
            }
        }
    }

    private val imageCapture = ImageCapture.Builder().build()

    private val imageAnalysis = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()
        .apply {
            setAnalyzer(
                Executors.newSingleThreadExecutor(), QrCodeAnalyzer(
                    onQrCodeScanned = { qrCodeValue ->
                        // Handle the scanned QR code value
                        // For example, update state or send an event
                        Log.d("QRScanViewModel", "Scanned QR Code: $qrCodeValue")
                        viewModelScope.launch {
//                        _event.send(QRScanEvent.OnQRCodeScanned(qrCodeValue))
                            // You might want to update the state as well
                            // _state.update { it.copy(scannedQrValue = qrCodeValue) }
                        }
                    },
                    onError = { exception ->
                        Log.e("QRScanViewModel", "Error scanning QR Code", exception)
                        // Handle error, maybe show a message
                    }
                ))
        }

    init {
        viewModelScope.launch {
            state
                .map { it.surfaceRequest }
                .distinctUntilChanged()
                .collect {
                    if (it != null) {
                        imageCapture // This seems to be unused, imageCapture is not being consumed.
                        // Consider if it's needed or if this collect block can be removed/repurposed.
                    }
                }
        }
    }

    fun onAction(action: QRScanAction) {
        when (action) {
            is QRScanAction.OnRequestPermission -> requestedPermission(action.isGranted)
            is QRScanAction.OnBindCamera -> onBindCamera(action.context, action.lifecycleOwner)
            QRScanAction.OnClickCloseApp -> onClickCloseApp()
            QRScanAction.OnClickGrantAccess -> onClickGrantAccess()
        }
    }

    private fun onClickGrantAccess() {
        viewModelScope.launch {
            _event.send(QRScanEvent.GrantCameraAccess)
        }
    }

    private fun onClickCloseApp() {
        viewModelScope.launch {
            _event.send(QRScanEvent.OnCloseApp)
        }
    }

    private fun onBindCamera(
        context: Context, lifecycleOwner: LifecycleOwner
    ) {
        viewModelScope.launch {
            bindToCamera(context, lifecycleOwner)
        }
    }

    private fun requestedPermission(isGranted: Boolean) {
        if (isGranted) {
            _state.value.messageBarState.addSuccess(
                UiText.StringResource(R.string.permission_granted)
            )
        }
        _state.update { it.copy(hasCameraPermission = isGranted) }
        _state.update {
            it.copy(
                showCameraRational = !it.hasCameraPermission
            )
        }
    }

    suspend fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
        val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)
        processCameraProvider.unbindAll() // Unbind existing use cases before rebinding
        processCameraProvider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            cameraPreviewUseCase,
            imageCapture, // Keep if you still need image capture functionality
            imageAnalysis // Add the image analysis use case
        )

        // Cancellation signals we're done with the camera
        try {
            awaitCancellation()
        } finally {
            processCameraProvider.unbindAll()
        }
    }

}

