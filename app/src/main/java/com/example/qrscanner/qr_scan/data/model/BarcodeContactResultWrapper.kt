package com.example.qrscanner.qr_scan.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BarcodeContactResultWrapper(
    val formattedName: String?,
    val prefix: String?,
    val middle: String?,
    val last: String?,
    val phone: String?,
    val email: String?,
)