package com.example.qrscanner.qr_scan.presentation.result.components

import androidx.compose.foundation.focusable
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.qrscanner.R

@Composable
fun ColumnScope.ContactContent(
    name: String,
    email: String,
    phone: String
) {
    val context = LocalContext.current
    var isFocus by remember {
        mutableStateOf(false)
    }

    var text by rememberSaveable {
        mutableStateOf("")
    }

    var textToShow by remember(isFocus, text) {
        mutableStateOf(
            if (isFocus || text.isNotEmpty()) text else context.getString(R.string.contact)
        )
    }

    BasicTextField(
        value = textToShow,
        onValueChange = {
            text = it
        },
        readOnly = false,
        enabled = true,
        textStyle = MaterialTheme.typography.titleMedium.copy(
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        ),
        decorationBox = { innerTextField ->

            if (isFocus) {
                if (text.isEmpty()) {
                    Text(
                        text = stringResource(R.string.contact),
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
            .focusable(true)
            .onFocusChanged {
                isFocus = it.isFocused
            }


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