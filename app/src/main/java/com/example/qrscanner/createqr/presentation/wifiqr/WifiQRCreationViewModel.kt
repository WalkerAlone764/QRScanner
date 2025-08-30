package com.example.qrscanner.createqr.presentation.wifiqr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class WifiQRCreationViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(WifiQRCreationState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = WifiQRCreationState()
        )

    private val _event = Channel<WifiQRCreationEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: WifiQRCreationAction) {
        when (action) {
            WifiQRCreationAction.OnClickBack -> goBack()
            is WifiQRCreationAction.OnEncryptionChanged -> encryptionChanged(action.encryption)
            WifiQRCreationAction.OnGenerateQR -> generateQR()
            is WifiQRCreationAction.OnPasswordChanged -> changePassword(action.password)
            is WifiQRCreationAction.OnSSIDChanged -> changeSSID(action.ssid)
        }
    }

    private fun changeSSID(ssid: String) {
        _state.update { it.copy(ssid = ssid) }
    }

    private fun encryptionChanged(encryption: String) {
        _state.update { it.copy(encryptedType = encryption) }
    }

    private fun changePassword(password: String) {
        _state.update { it.copy(password = password) }
    }

    private fun generateQR() {
        _event.trySend(
            WifiQRCreationEvent.NavigateToPreviewScreen(
                ssid = state.value.ssid,
                password = state.value.password,
                encryptedType = state.value.encryptedType
            )
        )
    }

    private fun goBack() {
        _event.trySend(WifiQRCreationEvent.NavigateBack)
    }

}