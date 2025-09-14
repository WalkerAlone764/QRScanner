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
import com.example.qrscanner.core.domain.BarcodeType
import com.example.qrscanner.core.presentation.components.CustomNavBar
import com.example.qrscanner.createqr.presentation.contactqr.ContactQRCreationRoot
import com.example.qrscanner.createqr.presentation.create.CreateRoot
import com.example.qrscanner.createqr.presentation.geolocationqr.GeolocationQRCreationRoot
import com.example.qrscanner.createqr.presentation.linkqr.LinkQRCreationRoot
import com.example.qrscanner.createqr.presentation.phoneqr.PhoneQRCreationRoot
import com.example.qrscanner.createqr.presentation.textqr.TextQRCreationRoot
import com.example.qrscanner.createqr.presentation.wifiqr.WifiQRCreationRoot
import com.example.qrscanner.history.presentation.HistoryRoot
import com.example.qrscanner.qr_scan.data.model.BarcodeContactResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeGeolocationResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeLinkResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodePhoneResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeTextResultWrapper
import com.example.qrscanner.qr_scan.data.model.BarcodeWifiResultWrapper
import com.example.qrscanner.qr_scan.presentation.result.ResultRoot
import com.example.qrscanner.qr_scan.presentation.scan.QRScanRoot
import kotlinx.serialization.json.Json

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
                            Routes.Result(0L, false, type, value)
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

            composable<Routes.History> {
                HistoryRoot(
                    onNavigateToResult = {
                        navController.navigate(
                            Routes.Result(
                                it.id ?: 0L,
                                !it.isScanned,
                                it.type,
                                it.value

                            )
                        )
                    }
                )
            }

            composable<Routes.TextQR> {
                TextQRCreationRoot(
                    mainPaddingValues = innerPadding,
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    onNavigateToPreviewScreen = { text ->
                        navController.navigate(
                            Routes.Result(
                                0L,
                                true,
                                BarcodeType.TEXT,
                                Json.encodeToString(BarcodeTextResultWrapper(text))
                            )
                        )
                    }
                )
            }

            composable<Routes.LinkQR> {
                LinkQRCreationRoot(
                    mainPaddingValues = innerPadding,
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    onNavigateToPreviewScreen = {
                        navController.navigate(
                            Routes.Result(
                                0L,
                                true,
                                BarcodeType.LINK,
                                Json.encodeToString(
                                    BarcodeLinkResultWrapper(
                                        title = it,
                                        url = it
                                    )
                                )
                            )
                        )
                    }
                )
            }

            composable<Routes.ContactQR> {
                ContactQRCreationRoot(
                    mainPaddingValues = innerPadding,
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    onNavigateToPreviewScreen = { name, email, phone ->
                        navController.navigate(
                            Routes.Result(
                                0L,
                                true,
                                BarcodeType.CONTACT,
                                Json.encodeToString(
                                    BarcodeContactResultWrapper(
                                        formattedName = name,
                                        email = email,
                                        phone = phone
                                    )
                                )
                            )
                        )
                    }
                )
            }


            composable<Routes.PhoneNumberQR> {
                PhoneQRCreationRoot(
                    mainPaddingValues = innerPadding,
                    onNavigateBack = { navController.navigateUp() },
                    onNavigateToPreviewScreen = { number ->
                        navController.navigate(
                            Routes.Result(
                                isGenerated = true,
                                type = BarcodeType.PHONE_NUMBER,
                                value = Json.encodeToString(
                                    BarcodePhoneResultWrapper(
                                        phone = number
                                    )
                                )
                            )
                        )
                    }
                )
            }

            composable<Routes.GeoLocationQR> {
                GeolocationQRCreationRoot(
                    mainPaddingValues = innerPadding,
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    onNavigateToPreviewScreen = { lat, long ->
                        navController.navigate(
                            Routes.Result(
                                isGenerated = true,
                                type = BarcodeType.GEO_LOCATION,
                                value = Json.encodeToString(
                                    BarcodeGeolocationResultWrapper(
                                        lat = lat.toDoubleOrNull(),
                                        long = long.toDoubleOrNull()
                                    )
                                )
                            )
                        )
                    }
                )
            }

            composable<Routes.WifiQR> {
                WifiQRCreationRoot(
                    mainPaddingValues = innerPadding,
                    onNavigateBack = { navController.navigateUp() },
                    onNavigateToPreviewScreen = { ssid, password, encryptedType ->
                        navController.navigate(
                            Routes.Result(
                                isGenerated = true,
                                type = BarcodeType.WIFI,
                                value = Json.encodeToString(
                                    BarcodeWifiResultWrapper(
                                        ssid = ssid,
                                        password = password,
                                        encryption = encryptedType
                                    )
                                )
                            )
                        )
                    }
                )
            }
        }
    }

}