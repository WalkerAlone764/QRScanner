package com.example.qrscanner.qr_scan.presentation.result

import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.qrscanner.app.presentation.Routes
import com.example.qrscanner.qr_scan.data.model.BarcodeContactResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeGeolocationResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeLinkResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodePhoneResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeTextResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeWifiResultWrapper
import com.example.qrscanner.qr_scan.domain.model.BarcodeType
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json

class ResultViewModel(
    saveStateHandle: SavedStateHandle
) : ViewModel() {

    val route = saveStateHandle.toRoute<Routes.Result>()


    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ResultState())
    val state = _state.onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ResultState()
        )

    init {
        val type = route.type
        when (type) {
            BarcodeType.LINK -> {
                val linkWrapper = Json.decodeFromString<BarcodeLinkResultWrapper>(route.value)
                val qrBitmap = generateQR(text = linkWrapper.url)
                _state.update { it.copy(bitmap = qrBitmap) }
            }

            BarcodeType.CONTACT -> {
                val contactWrapper = Json.decodeFromString<BarcodeContactResultWrapper>(route.value)
                val contactQRContent = "MECARD:N:${contactWrapper.formattedName};TEL:${contactWrapper.phone};EMAIL:${contactWrapper.email};;"
//                val contactQRContent = """
//BEGIN:VCARD
//VERSION:3.0
//N:Doe;John;;;
//FN:John Doe
//TEL;TYPE=CELL:1234567890
//EMAIL:john.doe@email.com
//END:VCARD
//""".trimIndent()

                val qrBitmap = generateQR(text = contactQRContent)
                _state.update { it.copy(bitmap = qrBitmap) }
            }

            BarcodeType.PHONE_NUMBER -> {
                val phoneWrapper = Json.decodeFromString<BarcodePhoneResultWrapper>(route.value)
                val phoneQRContent = "tel:${phoneWrapper.phone}"
                val qrBitmap = generateQR(text = phoneQRContent)
                _state.update { it.copy(bitmap = qrBitmap) }
            }
            BarcodeType.GEOLOCATION -> {
                val geoWrapper = Json.decodeFromString<BarcodeGeolocationResultWrapper>(route.value)
                val geoQRContent = "geo:${geoWrapper.lat},${geoWrapper.long}"
                val qrBitmap = generateQR(text = geoQRContent)
                _state.update { it.copy(bitmap = qrBitmap) }
            }
            BarcodeType.WIFI -> {
                val wifiWrapper = Json.decodeFromString<BarcodeWifiResultWrapper>(route.value)
                val wifiQRContent =
                    "WIFI:S:${wifiWrapper.ssid};T:${wifiWrapper.encryption};P:${wifiWrapper.password};;"
                val qrBitmap = generateQR(text = wifiQRContent)
                _state.update { it.copy(bitmap = qrBitmap) }
            }

            BarcodeType.TEXT -> {
                val textWrapper = Json.decodeFromString<BarcodeTextResultWrapper>(route.value)
                val qrBitmap = generateQR(text = textWrapper.text)
                _state.update { it.copy(bitmap = qrBitmap) }
            }

        }
    }

    fun onAction(action: ResultAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

    fun generateQR(text: String?): Bitmap {
        val size = 512
        val bits = QRCodeWriter().encode(
                text, BarcodeFormat.QR_CODE, size, size
            )
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }

}