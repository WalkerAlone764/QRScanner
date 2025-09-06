package com.example.qrscanner.createqr.presentation.create

import androidx.lifecycle.ViewModel
import com.example.qrscanner.createqr.presentation.create.model.QRCodeDetails
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class CreateViewModel : ViewModel() {

    private val _event = Channel<CreateEvent>()
    val event = _event.receiveAsFlow()
    fun onAction(action: CreateAction) {
        when (action) {
            is CreateAction.OnClickCreate -> onNavigateToCreate(action.qrCodeDetails)
        }
    }

    private fun onNavigateToCreate(qrCodeDetails: QRCodeDetails) {
        _event.trySend(CreateEvent.NavigateToCreate(qrCodeDetails))
    }

}