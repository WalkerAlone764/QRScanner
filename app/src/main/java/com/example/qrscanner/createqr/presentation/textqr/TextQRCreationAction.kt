package com.example.qrscanner.createqr.presentation.textqr
sealed interface TextQRCreationAction {
    data object OnClickBack : TextQRCreationAction
    data class OnChangeText(val text: String): TextQRCreationAction
    data object OnGenerateQR : TextQRCreationAction
}