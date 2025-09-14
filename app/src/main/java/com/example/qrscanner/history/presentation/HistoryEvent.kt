package com.example.qrscanner.history.presentation

import com.example.qrscanner.core.domain.qr.QR

sealed interface HistoryEvent {
    data class OnClickCard(val qr: QR): HistoryEvent
}