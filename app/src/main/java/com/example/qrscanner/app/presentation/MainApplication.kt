package com.example.qrscanner.app.presentation

import android.app.Application
import com.example.qrscanner.core.di.coreModule
import com.example.qrscanner.qr_scan.di.scanModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(scanModule, coreModule)
        }
    }
}