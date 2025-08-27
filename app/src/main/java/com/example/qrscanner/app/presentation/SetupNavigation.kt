package com.example.qrscanner.app.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.qrscanner.createqr.presentation.create.CreateRoot
import com.example.qrscanner.core.presentation.components.CustomNavBar
import com.example.qrscanner.createqr.presentation.textqr.TextQRCreationRoot
import com.example.qrscanner.qr_scan.domain.model.BarcodeType
import com.example.qrscanner.qr_scan.presentation.result.ResultRoot
import com.example.qrscanner.qr_scan.presentation.scan.QRScanRoot

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SetupNavigation(
    navController: NavHostController
) {
    val route by navController.currentRoute().collectAsStateWithLifecycle(Routes.Scan)

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
        }) { innerPadding ->
        NavHost(
            navController = navController, startDestination = Routes.Scan,
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
                CreateRoot(
                    onNavigateToCreate = {
                        navController.navigate(it.routes)
                    }
                )
            }

            composable<Routes.Reset> {

            }

            composable<Routes.TextQR> {
                TextQRCreationRoot(
                    mainPaddingValues = innerPadding,
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    onNavigateToPreviewScreen = { text ->
                    }
                )
            }

            composable<Routes.LinkQR> {  }

            composable<Routes.ContactQR> {  }

            composable<Routes.ContactQR> {  }

            composable<Routes.PhoneNumberQR> {  }

            composable<Routes.GeoLocationQR> {  }

            composable<Routes.WifiQR> {  }
        }
    }

}