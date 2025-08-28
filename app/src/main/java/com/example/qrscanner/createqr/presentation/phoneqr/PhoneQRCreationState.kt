package com.example.qrscanner.createqr.presentation.phoneqr

data class PhoneQRCreationState(
    val phone: String = ""
) {
    val canGenerateOR: Boolean
        get() = phone.isNotEmpty()
}