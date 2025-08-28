package com.example.qrscanner.qr_scan.domain

import com.example.qrscanner.qr_scan.domain.model.QRAnalysisResult
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface QRAnalysis {
    val isLoading: SharedFlow<Boolean>

    val result: SharedFlow<QRAnalysisResult>


}