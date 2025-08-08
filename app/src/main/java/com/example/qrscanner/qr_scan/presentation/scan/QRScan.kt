package com.example.qrscanner.qr_scan.presentation.scan

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.lifecycle.withStarted
import com.example.qrscanner.core.presentation.components.ContentWithBottomMessageBar
import com.example.qrscanner.core.presentation.util.ObserveAsEvents
import com.example.qrscanner.core.ui.theme.QRScannerTheme
import com.example.qrscanner.qr_scan.presentation.scan.components.CameraRationale
import com.example.qrscanner.qr_scan.presentation.scan.components.ViewFinderComponent
import org.koin.androidx.compose.koinViewModel

@Composable
fun QRScanRoot(
    viewModel: QRScanViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val requestCameraPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            viewModel.onAction(QRScanAction.OnRequestPermission(isGranted))
        }

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val activity = LocalActivity.current


    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.withStarted {
            viewModel.onAction(QRScanAction.OnBindCamera(context, lifecycleOwner))
        }
    }

    ObserveAsEvents(viewModel.event) { event ->
        when (event) {
            QRScanEvent.GrantCameraAccess -> {
                val result = activity?.canRequestAgain() ?: false
                if (result) {
                    requestCameraPermission.launch(Manifest.permission.CAMERA)
                }
            }

            QRScanEvent.OnCloseApp -> {
                activity?.finish()
            }
        }
    }

    LaunchedEffect(Unit) {
        requestCameraPermission.launch(Manifest.permission.CAMERA)
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
        ContentWithBottomMessageBar(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            state = state.messageBarState
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

            if (state.showCameraRational) {
                CameraRationale(
                    onClickCloseApp = { onAction(QRScanAction.OnClickCloseApp) },
                    onClickGrantAccess = { onAction(QRScanAction.OnClickGrantAccess) }
                )
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


private fun Activity.canRequestAgain(): Boolean {
    val resultForPermissionCheck = this.checkSelfPermission(Manifest.permission.CAMERA)
    if (resultForPermissionCheck == PackageManager.PERMISSION_GRANTED) {
        return false
    }
    Log.d("checkCameraPermission", "checkCameraPermission: $")
    val result = this.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
    if (result) {
        return true
    } else {
        this.openAppSettings()
        return true
    }

}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}