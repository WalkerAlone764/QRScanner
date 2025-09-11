package com.example.qrscanner.history.di

import com.example.qrscanner.history.presentation.HistoryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val historyModule = module {

    viewModelOf(::HistoryViewModel)
}