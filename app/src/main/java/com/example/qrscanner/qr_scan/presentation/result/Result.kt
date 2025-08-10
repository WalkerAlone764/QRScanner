package com.example.qrscanner.qr_scan.presentation.result

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.qrscanner.core.ui.theme.QRScannerTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ResultRoot(
    viewModel: ResultViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ResultScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ResultScreen(
    state: ResultState,
    onAction: (ResultAction) -> Unit,
) {

    state.bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = null
        )
    }

}

@Preview
@Composable
private fun Preview() {
    QRScannerTheme {
        ResultScreen(
            state = ResultState(),
            onAction = {}
        )
    }
}