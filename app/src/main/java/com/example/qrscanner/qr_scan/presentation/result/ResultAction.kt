package com.example.qrscanner.qr_scan.presentation.result

sealed interface ResultAction {
    data object OnClickCopy : ResultAction
    data object OnClickShare : ResultAction

    data class OnClickLink(val link: String) : ResultAction

}