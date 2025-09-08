package com.example.qrscanner.core.domain

enum class BarcodeType(val title: String) {
    TEXT("Text"),
    LINK("Link"),
    CONTACT("Contact"),
    PHONE_NUMBER("Phone Number"),
    GEO_LOCATION("Geolocation"),
    WIFI("Wi-Fi")
}