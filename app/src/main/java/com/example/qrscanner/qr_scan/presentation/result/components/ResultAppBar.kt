@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.qrscanner.qr_scan.presentation.result.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.qrscanner.R
import com.example.qrscanner.core.ui.theme.QRScannerTheme
import com.example.qrscanner.core.ui.theme.onOverlay

@Composable
fun ResultAppBar(
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit
) {

    CenterAlignedTopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        navigationIcon = {
            IconButton(
                onClick = onClickBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.onOverlay
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.scan_result),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onOverlay
                )
            )
        }
    )
    
}

@Preview
@Composable
private fun Preview() {
    QRScannerTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onSurface)
        ) {
            ResultAppBar(
                onClickBack = {}
            )
        }
    }
}