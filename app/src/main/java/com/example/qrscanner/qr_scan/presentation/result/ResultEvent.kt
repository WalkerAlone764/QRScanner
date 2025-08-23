package com.example.qrscanner.qr_scan.presentation.result

sealed interface ResultEvent {
    data class ClickCopy(
        val content: String
    ) : ResultEvent

    data class ClickShare(
        val content: String
    ) : ResultEvent

    data class ClickLink(
        val link: String
    ) : ResultEvent
}