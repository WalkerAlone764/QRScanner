package com.example.qrscanner.qr_scan.presentation.result.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qrscanner.core.ui.theme.QRScannerTheme
import com.example.qrscanner.core.ui.theme.linkBackground

@Composable
fun ColumnScope.LinkContent(
    link: String,
    onClickLink: () -> Unit
) {

    var isFocus by remember {
        mutableStateOf(false)
    }

    var text by rememberSaveable {
        mutableStateOf("")
    }

    var textToShow by remember(isFocus, text) {
        mutableStateOf(
            if (isFocus || text.isNotEmpty()) text else "Link"
        )
    }

    BasicTextField(
        value = textToShow,
        onValueChange = {
            text = it
        },
        enabled = true,
        textStyle = MaterialTheme.typography.titleMedium.copy(
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        ),
        decorationBox = { innerTextField ->

            if (isFocus) {
                if (text.isEmpty()) {
                    Text(
                        text = "Link",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        ),
                        modifier = Modifier

                    )
                } else {
                    innerTextField()
                }
            } else {
                innerTextField()
            }
        },
        modifier = Modifier


    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
        text = link,
        style = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.linkBackground)
            .align(Alignment.CenterHorizontally)
            .animateContentSize()
            .clickable(
                onClick = onClickLink
            )
    )

}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    QRScannerTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
        ) {
            LinkContent(
                link = "https://google.com",
                onClickLink = {}
            )
        }

    }
}