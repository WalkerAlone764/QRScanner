package com.example.qrscanner.app.presentation.navigation

import com.example.qrscanner.qr_scan.domain.model.BarcodeType
import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes {

    @Serializable
    data object Scan : Routes

    @Serializable
    data class Result(
        val isPreview: Boolean = false,
        val type: BarcodeType,
        val value: String
    ) : Routes

    @Serializable
    data object Create : Routes

    @Serializable
    data object Reset : Routes

    @Serializable
    data object TextQR : Routes

    @Serializable
    data object LinkQR: Routes

    @Serializable
    data object ContactQR: Routes

    @Serializable
    data object PhoneNumberQR: Routes

    @Serializable
    data object GeoLocationQR: Routes

    @Serializable
    data object WifiQR: Routes
}