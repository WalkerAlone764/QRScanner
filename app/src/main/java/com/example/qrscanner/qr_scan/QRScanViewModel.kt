package com.example.qrscanner.qr_scan

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    private val cameraPreviewUseCase = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            _state.update {
                it.copy(
                    surfaceRequest = newSurfaceRequest
                )
            }
        }
    }

    fun onAction(action: QRScanAction) {
        when (action) {
            is QRScanAction.OnRequestPermission -> requestedPermission(action.isGranted)
            is QRScanAction.OnBindCamera -> onBindCamera(action.context, action.lifecycleOwner)
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
        _state.update { it.copy(hasCameraPermission = isGranted) }
        _state.update {
            it.copy(
                showCameraRational = !it.hasCameraPermission
            )
        }
    }

    suspend fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
        val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)
        processCameraProvider.bindToLifecycle(
            lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, cameraPreviewUseCase
        )

        // Cancellation signals we're done with the camera
        try {
            awaitCancellation()
        } finally {
            processCameraProvider.unbindAll()
        }
    }
}