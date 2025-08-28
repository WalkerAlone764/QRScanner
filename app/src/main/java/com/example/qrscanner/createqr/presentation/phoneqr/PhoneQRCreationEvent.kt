package com.example.qrscanner.createqr.presentation.phoneqr

sealed interface PhoneQRCreationEvent {
    data object NavigateBack : PhoneQRCreationEvent
    data class NavigateToPreviewScreen(
        val number: String
    ) : PhoneQRCreationEvent
}