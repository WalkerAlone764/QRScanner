package com.example.qrscanner.createqr.presentation.create.model

import com.example.qrscanner.R
import com.example.qrscanner.app.presentation.navigation.Routes

data class QRType(
    val name: String,
    val icon: Int,
    val routes: Routes
)

val qrTypeList = listOf<QRType>(
    QRType("Text", R.drawable.ic_text, Routes.TextQR),
    QRType("Link", R.drawable.ic_link, Routes.LinkQR),
    QRType("Contact", R.drawable.ic_contact, Routes.ContactQR),
    QRType("Phone Number", R.drawable.ic_phone, Routes.PhoneNumberQR),
    QRType("Geolocation", R.drawable.ic_location, Routes.GeoLocationQR),
    QRType("Wi-Fi", R.drawable.ic_wifi, Routes.WifiQR)
)
