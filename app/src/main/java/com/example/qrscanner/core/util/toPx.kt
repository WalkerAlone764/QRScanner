package com.example.qrscanner.core.util

import android.content.Context
import android.util.TypedValue
import androidx.compose.ui.unit.Dp

fun Dp.toPx(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PX,
        this.value, // Dp.value gives you the Float value of the Dp
        context.resources.displayMetrics
    )
}