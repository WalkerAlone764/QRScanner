package com.example.qrscanner.app.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun NavHostController.currentRoute(): Flow<Routes> {

    return callbackFlow {
        val listener = NavController.OnDestinationChangedListener { controller, destination, _ ->
            val newRoutes = when {
                destination.hasRoute(Routes.Scan::class) -> Routes.Scan
                destination.hasRoute(Routes.Result::class) -> {
                    controller.currentBackStackEntry!!.toRoute<Routes.Result>()
                }

                destination.hasRoute(Routes.Create::class) -> Routes.Create
                else -> Routes.Scan
            }

            trySend(newRoutes)


        }

        addOnDestinationChangedListener(listener)
        awaitClose {

            removeOnDestinationChangedListener(listener)
            close()

        }
    }

}