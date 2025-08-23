package com.example.qrscanner.qr_scan.presentation.result.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.qrscanner.R

@Composable
fun ColumnScope.ContactContent(
    name: String,
    email: String,
    phone: String
) {
    Text(
        text = stringResource(R.string.contact),
        style = MaterialTheme.typography.titleMedium.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
        text = name,
        style = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
    )
    Text(
        text = email,
        style = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
    )
    Text(
        text = phone,
        style = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
    )

}