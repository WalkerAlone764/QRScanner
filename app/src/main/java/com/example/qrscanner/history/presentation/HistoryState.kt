package com.example.qrscanner.history.presentation

import com.example.qrscanner.core.domain.qr.QR
import com.example.qrscanner.history.presentation.components.SelectableTabItem

data class HistoryState(
    val selectedTab: SelectableTabItem = SelectableTabItem.Generated,
    val scannedQRs: List<QR> = emptyList(),
    val generatedQRs: List<QR> = emptyList()
)