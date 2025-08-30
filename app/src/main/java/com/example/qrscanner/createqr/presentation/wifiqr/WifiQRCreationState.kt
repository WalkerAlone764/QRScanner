package com.example.qrscanner.createqr.presentation.wifiqr

data class WifiQRCreationState(
    val ssid: String = "",
    val password: String = "",
    val encryptedType: String = "",
) {
    val canGenerateOr: Boolean
        get() = ssid.isNotEmpty() && password.isNotEmpty() && encryptedType.isNotEmpty()
}