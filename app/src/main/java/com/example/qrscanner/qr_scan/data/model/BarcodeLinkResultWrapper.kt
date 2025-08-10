package com.example.qrscanner.qr_scan.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BarcodeLinkResultWrapper(
    val title: String?,
    val url: String?
)
