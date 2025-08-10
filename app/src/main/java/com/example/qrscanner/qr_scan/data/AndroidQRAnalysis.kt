package com.example.qrscanner.qr_scan.data

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.qrscanner.core.util.toBitmap
import com.example.qrscanner.qr_scan.data.model.BarcodeContactResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeGeolocationResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeLinkResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodePhoneResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeTextResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeWifiResultWrapper
import com.example.qrscanner.qr_scan.domain.QRAnalysis
import com.example.qrscanner.qr_scan.domain.model.BarcodeType
import com.example.qrscanner.qr_scan.domain.model.QRAnalysisResult
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class AndroidQRAnalysis : QRAnalysis, ImageAnalysis.Analyzer {
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _isLoading = MutableStateFlow<Boolean>(false)
    override val isLoading: StateFlow<Boolean>
        get() = _isLoading.asStateFlow()

    private val _result = MutableSharedFlow<QRAnalysisResult>()
    override val result: SharedFlow<QRAnalysisResult>
        get() = _result.asSharedFlow()

    private val scannerOptions = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()

    private val scanner = BarcodeScanning.getClient(scannerOptions)


    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
//            _isLoading.update { true }
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            image.mediaImage?.toBitmap()

            scanner
                .process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        barcodes.forEach {
                            Log.d("barcode", it.rawValue.toString())
                        }

                        scope.launch {
                            barcodes?.firstOrNull()?.let { barcode ->
                                val valueType = barcode.valueType
                                when (valueType) {
                                    Barcode.TYPE_GEO -> {
                                        val type = BarcodeType.GEOLOCATION
                                        val geo = barcode.geoPoint
                                        val result = BarcodeGeolocationResultWrapper(
                                            lat = geo?.lat, long = geo?.lng
                                        )
                                        val resultAsJson = Json.encodeToString(result)

                                        _result.emit(
                                            QRAnalysisResult.Success(
                                                type = type,
                                                value = resultAsJson
                                            )
                                        )
                                    }

                                    Barcode.TYPE_URL -> {
                                        val type = BarcodeType.LINK
                                        val url = barcode.url
                                        val result = BarcodeLinkResultWrapper(
                                            title = url?.title,
                                            url = url?.url
                                        )

                                        val resultAsJson = Json.encodeToString(result)
                                        _result.emit(
                                            QRAnalysisResult.Success(
                                                type = type,
                                                value = resultAsJson
                                            )
                                        )
                                    }

                                    Barcode.TYPE_WIFI -> {
                                        val type = BarcodeType.WIFI
                                        val ssid = barcode.wifi!!.ssid
                                        val password = barcode.wifi!!.password
                                        val encryptionType = barcode.wifi!!.encryptionType
                                        val encryption: String = when (encryptionType) {
                                            0 -> "OPEN"
                                            1 -> "WPA"
                                            2 -> "WEP"
                                            3 -> "WPA2"
                                            4 -> "WPA3"
                                            else -> "UNKNOWN" // Handle cases with unexpected or undefined values
                                        }
                                        val result = BarcodeWifiResultWrapper(
                                            ssid = ssid,
                                            password = password,
                                            encryption = encryption
                                        )

                                        val resultAsJson = Json.encodeToString(result)

                                        _result.emit(
                                            QRAnalysisResult.Success(
                                                type = type,
                                                value = resultAsJson
                                            )
                                        )
                                    }

                                    Barcode.TYPE_CONTACT_INFO -> {
                                        val type = BarcodeType.CONTACT
                                        val contactInfo = barcode.contactInfo
                                        val result = BarcodeContactResultWrapper(
                                            formattedName = contactInfo?.name?.formattedName,
                                            prefix = contactInfo?.name?.prefix,
                                            middle = contactInfo?.name?.middle,
                                            last = contactInfo?.name?.last,
                                            phone = contactInfo?.phones?.firstOrNull()?.number,
                                            email = contactInfo?.emails?.firstOrNull()?.address
                                        )

                                        val resultAsJson = Json.encodeToString(result)

                                        _result.emit(
                                            QRAnalysisResult.Success(
                                                type = type,
                                                value = resultAsJson
                                            )
                                        )
                                    }

                                    Barcode.TYPE_TEXT -> {
                                        val type = BarcodeType.TEXT
                                        val text = barcode.rawValue

                                        val result = BarcodeTextResultWrapper(
                                            text = text
                                        )

                                        val resultAsJson = Json.encodeToString(result)

                                        _result.emit(
                                            QRAnalysisResult.Success(
                                                type = type,
                                                value = resultAsJson
                                            )
                                        )

                                    }

                                    Barcode.TYPE_PHONE -> {
                                        val type = BarcodeType.PHONE_NUMBER
                                        val phone = barcode.phone

                                        val result = BarcodePhoneResultWrapper(
                                            phone = phone?.number
                                        )

                                        val resultAsJson = Json.encodeToString(result)

                                        _result.emit(
                                            QRAnalysisResult.Success(
                                                type = type,
                                                value = resultAsJson
                                            )
                                        )

                                    }
                                }
                            }
                        }

                    }
                }
                .addOnFailureListener { exception ->
                    scope.launch {
                        _result.emit(QRAnalysisResult.Error(exception))
                    }
                }
                .addOnCompleteListener {
                    imageProxy.close()
//                    _isLoading.update { false }
                }
        }
    }
}