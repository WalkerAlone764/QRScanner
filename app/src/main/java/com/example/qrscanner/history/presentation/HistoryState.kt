package com.example.qrscanner.history.presentation

import com.example.qrscanner.history.presentation.components.SelectableTabItem

data class HistoryState(
    val selectedTab: SelectableTabItem = SelectableTabItem.Generated
)