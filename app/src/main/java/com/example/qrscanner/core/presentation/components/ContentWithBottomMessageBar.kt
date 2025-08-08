package com.example.qrscanner.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.qrscanner.core.presentation.util.MessageBarState
import com.example.qrscanner.core.presentation.util.MessageType
import com.example.qrscanner.core.ui.theme.onSuccess
import com.example.qrscanner.core.ui.theme.success
import java.util.Timer
import kotlin.concurrent.schedule

@Composable
fun ContentWithBottomMessageBar(
    modifier: Modifier = Modifier,
    state: MessageBarState = MessageBarState(),
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
    content: @Composable () -> Unit,

    ) {

    DisposableEffect(state.isVisible) {
        val timer = Timer("MessageBar", true)

        if (state.isVisible) {
            timer.schedule(2000) {
                state.clear()
            }
        }
        onDispose {
            timer.cancel()
            timer.purge()
        }
    }

    Box(
        modifier = modifier, contentAlignment = Alignment.BottomCenter
    ) {
        content()
        AnimatedVisibility(
            visible = state.isVisible && state.messageType != null,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .navigationBarsPadding()
                    .clip(RoundedCornerShape(24))
                    .background(
                        if (state.messageType == MessageType.SUCCESS) {
                            MaterialTheme.colorScheme.success
                        } else if (state.messageType == MessageType.ERROR) {
                            MaterialTheme.colorScheme.error
                        } else {
                            Color.Transparent
                        }
                    )
                    .padding(
                        horizontal = 12.dp, vertical = 8.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Check, contentDescription = null
                )

                if (state.messageType == MessageType.SUCCESS) {
                    Text(
                        text = state.success?.asString() ?: "",
                        color = MaterialTheme.colorScheme.onSuccess,
                        style = textStyle
                    )
                }

                if (state.messageType == MessageType.ERROR) {
                    Text(
                        text = state.error?.asString() ?: "",
                        color = MaterialTheme.colorScheme.onSuccess,
                        style = textStyle
                    )
                }
            }
        }
    }
}