package com.example.qrscanner.qr_scan.presentation.scan.components

import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.SurfaceRequest
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.qrscanner.ui.theme.QRScannerTheme
import com.example.qrscanner.ui.theme.onOverlay

@Composable
fun ViewFinderComponent(
    surfaceRequest: SurfaceRequest, modifier: Modifier = Modifier
) {

    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Point your camera at a QR code",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onOverlay
        )
        Spacer(
            modifier = Modifier.height(42.dp)
        )

        Box {
            CameraXViewfinder(
                surfaceRequest = surfaceRequest,
                contentScale = ContentScale.FillBounds,
                modifier = modifier
                    .widthIn(
                        max = 600.dp
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .aspectRatio(1f)
            )
            CornerFrame(
                modifier = modifier
                    .widthIn(
                        max = 600.dp
                    )
                    .aspectRatio(1f)
            )
        }
    }
}

@Composable
fun CornerFrame(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 8.dp,
    strokeLength: Dp = 60.dp,
    cornerRadius: Dp = 16.dp,
    cornerColor: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(modifier = modifier) {

        val strokeLengthInPx = strokeLength.toPx()
        val cornerRadiusInPx = cornerRadius.toPx()
        val strokeWidthInPx = strokeWidth.toPx()
        val canvasWidth = size.width
        val canvasHeight = size.height

        //draw top left corner
        drawLine(
            color = cornerColor,
            start = Offset(0f, cornerRadiusInPx),
            end = Offset(0f, strokeLengthInPx),
            strokeWidth = strokeWidthInPx
        )

        drawLine(
            color = cornerColor,
            start = Offset(cornerRadiusInPx, 0f),
            end = Offset(strokeLengthInPx, 0f),
            strokeWidth = strokeWidthInPx
        )

        drawArc(
            color = cornerColor,
            startAngle = 180f,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = strokeWidthInPx),
            topLeft = Offset(0f, 0f),
            size = Size(cornerRadiusInPx * 2, cornerRadiusInPx * 2)
        )

        //draw top right corner
        drawLine(
            color = cornerColor,
            start = Offset(canvasWidth, cornerRadiusInPx),
            end = Offset(canvasWidth, strokeLengthInPx),
            strokeWidth = strokeWidthInPx
        )

        drawLine(
            color = cornerColor,
            start = Offset(canvasWidth - cornerRadiusInPx, 0f),
            end = Offset(canvasWidth - strokeLengthInPx, 0f),
            strokeWidth = strokeWidthInPx

        )

        drawArc(
            color = cornerColor,
            startAngle = 0f,
            sweepAngle = -90f,
            useCenter = false,
            topLeft = Offset(canvasWidth - cornerRadiusInPx * 2, 0f),
            style = Stroke(width = strokeWidthInPx),
            size = Size(cornerRadiusInPx * 2, cornerRadiusInPx * 2)
        )


        //draw bottom left corner
        drawLine(
            color = cornerColor,
            start = Offset(0f, canvasHeight - cornerRadiusInPx),
            end = Offset(0f, canvasHeight - strokeLengthInPx),
            strokeWidth = strokeWidthInPx
        )

        drawLine(
            color = cornerColor,
            start = Offset(cornerRadiusInPx, canvasHeight),
            end = Offset(strokeLengthInPx, canvasHeight),
            strokeWidth = strokeWidthInPx
        )

        drawArc(
            color = cornerColor,
            startAngle = 90f,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = strokeWidthInPx),
            topLeft = Offset(0f, canvasHeight - cornerRadiusInPx * 2),
            size = Size(cornerRadiusInPx * 2, cornerRadiusInPx * 2)
        )


        //draw bottom right corner
        drawLine(
            color = cornerColor,
            start = Offset(canvasWidth, canvasHeight - cornerRadiusInPx),
            end = Offset(canvasWidth, canvasHeight - strokeLengthInPx),
            strokeWidth = strokeWidthInPx
        )
        drawLine(
            color = cornerColor,
            start = Offset(canvasWidth - cornerRadiusInPx, canvasHeight),
            end = Offset(canvasWidth - strokeLengthInPx, canvasHeight),
            strokeWidth = strokeWidthInPx
        )

        drawArc(
            color = cornerColor,
            startAngle = 0f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(
                canvasWidth - cornerRadiusInPx * 2, canvasHeight - cornerRadiusInPx * 2
            ),
            style = Stroke(width = strokeWidthInPx),
            size = Size(cornerRadiusInPx * 2, cornerRadiusInPx * 2),
        )

    }
}


@Preview
@Composable
private fun CornerFramePreview() {
    QRScannerTheme {
        CornerFrame(
            modifier = Modifier
                .size(250.dp)
                .aspectRatio(1f)
        )
    }
}