package com.example.qrscanner.createqr.presentation.linkqr

sealed interface LinkQRCreationAction {
    data object OnClickBack : LinkQRCreationAction
    data object OnGenerateQR : LinkQRCreationAction
    data class OnChangeUrl(val url: String) : LinkQRCreationAction
}