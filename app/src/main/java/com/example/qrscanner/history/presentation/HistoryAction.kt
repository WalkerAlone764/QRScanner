package com.example.qrscanner.history.presentation

import com.example.qrscanner.history.presentation.components.SelectableTabItem

sealed interface HistoryAction {
    data class OnTabSelected(val tabItem: SelectableTabItem) : HistoryAction
}