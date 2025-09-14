package com.example.qrscanner.history.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qrscanner.R
import com.example.qrscanner.core.domain.BarcodeType
import com.example.qrscanner.core.domain.BarcodeType.CONTACT
import com.example.qrscanner.core.domain.BarcodeType.GEO_LOCATION
import com.example.qrscanner.core.domain.BarcodeType.LINK
import com.example.qrscanner.core.domain.BarcodeType.PHONE_NUMBER
import com.example.qrscanner.core.domain.BarcodeType.TEXT
import com.example.qrscanner.core.domain.BarcodeType.WIFI
import com.example.qrscanner.core.domain.qr.QR
import com.example.qrscanner.core.ui.theme.QRScannerTheme
import com.example.qrscanner.qr_scan.data.model.BarcodeContactResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeGeolocationResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeLinkResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodePhoneResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeTextResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeWifiResultWrapper
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun HistoryItem(
    item: QR,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
        .withZone(ZoneId.systemDefault())

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable {
                onClick()
            }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Image(
                imageVector = ImageVector.vectorResource(item.type.getBarcodeTypeIcon()),
                contentDescription = item.type.name,
            )


            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title.takeIf { it.isNotBlank() } ?: item.type.name.replace("_", " ")
                        .let { it.substring(0, 1).uppercase() + it.substring(1).lowercase() },
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                when (item.type) {
                    TEXT -> {
                        TextContent(
                            textContent = Json.decodeFromString<BarcodeTextResultWrapper>(item.value)
                        )
                    }

                    LINK -> LinkContent(
                        linkContent = Json.decodeFromString(item.value)
                    )

                    CONTACT -> ContactContent(
                        contactContent = Json.decodeFromString(item.value)
                    )

                    PHONE_NUMBER -> PhoneContent(
                        phoneContent = Json.decodeFromString(item.value)
                    )

                    GEO_LOCATION -> LocationContent(
                        locationContent = Json.decodeFromString(item.value)
                    )

                    WIFI -> Unit
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dateFormatter.format(item.createdAt),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun TextContent(
    textContent: BarcodeTextResultWrapper
) {
    Text(
        text = textContent.text ?: "",
        fontSize = 14.sp,
        color = Color.DarkGray,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun LinkContent(
    linkContent: BarcodeLinkResultWrapper
) {
    Text(
        text = linkContent.url ?: "",
        fontSize = 14.sp,
        color = Color.DarkGray,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )

}

@Composable
private fun ContactContent(
    contactContent: BarcodeContactResultWrapper
) {
    Column {
        Text(
            text = contactContent.formattedName ?: "",
            fontSize = 14.sp,
            color = Color.DarkGray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = contactContent.email ?: "",
            fontSize = 14.sp,
            color = Color.DarkGray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PhoneContent(
    phoneContent: BarcodePhoneResultWrapper
) {
    Text(
        text = phoneContent.phone ?: "",
        fontSize = 14.sp,
        color = Color.DarkGray,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun LocationContent(
    locationContent: BarcodeGeolocationResultWrapper
) {
    Text(
        text = "${locationContent.lat}, ${locationContent.long}",
        fontSize = 14.sp,
        color = Color.DarkGray,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun WifiContent(
    wifiContent: BarcodeWifiResultWrapper
) {
    Text(
        text = wifiContent.ssid ?: "",
        fontSize = 14.sp,
        color = Color.DarkGray,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
    Text(
        text = wifiContent.password ?: "",
        fontSize = 14.sp,
        color = Color.DarkGray,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

private fun BarcodeType.getBarcodeTypeIcon(): Int {
    return when (this) {
        TEXT -> R.drawable.ic_text
        LINK -> R.drawable.ic_link
        CONTACT -> R.drawable.ic_contact
        PHONE_NUMBER -> R.drawable.ic_phone
        GEO_LOCATION -> R.drawable.ic_location
        WIFI -> R.drawable.ic_wifi
        // Add other types if necessary
        else -> R.drawable.ic_text // Default icon
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewHistoryItemText() {
    QRScannerTheme {
        HistoryItem(
            item = QR(
                isScanned = true,
                title = "Text",
                value = "Adipiscing ipsum lacinia tincidunt sed. In risus dui accumsan accumsan quam morbi nulla. Dictum...",
                type = TEXT,
                createdAt = Instant.parse("2025-06-24T14:36:00Z")
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewHistoryItemLink() {
    QRScannerTheme {
        HistoryItem(
            item = QR(
                isScanned = true,
                title = "Very Long Title That Should Be Ellipsized At Some Point",
                value = "https://www.google.com/search?q=jetpack+compose+preview+background+color&oq=jetpack+compose+preview+background+color&aqs=chrome..69i57j0i22i30l9.1337j0j7&sourceid=chrome&ie=UTF-8",
                type = LINK,
                createdAt = Instant.now()
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewHistoryItemGeo() {
    QRScannerTheme {
        HistoryItem(
            item = QR(
                isScanned = true,
                title = "", // Testing empty title
                value = "geo:37.7749,-122.4194?q=Golden Gate Bridge",
                type = GEO_LOCATION,
                createdAt = Instant.now().minusSeconds(86400 * 5) // 5 days ago
            ),
            onClick = {}
        )
    }
}

