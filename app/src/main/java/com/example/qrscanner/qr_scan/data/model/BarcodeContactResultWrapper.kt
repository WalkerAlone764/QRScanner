package com.example.qrscanner.qr_scan.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BarcodeContactResultWrapper(
    val name: String?,
    val phone: String?,
    val email: String?,
)