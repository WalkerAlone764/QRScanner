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
import com.example.qrscanner.core.ui.theme.onSurfaceDisabled

@Composable
fun CircularButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    disabledTextColor: Color = MaterialTheme.colorScheme.onSurfaceDisabled,
    backgroundColor: Color = Color.White,
    disabledBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    leftIcon: @Composable (() -> Unit)? = null,
) {

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(100))
            .background(
                if (enabled) backgroundColor else disabledBackgroundColor
            )
            .clickable(onClick = onClick, enabled = enabled)
            .padding(
                vertical = 8.dp, horizontal = 18.dp
            )
            .heightIn(
                min = 12.dp
            )
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically

    ) {
        leftIcon?.invoke()
        Text(
            text = text, style = textStyle.copy(
                color = if (enabled) textColor else disabledTextColor
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
                enabled = false,
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