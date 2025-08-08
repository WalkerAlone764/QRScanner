package com.example.qrscanner.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qrscanner.core.ui.theme.QRScannerTheme

@Composable
fun CircularButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    leftIcon: @Composable (() -> Unit)? = null,
) {

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(100))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(
                vertical = 12.dp, horizontal = 18.dp
            )
            .heightIn(
                min = 44.dp
            )
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically

    ) {
        leftIcon?.invoke()
        Text(
            text = text, style = textStyle.copy(
                color = textColor
            )
        )
    }

}

@Preview
@Composable
private fun Preview() {
    QRScannerTheme {
        Row {
            CircularButton(
                onClick = {},
                leftIcon = {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier

                    )
                }, text = "Close App", modifier = Modifier.weight(1f)

            )
        }
    }
}