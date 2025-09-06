package com.example.qrscanner.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.qrscanner.core.database.entity.QREntity


@Database(
    entities = [QREntity::class],
    version = 1
)
abstract class QRScannedDatabase: RoomDatabase() {

    abstract val qrDao: QRDao
}