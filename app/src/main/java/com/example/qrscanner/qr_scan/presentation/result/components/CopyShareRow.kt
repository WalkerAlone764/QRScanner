package com.example.qrscanner.qr_scan.presentation.result.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qrscanner.R
import com.example.qrscanner.core.presentation.components.CircularButton
import com.example.qrscanner.core.ui.theme.QRScannerTheme

@Composable
fun CopyShareRow(
    modifier: Modifier = Modifier,
    onClickShare: () -> Unit,
    onClickCopy: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CircularButton(
            text = stringResource(R.string.share),
            leftIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_share),
                    contentDescription = null
                )
            },
            onClick = onClickShare,
            modifier = Modifier
                .weight(1f)
        )

        CircularButton(
            text = stringResource(R.string.copy),
            leftIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_copy),
                    contentDescription = null
                )
            },
            onClick = onClickCopy,
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    QRScannerTheme {
        CopyShareRow(
            onClickCopy = {},
            onClickShare = {},
        )
    }
}