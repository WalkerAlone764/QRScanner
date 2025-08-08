package com.example.qrscanner.qr_scan.presentation.scan

import androidx.camera.core.SurfaceRequest
import com.example.qrscanner.core.presentation.util.MessageBarState

data class QRScanState(
    val hasCameraPermission: Boolean = false,
    val showCameraRational: Boolean = false,
    val surfaceRequest: SurfaceRequest? = null,
    val messageBarState: MessageBarState = MessageBarState()
)