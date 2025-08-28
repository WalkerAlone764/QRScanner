package com.example.qrscanner.createqr.presentation.linkqr

sealed interface LinkQRCreationEvent {
    data object NavigateBack : LinkQRCreationEvent
    data class NavigateToPreviewScreen(
        val url: String
    ) : LinkQRCreationEvent
}