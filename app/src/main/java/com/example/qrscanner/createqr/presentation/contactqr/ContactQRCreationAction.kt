package com.example.qrscanner.createqr.presentation.contactqr

sealed interface ContactQRCreationAction {
    data object OnClickBack : ContactQRCreationAction
    data object OnGenerateQR : ContactQRCreationAction
    data class OnChangeName(val name: String) : ContactQRCreationAction
    data class OnChangeEmail(val email: String) : ContactQRCreationAction
    data class OnChangePhone(val phone: String) : ContactQRCreationAction

}