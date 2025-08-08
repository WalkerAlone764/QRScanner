package com.example.qrscanner.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

//  QR Scanner App Colors
val LaserLemon = Color(0xffEBFF69)
val AquaHaze = Color(0xffEDF2F5)
val PureWhite = Color(0xffffffff)
val OuterSpace = Color(0xff273037)
val ShuttleGray = Color(0xff505F6A)
val Madras = Color(0xff373F05)
val RedRibbon = Color(0xffF12244)
val Shamrock = Color(0xff4DDA9D)
val PurpleHeart = Color(0xff583DC5)
val Eucalyptus = Color(0xff259570)
val MaroonFlush = Color(0xffB51D5C)
val OrangeRoughy = Color(0xffC86017)
val PersianBlue = Color(0xff1F44CD)

val ColorScheme.success: Color
    get() = Shamrock

val ColorScheme.onSuccess: Color
    get() = Color.Black

val ColorScheme.text: Color
    get() = PurpleHeart
val ColorScheme.textBackground: Color
    get() = Shamrock.copy(0.1f)

val ColorScheme.contact: Color
    get() = Eucalyptus
val ColorScheme.contactBackground: Color
    get() = Eucalyptus.copy(0.1f)

val ColorScheme.Geo: Color
    get() = MaroonFlush
val ColorScheme.GeoBackground: Color
    get() = MaroonFlush.copy(0.1f)

val ColorScheme.Phone: Color
    get() = OrangeRoughy
val ColorScheme.PhoneBackground: Color
    get() = OrangeRoughy.copy(0.1f)

val ColorScheme.Wifi: Color
    get() = PersianBlue
val ColorScheme.WifiBackground: Color
    get() = PersianBlue.copy(0.1f)

val ColorScheme.onOverlay: Color
    get() = PureWhite