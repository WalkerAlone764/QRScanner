package com.example.qrscanner.app.presentation

import com.example.qrscanner.qr_scan.domain.model.BarcodeType
import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes {
    @Serializable
    data object Scan: Routes
    @Serializable
    data class Result(
        val type: BarcodeType,
        val value: String
    ): Routes
}