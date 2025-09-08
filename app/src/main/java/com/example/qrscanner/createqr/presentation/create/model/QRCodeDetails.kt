package com.example.qrscanner.createqr.presentation.create.model

import com.example.qrscanner.R
import com.example.qrscanner.app.presentation.navigation.Routes
import com.example.qrscanner.core.domain.BarcodeType

data class QRCodeDetails(
    val type: BarcodeType,
    val icon: Int,
    val routes: Routes
)

val qrCodeDetailsLists = listOf<QRCodeDetails>(
    QRCodeDetails(BarcodeType.TEXT, R.drawable.ic_text, Routes.TextQR),
    QRCodeDetails(BarcodeType.LINK, R.drawable.ic_link, Routes.LinkQR),
    QRCodeDetails(BarcodeType.CONTACT, R.drawable.ic_contact, Routes.ContactQR),
    QRCodeDetails(BarcodeType.PHONE_NUMBER, R.drawable.ic_phone, Routes.PhoneNumberQR),
    QRCodeDetails(BarcodeType.GEO_LOCATION, R.drawable.ic_location, Routes.GeoLocationQR),
    QRCodeDetails(BarcodeType.WIFI, R.drawable.ic_wifi, Routes.WifiQR)
)
