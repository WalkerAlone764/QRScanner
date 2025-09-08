package com.example.qrscanner.core.data.qr

import androidx.compose.ui.util.fastMap
import com.example.qrscanner.core.database.QRDao
import com.example.qrscanner.core.domain.qr.QR
import com.example.qrscanner.core.domain.qr.QRDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomQRDataSource(
    private val dao: QRDao
): QRDataSource {
    override fun observeScannedQR(): Flow<List<QR>> {
        return dao
            .observeScannedQR()
            .map { qREntities ->
                qREntities.fastMap { it.toQR() }
            }
    }

    override fun observeGeneratedQR(): Flow<List<QR>> {
        return dao
            .observeGeneratedQR()
            .map { qREntities ->
                qREntities.fastMap { it.toQR() }
            }
    }

    override suspend fun insertQR(qr: QR): Long {
        return dao.upsertQR(qr.toQrEntity())
    }

    override suspend fun getQRById(id: Long): QR {
        return dao.getQRById(id).toQR()
    }
}