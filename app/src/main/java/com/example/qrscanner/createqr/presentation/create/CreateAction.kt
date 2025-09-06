package com.example.qrscanner.createqr.presentation.create

import com.example.qrscanner.createqr.presentation.create.model.QRCodeDetails

sealed interface CreateAction {
    data class OnClickCreate(val qrCodeDetails: QRCodeDetails): CreateAction
}