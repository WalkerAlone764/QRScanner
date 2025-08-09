package com.example.qrscanner.app.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes {
    @Serializable
    data object Scan: Routes
    @Serializable
    data class Result(
        val data: String
    ): Routes
}