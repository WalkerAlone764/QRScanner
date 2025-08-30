package com.example.qrscanner.createqr.presentation.wifiqr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
fun WifiQRCreationRoot(
    mainPaddingValues: PaddingValues,
    onNavigateBack: () -> Unit,
    onNavigateToPreviewScreen: (ssid: String, password: String, encryptionType: String) -> Unit,
    viewModel: WifiQRCreationViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.event) { event ->
        when (event) {
            WifiQRCreationEvent.NavigateBack -> onNavigateBack()
            is WifiQRCreationEvent.NavigateToPreviewScreen -> onNavigateToPreviewScreen(
                event.ssid,
                event.password,
                event.encryptedType
            )
        }
    }

    WifiQRCreationScreen(
        mainPaddingValues = mainPaddingValues,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun WifiQRCreationScreen(
    mainPaddingValues: PaddingValues,
    state: WifiQRCreationState,
    onAction: (WifiQRCreationAction) -> Unit,
) {
    Scaffold(
        topBar = {
            CreateAppBar(
                title = "Text",
                onClickBack = {
                    onAction(WifiQRCreationAction.OnClickBack)
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
                    text = state.ssid,
                    placeholder = "SSID",
                    onValueChange = {
                        onAction(WifiQRCreationAction.OnSSIDChanged(it))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                CustomTextField(
                    text = state.password,
                    placeholder = "Password",
                    onValueChange = {
                        onAction(WifiQRCreationAction.OnPasswordChanged(it))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    )
                )

                CustomTextField(
                    text = state.encryptedType,
                    placeholder = "Encryption Type",
                    onValueChange = {
                        onAction(WifiQRCreationAction.OnEncryptionChanged(it))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    )
                )

                CircularButton(
                    text = "Generate QR-Code",
                    onClick = {
                        onAction(WifiQRCreationAction.OnGenerateQR)
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
        WifiQRCreationScreen(
            mainPaddingValues = PaddingValues(),
            state = WifiQRCreationState(),
            onAction = {}
        )
    }
}