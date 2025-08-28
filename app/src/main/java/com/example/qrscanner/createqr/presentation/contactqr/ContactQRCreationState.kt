package com.example.qrscanner.createqr.presentation.contactqr

data class ContactQRCreationState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
) {
    val canGenerateOr: Boolean
        get() = name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()
}