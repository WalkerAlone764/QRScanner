package com.example.qrscanner.core.domain.qr

import kotlinx.coroutines.flow.Flow

interface QRDataSource {
    fun observeScannedQR(): Flow<List<QR>>
    fun observeGeneratedQR(): Flow<List<QR>>
    suspend fun insertQR(qr: QR): Long
    suspend fun getQRById(id: Long): QR
}