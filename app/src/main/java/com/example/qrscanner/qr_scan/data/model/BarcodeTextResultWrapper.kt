package com.example.qrscanner.qr_scan.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BarcodeTextResultWrapper(
    val text: String?,
)