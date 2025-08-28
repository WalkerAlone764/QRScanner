package com.example.qrscanner.createqr.presentation.phoneqr

sealed interface PhoneQRCreationAction {
    data object OnClickBack : PhoneQRCreationAction
    data object OnGenerateQR : PhoneQRCreationAction
    data class OnChangePhone(val number: String) : PhoneQRCreationAction

}