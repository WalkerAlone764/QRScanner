package com.example.qrscanner.createqr.presentation.create

import com.example.qrscanner.createqr.presentation.create.model.QRType

sealed interface CreateAction {
    data class OnClickCreate(val qrType: QRType): CreateAction
}