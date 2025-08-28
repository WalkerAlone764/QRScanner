package com.example.qrscanner.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qrscanner.core.ui.theme.QRScannerTheme

@Composable
fun CustomTextField(
    text: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    maxLine: Int = Int.MAX_VALUE,
    singleLine: Boolean = false
) {

    var isFocus by remember {
        mutableStateOf(false)
    }
    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        maxLines = maxLine,
        singleLine = singleLine,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = androidx.compose.ui.Alignment.CenterStart
            ) {
                if (text.isEmpty() && !isFocus){
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),

                        modifier = Modifier
                            .fillMaxWidth()
                    )
                } else {
                    innerTextField()
                }
            }
        },
        modifier = modifier
            .onFocusChanged {
                isFocus = it.isFocused
            }
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .heightIn(min = 50.dp)
            .padding(
                horizontal = 12.dp,
                vertical = 10.dp
            )
    )
}

@Preview
@Composable
private fun Preview() {
    QRScannerTheme {
        CustomTextField(
            text = "",
            placeholder = "Placeholder",
            onValueChange = {},
        )
    }
}