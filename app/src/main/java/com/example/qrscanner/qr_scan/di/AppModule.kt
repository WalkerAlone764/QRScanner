package com.example.qrscanner.qr_scan.di

import com.example.qrscanner.qr_scan.data.AndroidQRAnalysis
import com.example.qrscanner.qr_scan.domain.QRAnalysis
import com.example.qrscanner.qr_scan.presentation.result.ResultViewModel
import com.example.qrscanner.qr_scan.presentation.scan.QRScanViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    single<QRAnalysis> {
        AndroidQRAnalysis(
            context = get()
        )
    }

    viewModelOf(::QRScanViewModel)
    viewModelOf(::ResultViewModel)
}