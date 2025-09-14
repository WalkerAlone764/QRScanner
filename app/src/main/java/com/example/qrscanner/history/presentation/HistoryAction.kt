package com.example.qrscanner.history.presentation

import com.example.qrscanner.core.domain.qr.QR
import com.example.qrscanner.history.presentation.components.SelectableTabItem

sealed interface HistoryAction {
    data class OnTabSelected(val tabItem: SelectableTabItem) : HistoryAction
    data class OnClickCard(val qr: QR) : HistoryAction
}