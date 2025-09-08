package com.example.qrscanner.core.domain.qr

import com.example.qrscanner.core.domain.BarcodeType
import java.time.Instant

data class QR(
    val isScanned: Boolean,
    val type: BarcodeType,
    val title: String,
    val value: String,
    val createdAt: Instant,
    val id: Long? = null,
)