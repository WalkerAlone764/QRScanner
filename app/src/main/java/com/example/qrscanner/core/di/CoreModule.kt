package com.example.qrscanner.core.di

import androidx.room.Room
import com.example.qrscanner.core.data.qr.RoomQRDataSource
import com.example.qrscanner.core.database.QRScannedDatabase
import com.example.qrscanner.core.domain.qr.QRDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {

    single {
        Room.databaseBuilder(
            get(),
            QRScannedDatabase::class.java,
            "QRDatabase"
        ).build()

    }

    single {
        get<QRScannedDatabase>().qrDao

    }

    singleOf(::RoomQRDataSource).bind(QRDataSource::class)
}