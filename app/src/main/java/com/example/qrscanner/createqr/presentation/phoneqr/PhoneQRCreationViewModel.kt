package com.example.qrscanner.createqr.presentation.phoneqr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class PhoneQRCreationViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(PhoneQRCreationState())
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
            initialValue = PhoneQRCreationState()
        )

    private val _event = Channel<PhoneQRCreationEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: PhoneQRCreationAction) {
        when (action) {
            is PhoneQRCreationAction.OnChangePhone -> onPhoneChange(action.number)
            PhoneQRCreationAction.OnClickBack -> goBack()
            PhoneQRCreationAction.OnGenerateQR -> generateQR()
        }
    }

    private fun goBack() {
        _event.trySend(PhoneQRCreationEvent.NavigateBack)
    }

    private fun generateQR() {
        _event.trySend(PhoneQRCreationEvent.NavigateToPreviewScreen(_state.value.phone))
    }

    private fun onPhoneChange(number: String) {
        _state.update { it.copy(phone = number) }
    }

}