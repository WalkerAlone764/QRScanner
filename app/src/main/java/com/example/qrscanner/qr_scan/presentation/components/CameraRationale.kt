package com.example.qrscanner.qr_scan.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.qrscanner.core.presentation.components.CircularButton
import com.example.qrscanner.ui.theme.QRScannerTheme

@Composable
fun CameraRationale(
    onClickCloseApp: () -> Unit,
    onClickGrantAccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = {}, properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = modifier.padding(horizontal = 30.dp), shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Camera Required", style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = "This app cannot function without camera access. To scan QR codes, please grant permission.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )

                Row(
                    modifier = Modifier
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularButton(
                        text = "Close App",
                        onClick = onClickCloseApp,
                        textColor = MaterialTheme.colorScheme.error,
                        modifier = Modifier.weight(1f)
                    )
                    CircularButton(
                        text = "Grant Access",
                        onClick = onClickGrantAccess,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

}


@Preview
@Composable
private fun Preview() {
    QRScannerTheme {
        CameraRationale(
            onClickCloseApp = {},
            onClickGrantAccess = {}
        )
    }
}