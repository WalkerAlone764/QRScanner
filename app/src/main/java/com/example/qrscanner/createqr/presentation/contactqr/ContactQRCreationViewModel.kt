package com.example.qrscanner.createqr.presentation.contactqr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ContactQRCreationViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ContactQRCreationState())
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
            initialValue = ContactQRCreationState()
        )

    private val _event = Channel<ContactQRCreationEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: ContactQRCreationAction) {
        when (action) {
            ContactQRCreationAction.OnClickBack -> goBack()
            ContactQRCreationAction.OnGenerateQR -> generateQR()
            is ContactQRCreationAction.OnChangeEmail -> changeEmail(action.email)
            is ContactQRCreationAction.OnChangeName -> changeName(action.name)
            is ContactQRCreationAction.OnChangePhone -> changePhone(action.phone)
        }
    }

    private fun goBack() {
        _event.trySend(ContactQRCreationEvent.NavigateBack)
    }

    private fun generateQR() {
        _event.trySend(ContactQRCreationEvent.NavigateToPreviewScreen(
            name = state.value.name,
            email = state.value.email,
            phone = state.value.phone
        ))
    }

    private fun changeEmail(email: String) {
        _state.update { it.copy(email = email) }
    }

    private fun changePhone(phone: String) {
        _state.update { it.copy(phone = phone) }
    }

    private fun changeName(name: String) {
        _state.update { it.copy(name = name) }
    }

}