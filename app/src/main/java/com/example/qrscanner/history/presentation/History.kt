package com.example.qrscanner.history.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.qrscanner.core.ui.theme.QRScannerTheme
import com.example.qrscanner.core.util.copy
import com.example.qrscanner.history.presentation.components.SelectableTabItem
import com.example.qrscanner.history.presentation.components.TabRow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryRoot(
    viewModel: HistoryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HistoryScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun HistoryScreen(
    state: HistoryState,
    onAction: (HistoryAction) -> Unit,
    bottomTabHeight: Int = 600
) {
    val layoutDirection = LocalLayoutDirection.current
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    var offsetX by remember { mutableFloatStateOf(0f) }
    val draggableState = rememberDraggableState { delta ->
        offsetX += delta
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(
                    innerPadding.copy(
                        layoutDirection = layoutDirection,
                        bottom = 0.dp
                    )
                ),
        ) {
            TabRow(
                tabs = SelectableTabItem.entries,
                selectedTab = state.selectedTab,
                onSelectTab = { tabItem ->
                    onAction(HistoryAction.OnTabSelected(tabItem))
                },
                modifier = Modifier

            )

            Box( // Wrap AnimatedContent in a Box for draggable
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = draggableState,
                        onDragStopped = { velocity ->
                            scope.launch {
                                val screenWidth =
                                    density.run { 1.dp.toPx() * 360 } // Approximate screen width
                                val swipeThreshold =
                                    screenWidth * 0.4f // Example: 40% of screen width

                                val currentTabIndex = state.selectedTab.ordinal
                                val maxTabIndex = SelectableTabItem.entries.lastIndex

                                if (offsetX > swipeThreshold || (velocity > 500 && offsetX > 0)) { // Swipe right
                                    if (currentTabIndex > 0) {
                                        onAction(HistoryAction.OnTabSelected(SelectableTabItem.entries[currentTabIndex - 1]))
                                    }
                                } else if (offsetX < -swipeThreshold || (velocity < -500 && offsetX < 0)) { // Swipe left
                                    if (currentTabIndex < maxTabIndex) {
                                        onAction(HistoryAction.OnTabSelected(SelectableTabItem.entries[currentTabIndex + 1]))
                                    }
                                }
                                offsetX = 0f // Reset offset after swipe action
                            }
                        }
                    )
            ) {
                AnimatedContent(
                    targetState = state.selectedTab,
                    transitionSpec = {
                        val duration = 300 // Slightly faster for swipe feel
                        val delay = 0 // No delay for swipe
                        if (targetState.ordinal > initialState.ordinal) {
                            slideInHorizontally(
                                animationSpec = tween(
                                    durationMillis = duration,
                                    delayMillis = delay
                                ),
                                initialOffsetX = { fullWidth -> fullWidth }
                            ) togetherWith slideOutHorizontally(
                                animationSpec = tween(
                                    durationMillis = duration,
                                    delayMillis = delay
                                ),
                                targetOffsetX = { fullWidth -> -(fullWidth) }
                            )
                        } else {
                            slideInHorizontally(
                                animationSpec = tween(
                                    durationMillis = duration,
                                    delayMillis = delay
                                ),
                                initialOffsetX = { fullWidth -> -(fullWidth) }
                            ) togetherWith slideOutHorizontally(
                                animationSpec = tween(
                                    durationMillis = duration,
                                    delayMillis = delay
                                ),
                                targetOffsetX = { fullWidth -> (fullWidth) }
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                ) { value ->
                    if (value == SelectableTabItem.Generated) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) { // ensure LazyColumn fills size
                            items(state.generatedQRs) { item ->
                                Text(
                                    text = "Generated ${item.title}", // Changed from Scanned
                                    color = Color.Black
                                )
                            }
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) { // ensure LazyColumn fills size
                            items(state.scannedQRs) { item ->
                                Text(
                                    text = "Scanned ${item.title}",
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(with(density) { bottomTabHeight.toDp() })
                    .background(
                        brush = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.3f to Color.Transparent,
                                0.7f to Color.White.copy(0.4f),
                            )
                        )
                    )
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    QRScannerTheme {
        var tabSelected by remember {
            mutableStateOf(SelectableTabItem.Generated)
        }
        HistoryScreen(
            state = HistoryState(
                selectedTab = tabSelected,
                // Sample data for preview
            ),
            onAction = {
                when (it) {
                    is HistoryAction.OnTabSelected -> {
                        tabSelected = it.tabItem
                    }
                }
            }
        )
    }
}

// Assuming ScannedItem looks something like this, adjust if different
data class ScannedItem(val id: Int, val title: String, val content: String, val timestamp: Long)
