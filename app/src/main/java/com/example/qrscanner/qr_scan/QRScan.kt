package com.example.qrscanner.qr_scan

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.withStarted
import com.example.qrscanner.qr_scan.components.ViewFinderComponent
import com.example.qrscanner.ui.theme.QRScannerTheme

@Composable
fun QRScanRoot(
    viewModel: QRScanViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val requestCameraPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            viewModel.onAction(QRScanAction.OnRequestPermission(isGranted))
        }

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.withStarted {
            viewModel.onAction(QRScanAction.OnBindCamera(context, lifecycleOwner))
        }
    }

    LaunchedEffect(Unit) {
        requestCameraPermission.launch(android.Manifest.permission.CAMERA)
    }

    QRScanScreen(
        state = state, onAction = viewModel::onAction
    )
}

@Composable
fun QRScanScreen(
    state: QRScanState,
    onAction: (QRScanAction) -> Unit,
) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xff000000).copy(0.5f))
                    .padding(innerPadding)
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                state.surfaceRequest?.let { surfaceRequest ->
                    ViewFinderComponent(surfaceRequest)
                }
            }
        }


    }
}

@Preview
@Composable
private fun Preview() {
    QRScannerTheme {
        QRScanScreen(
            state = QRScanState(), onAction = {})
    }
}