package com.example.qrscanner.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.qrscanner.R
import com.example.qrscanner.app.presentation.navigation.Routes
import com.example.qrscanner.core.ui.theme.QRScannerTheme
import com.example.qrscanner.core.ui.theme.linkBackground

@Composable
fun CustomNavBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val backgroundColor = MaterialTheme.colorScheme.linkBackground

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Row(
        modifier = modifier
            .navigationBarsPadding()
            .padding(
                bottom = 12.dp
            )
            .background(Color.White, RoundedCornerShape(50)),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        IconButton(
            onClick = {
                navController.navigate(Routes.History) {
                    popUpTo(Routes.History) {
                        inclusive = true
                    }
                }
            },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (
                    currentDestination?.hierarchy?.any {
                        it.hasRoute(Routes.History::class)
                    } == true
                ) {
                    MaterialTheme.colorScheme.linkBackground.copy(0.4f)
                } else {
                    Color.Transparent
                }
            )
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_clock_refresh),
                contentDescription = null
            )
        }

        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(50.dp)
                    .scale(1.4f)
                    .clip(RoundedCornerShape(50))
                    .background(backgroundColor)
                    .clickable {
                        navController.navigate(Routes.Scan) {
                            popUpTo(Routes.Scan) {
                                inclusive = true
                            }
                        }
                    }
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_scan),
                contentDescription = null,
                modifier = Modifier
                    .scale(1.2f)

            )


        }

        IconButton(
            onClick = {
                navController.navigate(Routes.Create) {
                    popUpTo(Routes.Create) {
                        inclusive = true
                    }
                }
            },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (currentDestination?.hierarchy?.any {
                        it.hasRoute(Routes.Create::class)
                    } == true) {
                    MaterialTheme.colorScheme.linkBackground.copy(0.4f)
                } else {
                    Color.Transparent
                }
            )
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_plus_circle),
                contentDescription = null
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    QRScannerTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .height(300.dp),
            verticalArrangement = Arrangement.Center
        ) {
            CustomNavBar(
                navController = rememberNavController(),
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(50))
            )
        }
    }
}