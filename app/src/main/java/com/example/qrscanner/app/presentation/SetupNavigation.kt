package com.example.qrscanner.app.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.qrscanner.app.presentation.navigation.Routes
import com.example.qrscanner.app.presentation.navigation.currentRoute
import com.example.qrscanner.qr_scan.presentation.components.CustomNavBar
import com.example.qrscanner.qr_scan.presentation.result.ResultRoot
import com.example.qrscanner.qr_scan.presentation.scan.QRScanRoot

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SetupNavigation(
    navController: NavHostController
) {
    val route by navController.currentRoute().collectAsStateWithLifecycle(Routes.Home)

    Scaffold(
        bottomBar = {
            if (route == Routes.Scan || route == Routes.Create) {
                CustomNavBar(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }) {
        NavHost(
            navController = navController, startDestination = Routes.Scan
        ) {
            composable<Routes.Scan> {
                QRScanRoot(
                    onNavigateToResult = { type, value ->
                        navController.navigate(
                            Routes.Result(type, value)
                        )
                    })
            }

            composable<Routes.Result> {
                it.toRoute<Routes.Result>()
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ResultRoot(
                        onClickBack = {
                            navController.navigateUp()
                        })
                }
            }

            composable<Routes.Create> {

            }

            composable<Routes.Reset> {

            }
        }
    }

}