package com.example.qrscanner.createqr.presentation.geolocationqr 
sealed interface GeolocationQRCreationAction {

    data object OnClickBack: GeolocationQRCreationAction
    data object OnGenerateQR: GeolocationQRCreationAction
    data class OnChangeLatitude(val latitude: String): GeolocationQRCreationAction
    data class OnChangeLongitude(val longitude: String): GeolocationQRCreationAction
}