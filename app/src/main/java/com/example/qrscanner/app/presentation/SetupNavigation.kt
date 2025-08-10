package com.example.qrscanner.app.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
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
            QRScanRoot(
                onNavigateToResult = { type, value ->
                    navController.navigate(
                        Routes.Result(type, value)
                    )
                }
            )
        }

        composable<Routes.Result> {
            val args = it.toRoute<Routes.Result>()
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "result ${args.value}",
                    color = Color.White
                )
            }
        }
    }

}