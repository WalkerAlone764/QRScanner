package com.example.qrscanner.qr_scan.data

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.qrscanner.qr_scan.domain.QRAnalysis
import com.example.qrscanner.qr_scan.domain.QRAnalysisResult
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AndroidQRAnalysis : QRAnalysis, ImageAnalysis.Analyzer {

    private val scope = CoroutineScope(Dispatchers.Main)

    private val _isLoading = MutableStateFlow<Boolean>(false)
    override val isLoading: StateFlow<Boolean>
        get() = _isLoading.asStateFlow()

    private val _result = MutableSharedFlow<QRAnalysisResult>()
    override val result: SharedFlow<QRAnalysisResult>
        get() = _result.asSharedFlow()

    private val scannerOptions = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()

    private val scanner = BarcodeScanning.getClient(scannerOptions)


    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
//            _isLoading.update { true }
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanner
                .process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        barcodes.firstOrNull()?.rawValue?.let { value ->
                            scope.launch {
                                _result.emit(QRAnalysisResult.Success(value))
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    scope.launch {
                        _result.emit(QRAnalysisResult.Error(exception))
                    }
                }
                .addOnCompleteListener {
                    imageProxy.close()
//                    _isLoading.update { false }
                }
        }
    }
}