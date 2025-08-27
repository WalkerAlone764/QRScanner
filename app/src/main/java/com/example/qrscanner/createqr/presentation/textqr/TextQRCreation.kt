package com.example.qrscanner.createqr.presentation.textqr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qrscanner.core.presentation.components.CircularButton
import com.example.qrscanner.core.presentation.components.CustomTextField
import com.example.qrscanner.core.presentation.util.ObserveAsEvents
import com.example.qrscanner.core.ui.theme.QRScannerTheme
import com.example.qrscanner.createqr.presentation.components.CreateAppBar
import com.example.qrscanner.createqr.presentation.components.CreateBasicLayout

@Composable
fun TextQRCreationRoot(
    mainPaddingValues: PaddingValues,
    onNavigateBack: () -> Unit,
    onNavigateToPreviewScreen: (text: String) -> Unit,
    viewModel: TextQRCreationViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.event) { event ->
        when (event) {
            TextQRCreationEvent.NavigateBack -> onNavigateBack()
            is TextQRCreationEvent.NavigateToPreviewScreen -> onNavigateToPreviewScreen(event.textContent)
        }
    }

    TextQRCreationScreen(
        mainPaddingValues = mainPaddingValues,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun TextQRCreationScreen(
    mainPaddingValues: PaddingValues,
    state: TextQRCreationState,
    onAction: (TextQRCreationAction) -> Unit,
) {
    Scaffold(
        topBar = {
            CreateAppBar(
                title = "Text",
                onClickBack = {
                    onAction(TextQRCreationAction.OnClickBack)
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(innerPadding)
                .padding(
                    bottom = mainPaddingValues.calculateBottomPadding()
                )
                .verticalScroll(rememberScrollState())
                .padding(top = 22.dp)
                .padding(horizontal = 18.dp)
        ) {

            CreateBasicLayout {

                CustomTextField(
                    text = state.textContent,
                    placeholder = "Text",
                    onValueChange = {
                        onAction(TextQRCreationAction.OnChangeText(it))
                    },
                )

                CircularButton(
                    text = "Generate QR-Code",
                    onClick = {
                        onAction(TextQRCreationAction.OnGenerateQR)
                    },
                    enabled = state.canGenerateOr,
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(22.dp)
                )

            }

        }

    }
}

@Preview
@Composable
private fun Preview() {
    QRScannerTheme {
        TextQRCreationScreen(
            mainPaddingValues = PaddingValues(),
            state = TextQRCreationState(),
            onAction = {}
        )
    }
}