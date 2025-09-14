package com.example.qrscanner.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrscanner.core.domain.qr.QR
import com.example.qrscanner.core.domain.qr.QRDataSource
import com.example.qrscanner.history.presentation.components.SelectableTabItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HistoryViewModel(
    private val qrDataSource: QRDataSource
) : ViewModel() {

    private var hasLoadedInitialData = false

    private var scannedDataSourceJob: Job? = null
    private var generatedDataSourceJob: Job? = null

    private val _state = MutableStateFlow(HistoryState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .onEach {
            if (it.selectedTab == SelectableTabItem.Scanned) {
                generatedDataSourceJob?.cancel()
                scannedDataSourceJob = qrDataSource
                    .observeScannedQR()
                    .onEach { scannedQRs ->
                        _state.update { state ->
                            it.copy(
                                scannedQRs = scannedQRs,
                                generatedQRs = emptyList()
                            )
                        }
                    }
                    .launchIn(viewModelScope)
            } else if (it.selectedTab == SelectableTabItem.Generated) {
                scannedDataSourceJob?.cancel()
                generatedDataSourceJob = qrDataSource
                    .observeGeneratedQR()
                    .onEach { generatedQRs ->
                        _state.update { state ->
                            it.copy(
                                scannedQRs = emptyList(),
                                generatedQRs = generatedQRs
                            )
                        }
                    }
                    .launchIn(viewModelScope)
            }

        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HistoryState()
        )

    private val _event = Channel<HistoryEvent>()
    val event = _event.receiveAsFlow()


    fun onAction(action: HistoryAction) {
        when (action) {
            is HistoryAction.OnTabSelected -> onTabSelect(action.tabItem)
            is HistoryAction.OnClickCard -> onClickCard(action.qr)
        }
    }

    private fun onClickCard(qr: QR) {
        _event.trySend(HistoryEvent.OnClickCard(qr))
    }

    private fun onTabSelect(tabItem: SelectableTabItem) {
        _state.update { it.copy(selectedTab = tabItem) }
    }

}