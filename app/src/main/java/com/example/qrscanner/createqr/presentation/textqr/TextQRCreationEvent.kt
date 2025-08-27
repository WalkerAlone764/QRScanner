package com.example.qrscanner.createqr.presentation.textqr

sealed interface TextQRCreationEvent {
    data object NavigateBack : TextQRCreationEvent
    data class NavigateToPreviewScreen(
        val textContent: String
    ) : TextQRCreationEvent
}