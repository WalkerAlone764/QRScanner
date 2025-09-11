package com.example.qrscanner.qr_scan.presentation.result

import android.graphics.Bitmap
import com.example.qrscanner.qr_scan.data.model.BarcodeContactResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeGeolocationResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeLinkResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodePhoneResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeTextResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeWifiResultWrapper
import com.example.qrscanner.core.domain.BarcodeType

data class ResultState(
    val isPreview: Boolean = false,
    val type: BarcodeType? = null,
    val bitmap: Bitmap? = null,
    val textResultWrapper: BarcodeTextResultWrapper? = null,
    val wifiResultWrapper: BarcodeWifiResultWrapper? = null,
    val geolocationResultWrapper: BarcodeGeolocationResultWrapper? = null,
    val phoneResultWrapper: BarcodePhoneResultWrapper? = null,
    val contactResultWrapper: BarcodeContactResultWrapper? = null,
    val linkResultWrapper: BarcodeLinkResultWrapper? = null
)