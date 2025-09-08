package com.example.qrscanner.qr_scan.presentation.scan

import com.example.qrscanner.core.domain.BarcodeType

sealed interface QRScanEvent {
    data object OnCloseApp: QRScanEvent
    data object GrantCameraAccess: QRScanEvent

    data class OnSuccess(val type: BarcodeType, val value: String): QRScanEvent
}