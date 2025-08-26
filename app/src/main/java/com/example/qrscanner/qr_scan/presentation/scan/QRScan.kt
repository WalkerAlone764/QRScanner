@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.qrscanner.qr_scan.presentation.scan

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.compose.CameraXViewfinder
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.withStarted
import com.example.qrscanner.core.presentation.components.ContentWithBottomMessageBar
import com.example.qrscanner.core.presentation.util.ObserveAsEvents
import com.example.qrscanner.core.ui.theme.QRScannerTheme
import com.example.qrscanner.qr_scan.domain.model.BarcodeType
import com.example.qrscanner.qr_scan.presentation.scan.components.CameraRationale
import com.example.qrscanner.qr_scan.presentation.scan.components.ErrorMessageDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun QRScanRoot(
    onNavigateToResult: (type: BarcodeType, value: String) -> Unit,
    viewModel: QRScanViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isScanLoading by viewModel.isLoading.collectAsStateWithLifecycle()

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

            is QRScanEvent.OnSuccess -> {
                onNavigateToResult(event.type, event.value)
            }
        }
    }

    LaunchedEffect(Unit) {
        requestCameraPermission.launch(Manifest.permission.CAMERA)
    }

    QRScanScreen(
        isLoading = isScanLoading, state = state, onAction = viewModel::onAction
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QRScanScreen(
    isLoading: Boolean,
    state: QRScanState,
    onAction: (QRScanAction) -> Unit,
) {
    val textMeasurer = rememberTextMeasurer()
    val textToDraw = "Point your camera at a QR code"
    val textStyle = androidx.compose.ui.text.TextStyle(
        color = Color.White,
        fontSize = 18.sp, // You can adjust the font size
        textAlign = TextAlign.Center,

    )
    val measuredText = textMeasurer.measure(
        text = textToDraw,
        style = textStyle
    )
    Scaffold { innerPadding ->
        ContentWithBottomMessageBar(
            modifier = Modifier
                .fillMaxSize(),
            state = state.messageBarState
        ) {
            state.surfaceRequest?.let { surfaceRequest ->

                CameraXViewfinder(
                    surfaceRequest = surfaceRequest,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            val cornerColor = MaterialTheme.colorScheme.primary

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val size = this.size
                val width = 324.dp
                val height = 324.dp
                val radius = 16.dp
                val strokeWidth = 8.dp
                val strokeLength = 60.dp

                val strokeLengthInPx = strokeLength.toPx()
                val cornerRadiusInPx = radius.toPx()
                val strokeWidthInPx = strokeWidth.toPx()


                val scanSize = Size(
                    width = with(density) { width.toPx() },
                    height = with(density) { height.toPx() }
                )

                drawRect(
                    color = Color.Black.copy(0.5f),
                    topLeft = Offset.Zero,
                    size = size
                )

                val scanSizeOffset = Offset(
                    (size.width - scanSize.width) / 2,
                    (size.height - scanSize.height) / 2
                )

//                if (!isLoading) {
                    drawRoundRect(
                        color = Color.Transparent,
                        cornerRadius = CornerRadius(
                            x = with(density) { radius.toPx() },
                            y = with(density) { radius.toPx() }
                        ),
                        topLeft = scanSizeOffset,
                        size = scanSize,
                        blendMode = BlendMode.Clear
                    )

                    drawText(
                        textLayoutResult = measuredText,
                        topLeft = scanSizeOffset.copy(
                            y = scanSizeOffset.y - 65.dp.toPx(),
                            x = (size.width / 2f) - (measuredText.size.width / 2f)
                        )
                    )

                    //draw top left corner
                    drawLine(
                        color = cornerColor,
                        strokeWidth = strokeWidthInPx,
                        start = scanSizeOffset.copy(y = scanSizeOffset.y + cornerRadiusInPx),
                        end = scanSizeOffset.copy(y = scanSizeOffset.y + strokeLengthInPx)
                    )

                    drawLine(
                        color = cornerColor,
                        strokeWidth = strokeWidthInPx,
                        start = scanSizeOffset.copy(x = scanSizeOffset.x + cornerRadiusInPx),
                        end = scanSizeOffset.copy(x = scanSizeOffset.x + strokeLengthInPx)
                    )

                    drawArc(
                        color = cornerColor,
                        startAngle = 180f,
                        sweepAngle = 90f,
                        useCenter = false,
                        style = Stroke(width = strokeWidthInPx),
                        topLeft = scanSizeOffset,
                        size = Size(cornerRadiusInPx * 2, cornerRadiusInPx * 2)

                    )

                    //draw top right corner
                    drawLine(
                        color = cornerColor,
                        strokeWidth = strokeWidthInPx,
                        start = scanSizeOffset.copy(x = scanSizeOffset.x + scanSize.width - cornerRadiusInPx),
                        end = scanSizeOffset.copy(x = scanSizeOffset.x + scanSize.width - strokeLengthInPx)
                    )

                    drawLine(
                        color = cornerColor,
                        strokeWidth = strokeWidthInPx,
                        start = scanSizeOffset.copy(
                            scanSizeOffset.x + scanSize.width,
                            scanSizeOffset.y + cornerRadiusInPx
                        ),
                        end = scanSizeOffset.copy(
                            scanSizeOffset.x + scanSize.width,
                            scanSizeOffset.y + cornerRadiusInPx + strokeLengthInPx
                        )
                    )

                    drawArc(
                        color = cornerColor,
                        startAngle = 0f,
                        sweepAngle = -90f,
                        useCenter = false,
                        style = Stroke(width = strokeWidthInPx),
                        topLeft = scanSizeOffset.copy(
                            x = scanSizeOffset.x + scanSize.width - (cornerRadiusInPx * 2),
                            y = scanSizeOffset.y
                        ),
                        size = Size(cornerRadiusInPx * 2, cornerRadiusInPx * 2)

                    )


                    //draw bottom left corner
                    drawLine(
                        color = cornerColor,
                        strokeWidth = strokeWidthInPx,
                        start = scanSizeOffset.copy(
                            y = scanSizeOffset.y + scanSize.height - cornerRadiusInPx,
                        ),
                        end = scanSizeOffset.copy(
                            y = scanSizeOffset.y + scanSize.height - cornerRadiusInPx - strokeLengthInPx,
                        )
                    )

                    drawLine(
                        color = cornerColor,
                        strokeWidth = strokeWidthInPx,
                        start = scanSizeOffset.copy(
                            y = scanSizeOffset.y + scanSize.height,
                            x = scanSizeOffset.x + cornerRadiusInPx
                        ),
                        end = scanSizeOffset.copy(
                            y = scanSizeOffset.y + scanSize.height,
                            x = scanSizeOffset.x + cornerRadiusInPx + strokeLengthInPx
                        )
                    )
                    drawArc(
                        color = cornerColor,
                        startAngle = 90f,
                        sweepAngle = 90f,
                        useCenter = false,
                        style = Stroke(width = strokeWidthInPx),
                        topLeft = scanSizeOffset.copy(
                            y = scanSizeOffset.y + scanSize.height - (cornerRadiusInPx * 2),
                            x = scanSizeOffset.x
                        ),
                        size = Size(cornerRadiusInPx * 2, cornerRadiusInPx * 2)
                    )

                    // draw bottom right corner
                    drawLine(
                        color = cornerColor,
                        strokeWidth = strokeWidthInPx,
                        start = scanSizeOffset.copy(
                            x = scanSizeOffset.x + scanSize.width - cornerRadiusInPx,
                            y = scanSizeOffset.y + scanSize.height,
                        ),
                        end = scanSizeOffset.copy(
                            x = scanSizeOffset.x + scanSize.width - cornerRadiusInPx - strokeLengthInPx,
                            y = scanSizeOffset.y + scanSize.height,
                        )
                    )

                    drawLine(
                        color = cornerColor,
                        strokeWidth = strokeWidthInPx,
                        start = scanSizeOffset.copy(
                            x = scanSizeOffset.x + scanSize.width,
                            y = scanSizeOffset.y + scanSize.height - cornerRadiusInPx
                        ),
                        end = scanSizeOffset.copy(
                            x = scanSizeOffset.x + scanSize.width,
                            y = scanSizeOffset.y + scanSize.height - cornerRadiusInPx - strokeLengthInPx
                        )
                    )

                    drawArc(
                        color = cornerColor,
                        startAngle = 0f,
                        sweepAngle = 90f,
                        useCenter = false,
                        style = Stroke(width = strokeWidthInPx),
                        topLeft = scanSizeOffset.copy(
                            y = scanSizeOffset.y + scanSize.height - (cornerRadiusInPx * 2),
                            x = scanSizeOffset.x + scanSize.width - (cornerRadiusInPx * 2)
                        ),
                        size = Size(cornerRadiusInPx * 2, cornerRadiusInPx * 2)
                    )
                }
//            }

            if (state.showCameraRational) {
                CameraRationale(
                    onClickCloseApp = { onAction(QRScanAction.OnClickCloseApp) },
                    onClickGrantAccess = { onAction(QRScanAction.OnClickGrantAccess) }
                )
            }

            if (state.hasError) {
                ErrorMessageDialog(
                    onDismiss = { }
                )
            }
        }

        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut()
            ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator(
                        color = Color.White
                    )

                    Text(
                        text = "Loading...",
                        color = Color.White
                    )
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
           isLoading = false, state = QRScanState(), onAction = {})
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