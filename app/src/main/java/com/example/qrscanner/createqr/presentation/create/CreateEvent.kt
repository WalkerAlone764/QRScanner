package com.example.qrscanner.createqr.presentation.create

import com.example.qrscanner.createqr.presentation.create.model.QRType

sealed interface CreateEvent {
    data class NavigateToCreate(val qrType: QRType): CreateEvent
}