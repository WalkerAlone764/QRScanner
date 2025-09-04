package com.example.qrscanner.history.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed

@Composable
fun TabRow(
    tabs: List<SelectableTabItem>,
    selectedTab: SelectableTabItem,
    onSelectTab: (SelectableTabItem) -> Unit,
    modifier: Modifier = Modifier
) {

    var boxWidth by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    val animatedWidth by animateDpAsState(
        targetValue = with(density) { (boxWidth).toDp() } * (tabs.indexOf(selectedTab))
    )


    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
        ) {

            tabs.fastForEachIndexed { index, tab ->

                val animatedTextColor by animateColorAsState(
                    targetValue = if (tab == selectedTab) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .onSizeChanged {
                                boxWidth = it.width
                            }
                            .clickable {
                                onSelectTab(tab)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab.title,
                            color = animatedTextColor,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }


                }
            }


        }
        Box(
            modifier = Modifier
                .padding(
                    start = animatedWidth,
                )
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                .background(MaterialTheme.colorScheme.onSurface)

                .width(with(density) { boxWidth.toDp() })
                .height(4.dp)
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TabRowPreview() {
    val tabs = SelectableTabItem.entries

    var selectedItem by remember {
        mutableStateOf(SelectableTabItem.Generated)
    }
    TabRow(
        tabs = tabs,
        selectedTab = selectedItem,
        onSelectTab = {
            selectedItem = it
        }
    )
}