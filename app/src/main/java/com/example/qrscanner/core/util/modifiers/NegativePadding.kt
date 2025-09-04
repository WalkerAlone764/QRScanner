package com.example.qrscanner.core.util.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp

fun Modifier.negativePadding(all: Dp): Modifier = this.then(
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        // Add the negative padding to the placeable width and height
        val width = placeable.width + (all * 2).roundToPx()
        val height = placeable.height + (all * 2).roundToPx()

        layout(width, height) {
            // Place the composable with the negative padding
            placeable.placeRelative(-all.roundToPx(), -all.roundToPx())
        }
    }
)

fun Modifier.negativePadding(horizontal: Dp = Dp.Hairline, vertical: Dp = Dp.Hairline): Modifier =
    this.then(
        layout { measurable, constraints ->
            val placeable = measurable.measure(
                constraints.copy(
                    minWidth = constraints.minWidth + (horizontal * 2).roundToPx(),
                    maxWidth = constraints.maxWidth + (horizontal * 2).roundToPx(),
                    minHeight = constraints.minHeight + (vertical * 2).roundToPx(),
                    maxHeight = constraints.maxHeight + (vertical * 2).roundToPx(),
                )
            )

            // Add the negative padding to the placeable width and height


            layout(placeable.width, placeable.height) {
                // Place the composable with the negative padding
                placeable.placeRelative(0, 0)
            }
        }
    )

fun Modifier.negativePadding(
    start: Dp = Dp.Hairline,
    top: Dp = Dp.Hairline,
    end: Dp = Dp.Hairline,
    bottom: Dp = Dp.Hairline
): Modifier = this.then(
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        // Add the negative padding to the placeable width and height
        val width = placeable.width + (start + end).roundToPx()
        val height = placeable.height + (top + bottom).roundToPx()

        layout(width, height) {
            // Place the composable with the negative padding
            placeable.placeRelative(-start.roundToPx(), -top.roundToPx())
        }
    }
)
