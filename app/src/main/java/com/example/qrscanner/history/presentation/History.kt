package com.example.qrscanner.history.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qrscanner.core.ui.theme.QRScannerTheme
import com.example.qrscanner.core.util.copy
import com.example.qrscanner.history.presentation.components.SelectableTabItem
import com.example.qrscanner.history.presentation.components.TabRow

@Composable
fun HistoryRoot(
    viewModel: HistoryViewModel = viewModel()
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

            AnimatedContent(
                targetState = state.selectedTab,
                transitionSpec = {
                    val duration = 600
                    val delay = 90
                    if (targetState.ordinal > initialState.ordinal) {
                        // New tab is to the right of the old tab.
                        // New content slides in from the right. Old content slides out to the left.
                        slideInHorizontally(
                            animationSpec = tween(durationMillis = duration, delayMillis = delay),
                            initialOffsetX = { fullWidth -> fullWidth }
                        ) togetherWith slideOutHorizontally(
                            animationSpec = tween(durationMillis = duration, delayMillis = delay),
                            targetOffsetX = { fullWidth -> -fullWidth }
                        )
                    } else {
                        // New tab is to the left of the old tab.
                        // New content slides in from the left. Old content slides out to the right.
                        slideInHorizontally(
                            animationSpec = tween(durationMillis = duration, delayMillis = delay),
                            initialOffsetX = { fullWidth -> -fullWidth }
                        ) togetherWith slideOutHorizontally(
                            animationSpec = tween(durationMillis = duration, delayMillis = delay),
                            targetOffsetX = { fullWidth -> fullWidth }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),

                ) { value ->
                if (value == SelectableTabItem.Generated) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Generated"
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Blue),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "Scanned"
                        )
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
                selectedTab = tabSelected
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
