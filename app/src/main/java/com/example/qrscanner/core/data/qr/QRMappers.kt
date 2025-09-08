package com.example.qrscanner.core.data.qr

import com.example.qrscanner.core.database.entity.QREntity
import com.example.qrscanner.core.domain.qr.QR
import java.time.Instant

fun QR.toQrEntity(): QREntity {
    return QREntity(
        isScanned = isScanned,
        type = type,
        title = title,
        value = value,
        createdAt = createdAt.toEpochMilli(),
        id = id ?: 0
    )
}

fun QREntity.toQR(): QR {
    return QR(
        isScanned = isScanned,
        type = type,
        title = title,
        value = value,
        createdAt = Instant.ofEpochMilli(createdAt),
        id = id
    )
}