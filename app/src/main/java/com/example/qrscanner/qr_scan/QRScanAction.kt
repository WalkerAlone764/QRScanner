package com.example.qrscanner.qr_scan

import android.content.Context
import androidx.lifecycle.LifecycleOwner

sealed interface QRScanAction {

    data class OnRequestPermission(val isGranted: Boolean) : QRScanAction
    data class OnBindCamera(val context: Context, val lifecycleOwner: LifecycleOwner) : QRScanAction
}