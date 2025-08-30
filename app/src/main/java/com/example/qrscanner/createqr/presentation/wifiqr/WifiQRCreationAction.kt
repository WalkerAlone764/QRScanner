package com.example.qrscanner.createqr.presentation.wifiqr

sealed interface WifiQRCreationAction {

    data object OnClickBack : WifiQRCreationAction
    data object OnGenerateQR : WifiQRCreationAction

    data class OnSSIDChanged(val ssid: String) : WifiQRCreationAction
    data class OnPasswordChanged(val password: String) : WifiQRCreationAction
    data class OnEncryptionChanged(val encryption: String) : WifiQRCreationAction
}