package com.example.qrscanner.createqr.presentation.geolocationqr

sealed interface GeolocationQRCreationEvent {
    data object NavigateBack : GeolocationQRCreationEvent
    data class NavigateToPreviewScreen(
        val latitude: String,
        val longitude: String,
    ) : GeolocationQRCreationEvent
}