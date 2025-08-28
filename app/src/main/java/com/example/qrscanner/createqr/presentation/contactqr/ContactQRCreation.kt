package com.example.qrscanner.createqr.presentation.contactqr

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
fun ContactQRCreationRoot(
    mainPaddingValues: PaddingValues,
    onNavigateBack: () -> Unit,
    onNavigateToPreviewScreen: (
        name: String,
        email: String,
        phone: String
    ) -> Unit,
    viewModel: ContactQRCreationViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.event) { event ->
        when (event) {
            ContactQRCreationEvent.NavigateBack -> onNavigateBack()
            is ContactQRCreationEvent.NavigateToPreviewScreen -> onNavigateToPreviewScreen(
                event.name,
                event.email,
                event.phone
            )
        }
    }

    ContactQRCreationScreen(
        mainPaddingValues = mainPaddingValues,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ContactQRCreationScreen(
    mainPaddingValues: PaddingValues,
    state: ContactQRCreationState,
    onAction: (ContactQRCreationAction) -> Unit,
) {
    Scaffold(
        topBar = {
            CreateAppBar(
                title = "Contact",
                onClickBack = {
                    onAction(ContactQRCreationAction.OnClickBack)
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
                    text = state.name,
                    placeholder = "Name",
                    onValueChange = {
                        onAction(ContactQRCreationAction.OnChangeName(it))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    ),
                )

                CustomTextField(
                    text = state.email,
                    placeholder = "Email",
                    onValueChange = {
                        onAction(ContactQRCreationAction.OnChangeEmail(it))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email
                    ),
                )

                CustomTextField(
                    text = state.phone,
                    placeholder = "Phone Number",
                    onValueChange = {
                        onAction(ContactQRCreationAction.OnChangePhone(it))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Phone
                    ),
                )

                CircularButton(
                    text = "Generate QR-Code",
                    onClick = {
                        onAction(ContactQRCreationAction.OnGenerateQR)
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
        ContactQRCreationScreen(
            mainPaddingValues = PaddingValues(),
            state = ContactQRCreationState(),
            onAction = {}
        )
    }
}