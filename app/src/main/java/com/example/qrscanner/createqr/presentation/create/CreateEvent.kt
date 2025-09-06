package com.example.qrscanner.createqr.presentation.create

import com.example.qrscanner.createqr.presentation.create.model.QRCodeDetails

sealed interface CreateEvent {
    data class NavigateToCreate(val qrCodeDetails: QRCodeDetails): CreateEvent
}