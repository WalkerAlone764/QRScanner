package com.example.qrscanner.core.presentation.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

data class MessageBarState(
    val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) {
    var isVisible: Boolean by mutableStateOf(false)
    var error: UiText? by mutableStateOf(null)
    var success: UiText? by mutableStateOf(null)
    var messageType: MessageType? = null

    fun addError(error: UiText ) {
        isVisible = true
        this.error = error
        messageType = MessageType.ERROR

    }

    fun addSuccess(success: UiText) {
        isVisible = true
        this.success = success
        messageType = MessageType.SUCCESS
    }

    fun clear() {
        isVisible = false
        this.error = null
        this.success = null
        messageType = null
    }
}

enum class MessageType {
    SUCCESS,
    ERROR,
}
