package com.example.qrscanner.qr_scan.domain.model

sealed class QRAnalysisResult {
    data class Success(val data: String) : QRAnalysisResult()
    data class Error(val exception: Exception) : QRAnalysisResult()
}