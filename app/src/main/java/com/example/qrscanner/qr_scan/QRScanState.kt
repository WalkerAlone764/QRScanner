package com.example.qrscanner.qr_scan

import androidx.camera.core.SurfaceRequest

data class QRScanState(
    val hasCameraPermission: Boolean = false,
    val showCameraRational: Boolean = false,
    val surfaceRequest: SurfaceRequest? = null,
)