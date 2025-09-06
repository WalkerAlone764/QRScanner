package com.example.qrscanner.core.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.qrscanner.core.database.entity.QREntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QRDao {
    @Query("select * from QREntity where isScanned = 1")
    fun observeScannedQR(): Flow<List<QREntity>>

    @Query("select * from QREntity where isScanned = 0")
    fun observeGeneratedQR(): Flow<List<QREntity>>

    @Upsert
    suspend fun upsertQR(qrEntity: QREntity): Long

    @Query("select * from QREntity where id = :id")
    suspend fun getQRById(id: Long): QREntity
}