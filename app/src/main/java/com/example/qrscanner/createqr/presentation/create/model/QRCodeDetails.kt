package com.example.qrscanner.createqr.presentation.create.model

import com.example.qrscanner.R
import com.example.qrscanner.app.presentation.navigation.Routes
import com.example.qrscanner.core.domain.QRType

data class QRCodeDetails(
    val type: QRType,
    val icon: Int,
    val routes: Routes
)

val qrCodeDetailsLists = listOf<QRCodeDetails>(
    QRCodeDetails(QRType.TEXT, R.drawable.ic_text, Routes.TextQR),
    QRCodeDetails(QRType.LINK, R.drawable.ic_link, Routes.LinkQR),
    QRCodeDetails(QRType.CONTACT, R.drawable.ic_contact, Routes.ContactQR),
    QRCodeDetails(QRType.PHONE_NUMBER, R.drawable.ic_phone, Routes.PhoneNumberQR),
    QRCodeDetails(QRType.GEO_LOCATION, R.drawable.ic_location, Routes.GeoLocationQR),
    QRCodeDetails(QRType.WIFI, R.drawable.ic_wifi, Routes.WifiQR)
)
