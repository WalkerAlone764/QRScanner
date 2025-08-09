package com.example.qrscanner.core.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.Image
import android.util.Log
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

/**
 * Converts an android.media.Image to a Bitmap.
 *
 * Supports YUV_420_888 and JPEG formats.
 * For YUV_420_888, it converts the image to NV21, then to JPEG, and finally to Bitmap.
 *
 * Note: The caller of this function is responsible for closing the original Image
 * if it wasn't obtained from an ImageProxy (which handles closing automatically).
 * When using with ImageProxy, imageProxy.close() will close the underlying Image.
 */
fun Image.toBitmap(): Bitmap? {
    when (format) {
        ImageFormat.JPEG -> {
            val buffer = planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

        ImageFormat.YUV_420_888 -> {
            // Planar YUV_420_888 to NV21 conversion
            val yPlane = planes[0]
            val uPlane = planes[1]
            val vPlane = planes[2]

            val yBuffer: ByteBuffer = yPlane.buffer
            val uBuffer: ByteBuffer = uPlane.buffer
            val vBuffer: ByteBuffer = vPlane.buffer

            // Rewind buffers to ensure we read from the beginning
            yBuffer.rewind()
            uBuffer.rewind()
            vBuffer.rewind()

            val ySize = yBuffer.remaining()

            // Output NV21 byte array. Size is 1.5 * width * height
            val nv21 = ByteArray(width * height * 3 / 2)
            var nv21Offset = 0

            // Copy Y plane
            // Source (yBytes)
            val yBytes = ByteArray(ySize)
            yBuffer.get(yBytes)
            // Destination (nv21)
            val yRowStride = yPlane.rowStride
            if (yRowStride == width) { // If Y plane is not padded
                System.arraycopy(yBytes, 0, nv21, nv21Offset, ySize)
                nv21Offset += ySize
            } else { // If Y plane is padded, copy row by row
                for (y in 0 until height) {
                    System.arraycopy(yBytes, y * yRowStride, nv21, nv21Offset, width)
                    nv21Offset += width
                }
            }

            // Copy V and U planes (interleaved VU for NV21)
            // For YUV_420_888, U (planes[1]) and V (planes[2]) are planar.
            // NV21 expects V before U in the interleaved chroma plane.
            val chromaWidth = width / 2
            val chromaHeight = height / 2
            val vRowStride = vPlane.rowStride
            val uRowStride = uPlane.rowStride
            val vPixelStride =
                vPlane.pixelStride // Should be 1 for V in I420 format from YUV_420_888
            val uPixelStride =
                uPlane.pixelStride // Should be 1 for U in I420 format from YUV_420_888

            val vBytes = ByteArray(vBuffer.remaining())
            vBuffer.get(vBytes)
            val uBytes = ByteArray(uBuffer.remaining())
            uBuffer.get(uBytes)

            for (y in 0 until chromaHeight) {
                for (x in 0 until chromaWidth) {
                    val vIndex = y * vRowStride + x * vPixelStride
                    val uIndex = y * uRowStride + x * uPixelStride

                    // Check bounds to prevent issues
                    if (nv21Offset < nv21.size - 1 && vIndex < vBytes.size && uIndex < uBytes.size) {
                        nv21[nv21Offset++] = vBytes[vIndex] // V sample
                        nv21[nv21Offset++] = uBytes[uIndex] // U sample
                    } else {
                        // This might happen if strides/sizes are unexpected.
                        // Log.w("ImageToBitmap", "Skipping VU pixel due to boundary check fail at y=$y, x=$x.")
                    }
                }
            }

            // Create YuvImage from NV21 data
            val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
            val out = ByteArrayOutputStream()
            // Compress to JPEG
            yuvImage.compressToJpeg(
                Rect(0, 0, width, height),
                95,
                out
            ) // Adjust quality as needed (0-100)
            val imageBytes = out.toByteArray()
            // Decode JPEG to Bitmap
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        }

        else -> {
            Log.e(
                "ImageToBitmap",
                "Unsupported image format: $format. Only JPEG and YUV_420_888 are supported."
            )
            return null
        }
    }
}
