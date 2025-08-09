package com.example.qrscanner.app.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.qrscanner.qr_scan.presentation.scan.QRScanRoot

@Composable
fun SetupNavigation(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = Routes.Scan
    ) {
        composable<Routes.Scan> {
            QRScanRoot()
        }

        composable<Routes.Result> {
            Text(
                text = "result"
            )
        }
    }

}