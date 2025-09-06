package com.example.qrscanner.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.qrscanner.core.domain.QRType

@Entity
data class QREntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val isScanned: Boolean,
    val type: QRType,
    val title: String,
    val value: String
)
