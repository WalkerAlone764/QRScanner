package com.example.qrscanner.createqr.presentation.linkqr

data class LinkQRCreationState(
    val url: String = ""
) {
    val canGenerateOr: Boolean
        get() = url.isNotBlank() && (url.startsWith("https://") || url.startsWith("www"))
}