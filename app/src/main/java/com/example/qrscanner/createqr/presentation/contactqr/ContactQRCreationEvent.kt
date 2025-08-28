package com.example.qrscanner.createqr.presentation.contactqr

sealed interface ContactQRCreationEvent {
    data object NavigateBack : ContactQRCreationEvent
    data class NavigateToPreviewScreen(
        val name: String,
        val email: String,
        val phone: String
    ) : ContactQRCreationEvent
}