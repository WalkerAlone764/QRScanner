package com.example.qrscanner.qr_scan.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qrscanner.R
import com.example.qrscanner.core.ui.theme.QRScannerTheme
import com.example.qrscanner.core.ui.theme.linkBackground

@Composable
fun CustomNavBar(
    modifier: Modifier = Modifier
) {
    val backgroundColor = MaterialTheme.colorScheme.linkBackground

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        IconButton(
            onClick = {},
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (false) {
                    MaterialTheme.colorScheme.linkBackground.copy(0.4f)
                } else {
                    Color.Transparent
                }
            )
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_clock_refresh),
                contentDescription = null
            )
        }

        IconButton(
            onClick = {},
            modifier = Modifier
                .drawBehind {
                    drawCircle(
                        color = backgroundColor,
                        center = this.center,
                        radius = 35.dp.toPx()
                    )
                }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_scan),
                contentDescription = null,
                modifier = Modifier
                    .scale(1.2f)
            )
        }

        IconButton(
            onClick = {},
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (true) {
                    MaterialTheme.colorScheme.linkBackground.copy(0.4f)
                } else {
                    Color.Transparent
                }
            )
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_plus_circle),
                contentDescription = null
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    QRScannerTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .height(300.dp),
            verticalArrangement = Arrangement.Center
        ) {
            CustomNavBar(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(50))
            )
        }
    }
}