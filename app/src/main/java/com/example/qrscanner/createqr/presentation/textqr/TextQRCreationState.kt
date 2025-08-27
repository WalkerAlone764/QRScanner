package com.example.qrscanner.createqr.presentation.textqr

data class TextQRCreationState(
    val textContent: String = ""
) {
    val canGenerateOr: Boolean
        get() = textContent.isNotEmpty()
}