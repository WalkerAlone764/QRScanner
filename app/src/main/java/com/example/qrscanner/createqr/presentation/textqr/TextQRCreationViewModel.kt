package com.example.qrscanner.createqr.presentation.textqr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class TextQRCreationViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(TextQRCreationState())
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
            initialValue = TextQRCreationState()
        )

    private val _event = Channel<TextQRCreationEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: TextQRCreationAction) {
        when (action) {
            is TextQRCreationAction.OnChangeText -> onChangeText(action.text)
            TextQRCreationAction.OnClickBack -> {
                _event.trySend(TextQRCreationEvent.NavigateBack)
            }

            TextQRCreationAction.OnGenerateQR -> onGenerateQR()
        }
    }

    private fun onGenerateQR() {
        _event.trySend(TextQRCreationEvent.NavigateToPreviewScreen(_state.value.textContent))
    }

    private fun onChangeText(text: String) {
        _state.update { it.copy(textContent = text) }
    }

}