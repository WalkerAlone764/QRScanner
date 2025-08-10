package com.example.qrscanner.qr_scan.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BarcodeWifiResultWrapper(
    val ssid: String?,
    val password: String?,
    val encryption: String?
)