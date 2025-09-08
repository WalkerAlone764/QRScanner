package com.example.qrscanner.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.qrscanner.core.domain.BarcodeType

@Entity
data class QREntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val isScanned: Boolean,
    val type: BarcodeType,
    val title: String,
    val value: String,
    val createdAt: Long
)
