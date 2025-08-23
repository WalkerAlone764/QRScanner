package com.example.qrscanner.qr_scan.presentation.result.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qrscanner.R
import com.example.qrscanner.core.ui.theme.QRScannerTheme

@Composable
fun ColumnScope.GeoContent(
    latitude: String,
    longitude: String
) {
    Text(
        text = stringResource(R.string.geolocation),
        style = MaterialTheme.typography.titleMedium.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
        text = "$latitude, $longitude",
        style = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    QRScannerTheme {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            GeoContent(
                latitude = "48.423123123123",
                longitude = "2.231312312312"
            )
        }
    }
}