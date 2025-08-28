package com.example.qrscanner.createqr.presentation.phoneqr

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
fun PhoneQRCreationRoot(
    mainPaddingValues: PaddingValues,
    onNavigateBack: () -> Unit,
    onNavigateToPreviewScreen: (number: String) -> Unit,
    viewModel: PhoneQRCreationViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.event) { event ->
        when (event) {
            PhoneQRCreationEvent.NavigateBack -> onNavigateBack()
            is PhoneQRCreationEvent.NavigateToPreviewScreen -> onNavigateToPreviewScreen(event.number)
        }
    }

    PhoneQRCreationScreen(
        mainPaddingValues = mainPaddingValues,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun PhoneQRCreationScreen(
    mainPaddingValues: PaddingValues,
    state: PhoneQRCreationState,
    onAction: (PhoneQRCreationAction) -> Unit,
) {

    Scaffold(
        topBar = {
            CreateAppBar(
                title = "Phone Number",
                onClickBack = {
                    onAction(PhoneQRCreationAction.OnClickBack)
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
                    text = state.phone,
                    placeholder = "Phone Number",
                    onValueChange = {
                        onAction(PhoneQRCreationAction.OnChangePhone(it))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                    )
                )

                CircularButton(
                    text = "Generate QR-Code",
                    onClick = {
                        onAction(PhoneQRCreationAction.OnGenerateQR)
                    },
                    enabled = state.canGenerateOR,
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
        PhoneQRCreationScreen(
            mainPaddingValues = PaddingValues(),
            state = PhoneQRCreationState(),
            onAction = {}
        )
    }
}