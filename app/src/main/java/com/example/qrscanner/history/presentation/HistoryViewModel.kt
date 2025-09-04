package com.example.qrscanner.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrscanner.history.presentation.components.SelectableTabItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HistoryViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(HistoryState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HistoryState()
        )

    fun onAction(action: HistoryAction) {
        when (action) {
            is HistoryAction.OnTabSelected -> onTabSelect(action.tabItem)
        }
    }

    private fun onTabSelect(tabItem: SelectableTabItem) {
        _state.update { it.copy(selectedTab = tabItem) }
    }

}