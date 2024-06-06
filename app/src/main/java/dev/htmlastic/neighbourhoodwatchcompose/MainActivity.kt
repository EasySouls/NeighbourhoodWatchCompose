package dev.htmlastic.neighbourhoodwatchcompose

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.htmlastic.neighbourhoodwatchcompose.core.presentation.LocationPermissionTextProvider
import dev.htmlastic.neighbourhoodwatchcompose.core.presentation.PermissionViewModel
import dev.htmlastic.neighbourhoodwatchcompose.core.presentation.NotificationPermissionTextProvider
import dev.htmlastic.neighbourhoodwatchcompose.core.presentation.PermissionDialog
import dev.htmlastic.neighbourhoodwatchcompose.patrols.presentation.PatrolsScreen
import dev.htmlastic.neighbourhoodwatchcompose.ui.theme.NeighbourhoodWatchComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            NeighbourhoodWatchComposeTheme {
                val navController = rememberNavController()


                NavHost(
                    navController = navController,
                    startDestination = Route.PatrolsScreen
                ) {
                    composable<Route.PatrolsScreen> {
                        PatrolsScreen(
                            navController = navController,
                        )
                    }

                    composable<Route.MessagesScreen> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Messages")
                        }
                    }

                    composable<Route.CalendarScreen> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Calendar")
                        }
                    }
                }
            }
        }
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}