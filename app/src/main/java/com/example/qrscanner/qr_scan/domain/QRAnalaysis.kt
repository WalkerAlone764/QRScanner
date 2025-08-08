package com.example.qrscanner.qr_scan.domain

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface QRAnalysis {
    val isLoading: StateFlow<Boolean>

    val result: SharedFlow<QRAnalysisResult>


}

sealed class QRAnalysisResult {
    data class Success(val data: String) : QRAnalysisResult()
    data class Error(val exception: Exception) : QRAnalysisResult()
}