package com.example.qrscanner.createqr.presentation.geolocationqr

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
fun GeolocationQRCreationRoot(
    mainPaddingValues: PaddingValues,
    onNavigateBack: () -> Unit,
    onNavigateToPreviewScreen: (lat: String, long: String) -> Unit,
    viewModel: GeolocationQRCreationViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.event) { event ->
        when (event) {
            GeolocationQRCreationEvent.NavigateBack -> onNavigateBack()
            is GeolocationQRCreationEvent.NavigateToPreviewScreen -> onNavigateToPreviewScreen(
                event.latitude,
                event.longitude
            )
        }
    }

    GeolocationQRCreationScreen(
        mainPaddingValues = mainPaddingValues,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun GeolocationQRCreationScreen(
    mainPaddingValues: PaddingValues,
    state: GeolocationQRCreationState,
    onAction: (GeolocationQRCreationAction) -> Unit,
) {

    Scaffold(
        topBar = {
            CreateAppBar(
                title = "Geolocation",
                onClickBack = {
                    onAction(GeolocationQRCreationAction.OnClickBack)
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
                    text = state.latitude,
                    placeholder = "Latitude",
                    onValueChange = {
                        onAction(GeolocationQRCreationAction.OnChangeLatitude(it))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                CustomTextField(
                    text = state.longitude,
                    placeholder = "Longitude",
                    onValueChange = {
                        onAction(GeolocationQRCreationAction.OnChangeLongitude(it))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )

                CircularButton(
                    text = "Generate QR-Code",
                    onClick = {
                        onAction(GeolocationQRCreationAction.OnGenerateQR)
                    },
                    enabled = state.canGenerateQR,
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
        GeolocationQRCreationScreen(
            mainPaddingValues = PaddingValues(),
            state = GeolocationQRCreationState(),
            onAction = {}
        )
    }
}