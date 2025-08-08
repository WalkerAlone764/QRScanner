package com.example.qrscanner.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.qrscanner.R

// Set of Material typography styles to start with

val SUSE = FontFamily(
    fonts = listOf(
        Font(
            resId = R.font.suse_regular,
            weight = FontWeight.Normal,
        ),
        Font(
            resId = R.font.suse_semibold,
            weight = FontWeight.SemiBold,
        ),
        Font(
            resId = R.font.suse_medium,
            weight = FontWeight.Medium,
        ),
    )
)
val Typography = Typography(
    titleMedium = TextStyle(
        fontFamily = SUSE,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 32.sp,
    ), titleSmall = TextStyle(
        fontFamily = SUSE,
        fontWeight = FontWeight.SemiBold,
        fontSize = 19.sp,
        lineHeight = 24.sp,

        ), labelLarge = TextStyle(
        fontFamily = SUSE,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    ), bodyLarge = TextStyle(
        fontFamily = SUSE,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    )
)