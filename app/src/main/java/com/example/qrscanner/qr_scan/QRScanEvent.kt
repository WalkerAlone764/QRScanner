package com.example.qrscanner.qr_scan

sealed interface QRScanEvent {
    data object OnCloseApp: QRScanEvent
    data object GrantCameraAccess: QRScanEvent
}