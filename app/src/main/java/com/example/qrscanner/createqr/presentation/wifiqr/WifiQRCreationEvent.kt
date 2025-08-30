package com.example.qrscanner.createqr.presentation.wifiqr

sealed interface WifiQRCreationEvent {
    data object NavigateBack : WifiQRCreationEvent
    data class NavigateToPreviewScreen(
        val ssid: String,
        val password: String,
        val encryptedType: String
    ) : WifiQRCreationEvent
}