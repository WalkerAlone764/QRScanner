package com.example.qrscanner.createqr.presentation.create

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qrscanner.core.presentation.util.ObserveAsEvents
import com.example.qrscanner.core.ui.theme.QRScannerTheme
import com.example.qrscanner.createqr.presentation.components.CreateAppBar
import com.example.qrscanner.createqr.presentation.create.components.CreateItems
import com.example.qrscanner.createqr.presentation.create.model.QRType

@Composable
fun CreateRoot(
    viewModel: CreateViewModel = viewModel(),
    onNavigateToCreate: (QRType) -> Unit
) {

    val view = LocalView.current
    val activity = LocalActivity.current

    DisposableEffect(true) {
        val window = activity?.window
        window?.let {
            // For dark icons on a light status bar:
            WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = true
            WindowCompat.getInsetsController(it, view).isAppearanceLightNavigationBars = false
            // For light icons on a dark status bar:
            // WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = false
        }

        onDispose {
        }
    }

    ObserveAsEvents(viewModel.event) { event ->
        when (event) {
            is CreateEvent.NavigateToCreate -> {
                onNavigateToCreate(event.qrType)
            }
        }
    }


    CreateScreen(
        onAction = viewModel::onAction
    )
}

@Composable
fun CreateScreen(
    onAction: (CreateAction) -> Unit,
) {

    Scaffold(
        topBar = {
            CreateAppBar(
                title = "Create QR",
                onClickBack = {},
                shouldShowBack = false
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(innerPadding)
                .padding(top = 22.dp)
                .padding(horizontal = 18.dp)
        ) {

            CreateItems(
                onClickItem = {
                    onAction(CreateAction.OnClickCreate(it))
                },
            )

        }

    }

}

@Preview
@Composable
private fun Preview() {
    QRScannerTheme {
        CreateScreen(
            onAction = {}
        )
    }
}