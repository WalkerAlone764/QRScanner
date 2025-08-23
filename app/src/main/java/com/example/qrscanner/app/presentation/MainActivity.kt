package com.example.qrscanner.app.presentation

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.qrscanner.core.ui.theme.QRScannerTheme
import com.example.qrscanner.qr_scan.presentation.scan.QRScanRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                Color.WHITE
            ),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT) // Assuming you want to keep navigation bar style as is
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        setContent {
            QRScannerTheme {
                val navController = rememberNavController()
                SetupNavigation(navController = navController)

            }
        }
    }
}