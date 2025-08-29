package com.example.qrscanner.createqr.presentation.geolocationqr

data class GeolocationQRCreationState(
    val latitude: String = "",
    val longitude: String = ""
) {
    val canGenerateQR: Boolean
        get() = latitude.isNotBlank() && longitude.isNotBlank()
}