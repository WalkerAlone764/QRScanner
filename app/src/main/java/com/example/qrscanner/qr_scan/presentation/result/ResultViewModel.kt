package com.example.qrscanner.qr_scan.presentation.result

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.qrscanner.app.presentation.navigation.Routes
import com.example.qrscanner.core.domain.qr.QRDataSource
import com.example.qrscanner.qr_scan.data.model.BarcodeContactResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeGeolocationResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeLinkResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodePhoneResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeTextResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeWifiResultWrapper
import com.example.qrscanner.core.domain.BarcodeType
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.Instant

class ResultViewModel(
    saveStateHandle: SavedStateHandle,
    private val qrDataSource: QRDataSource
) : ViewModel() {

    val route = saveStateHandle.toRoute<Routes.Result>()
    private var hasLoadedInitialData = false

    private var id = MutableStateFlow(0L)

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

    private val _event = Channel<ResultEvent>()
    val event = _event.receiveAsFlow()

    init {
        val type = route.type
        _state.update { it.copy(type = type, isPreview = route.isGenerated) }
        when (type) {
            BarcodeType.LINK -> {
                val linkWrapper = Json.decodeFromString<BarcodeLinkResultWrapper>(route.value)
                val qrBitmap = generateQR(text = linkWrapper.url)
                _state.update { it.copy(bitmap = qrBitmap, linkResultWrapper = linkWrapper) }
            }

            BarcodeType.CONTACT -> {
                val contactWrapper = Json.decodeFromString<BarcodeContactResultWrapper>(route.value)
                val contactQRContent =
                    "MECARD:N:${contactWrapper.formattedName};TEL:${contactWrapper.phone};EMAIL:${contactWrapper.email};;"
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
                _state.update { it.copy(bitmap = qrBitmap, contactResultWrapper = contactWrapper) }
            }

            BarcodeType.PHONE_NUMBER -> {
                val phoneWrapper = Json.decodeFromString<BarcodePhoneResultWrapper>(route.value)
                val phoneQRContent = "tel:${phoneWrapper.phone}"
                val qrBitmap = generateQR(text = phoneQRContent)
                _state.update { it.copy(bitmap = qrBitmap, phoneResultWrapper = phoneWrapper) }
            }

            BarcodeType.GEO_LOCATION -> {
                val geoWrapper = Json.decodeFromString<BarcodeGeolocationResultWrapper>(route.value)
                val geoQRContent = "geo:${geoWrapper.lat},${geoWrapper.long}"
                val qrBitmap = generateQR(text = geoQRContent)
                _state.update { it.copy(bitmap = qrBitmap, geolocationResultWrapper = geoWrapper) }
            }

            BarcodeType.WIFI -> {
                val wifiWrapper = Json.decodeFromString<BarcodeWifiResultWrapper>(route.value)
                val wifiQRContent =
                    "WIFI:S:${wifiWrapper.ssid};T:${wifiWrapper.encryption};P:${wifiWrapper.password};;"
                val qrBitmap = generateQR(text = wifiQRContent)
                _state.update { it.copy(bitmap = qrBitmap, wifiResultWrapper = wifiWrapper) }
            }

            BarcodeType.TEXT -> {
                val textWrapper = Json.decodeFromString<BarcodeTextResultWrapper>(route.value)
                val qrBitmap = generateQR(text = textWrapper.text)
                _state.update { it.copy(bitmap = qrBitmap, textResultWrapper = textWrapper) }
            }

        }
    }

    fun onAction(action: ResultAction) {
        when (action) {
            ResultAction.OnClickCopy -> onCopy()
            ResultAction.OnClickShare -> onShare()
            is ResultAction.OnClickLink -> onCLickLink(action.link)
            is ResultAction.OnLossFocus -> updateQRDataToDB(action.title)
        }
    }

    private fun updateQRDataToDB(
        title: String
    ) {
        viewModelScope.launch {
            val resultID = qrDataSource.insertQR(
                com.example.qrscanner.core.domain.qr.QR(
                    id = id.value,
                    isScanned = route.isGenerated,
                    type = route.type,
                    title = title,
                    value = route.value,
                    createdAt = Instant.now()
                )
            )

            Log.d("result", resultID.toString())
            if (resultID > 0) {
                id.update { resultID }
            }
        }
    }
    private fun onCLickLink(link: String) {
        viewModelScope.launch {
            _event.send(
                ResultEvent.ClickLink(
                    link
                )
            )
        }

    }

    private fun onShare() {
        viewModelScope.launch {
            val text = convertToString()
            _event.send(
                ResultEvent.ClickShare(
                    text
                )
            )
        }
    }

    private fun onCopy() {
        viewModelScope.launch {
            val text = convertToString()
            _event.send(
                ResultEvent.ClickCopy(
                    text
                )
            )
        }
    }

    private fun convertToString(): String {
        val type = _state.value.type
        return when (type) {
            BarcodeType.LINK -> _state.value.linkResultWrapper?.url ?: ""
            BarcodeType.CONTACT -> "${_state.value.contactResultWrapper?.formattedName ?: ""} ${_state.value.contactResultWrapper?.phone ?: ""} ${_state.value.contactResultWrapper?.email ?: ""}"
            BarcodeType.PHONE_NUMBER -> _state.value.phoneResultWrapper?.phone ?: ""
            BarcodeType.GEO_LOCATION -> "${_state.value.geolocationResultWrapper?.lat ?: ""} ${_state.value.geolocationResultWrapper?.long ?: ""}"
            BarcodeType.WIFI -> "SSID: ${_state.value.wifiResultWrapper?.ssid ?: ""}\nPassword: ${_state.value.wifiResultWrapper?.password ?: ""}\nEncryption type: ${_state.value.wifiResultWrapper?.encryption ?: ""}"
            BarcodeType.TEXT -> _state.value.textResultWrapper?.text ?: ""
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