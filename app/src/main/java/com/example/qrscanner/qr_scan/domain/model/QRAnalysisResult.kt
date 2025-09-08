package com.example.qrscanner.qr_scan.domain.model

import com.example.qrscanner.core.domain.BarcodeType

sealed class QRAnalysisResult {
    data class Success(val type: BarcodeType, val value: String) : QRAnalysisResult()
    data class Error(val exception: Exception) : QRAnalysisResult()
}