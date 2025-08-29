package com.example.qrscanner.createqr.presentation.geolocationqr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class GeolocationQRCreationViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(GeolocationQRCreationState())
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
            initialValue = GeolocationQRCreationState()
        )

    private val _event = Channel<GeolocationQRCreationEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: GeolocationQRCreationAction) {
        when (action) {
            is GeolocationQRCreationAction.OnChangeLatitude -> changeLatitude(action.latitude)
            is GeolocationQRCreationAction.OnChangeLongitude -> changeLongitude(action.longitude)
            GeolocationQRCreationAction.OnClickBack -> clickBack()
            GeolocationQRCreationAction.OnGenerateQR -> generateQR()
        }
    }

    private fun clickBack() {
        _event.trySend(GeolocationQRCreationEvent.NavigateBack)
    }

    private fun generateQR() {
        _event.trySend(
            GeolocationQRCreationEvent.NavigateToPreviewScreen(
                latitude = state.value.latitude,
                longitude = state.value.longitude,
            )
        )
    }

    private fun changeLongitude(longitude: String) {
        _state.update { it.copy(longitude = longitude) }
    }

    private fun changeLatitude(latitude: String) {
        _state.update { it.copy(latitude = latitude) }
    }

}