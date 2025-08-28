package com.example.qrscanner.createqr.presentation.linkqr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class LinkQRCreationViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(LinkQRCreationState())
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
            initialValue = LinkQRCreationState()
        )

    private val _event = Channel<LinkQRCreationEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: LinkQRCreationAction) {
        when (action) {
            is LinkQRCreationAction.OnChangeUrl -> changeUrl(action.url)
            LinkQRCreationAction.OnClickBack -> goBack()
            LinkQRCreationAction.OnGenerateQR -> generateQR()
        }
    }

    private fun generateQR() {
        _event.trySend(LinkQRCreationEvent.NavigateToPreviewScreen(state.value.url))
    }

    private fun goBack() {
        _event.trySend(LinkQRCreationEvent.NavigateBack)
    }

    private fun changeUrl(url: String) {
        _state.update { it.copy(url = url) }
    }

}