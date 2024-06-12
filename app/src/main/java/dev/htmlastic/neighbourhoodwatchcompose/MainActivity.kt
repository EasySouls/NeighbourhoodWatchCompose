package dev.htmlastic.neighbourhoodwatchcompose

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.htmlastic.neighbourhoodwatchcompose.authentication.AuthenticationScreen
import dev.htmlastic.neighbourhoodwatchcompose.core.data.Constants.APP_ID
import dev.htmlastic.neighbourhoodwatchcompose.authentication.AuthenticationViewModel
import dev.htmlastic.neighbourhoodwatchcompose.patrols.presentation.PatrolsScreen
import dev.htmlastic.neighbourhoodwatchcompose.patrols.presentation.PatrolsViewModel
import dev.htmlastic.neighbourhoodwatchcompose.ui.theme.NeighbourhoodWatchComposeTheme
import io.realm.kotlin.mongodb.App

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            NeighbourhoodWatchComposeTheme {
                val navController = rememberNavController()


                NavHost(
                    navController = navController,
                    startDestination = Route.PatrolsScreen,
//                    startDestination = getStartDestination()
                ) {
                    composable<Route.PatrolsScreen> {
                        val viewModel: PatrolsViewModel = hiltViewModel()
                        val currentPatrol = viewModel.currentPatrol.collectAsStateWithLifecycle()
                        val ongoingPatrols = viewModel.ongoingPatrols.collectAsStateWithLifecycle()
                        val upcomingEvents = viewModel.upcomingEvents.collectAsStateWithLifecycle()

                        PatrolsScreen(
                            currentPatrol = currentPatrol.value,
                            ongoingPatrols = ongoingPatrols.value,
                            upcomingEvents = upcomingEvents.value,
                            navController = navController,
                        )
                    }

                    composable<Route.AuthScreen> {
                        val viewModel: AuthenticationViewModel = hiltViewModel()
                        val authenticated = viewModel.authenticated
                        
                        AuthenticationScreen(authenticated = authenticated.value)
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

fun getStartDestination(): Route {
    val user = App.create(APP_ID).currentUser
    return if (user != null && user.loggedIn) Route.PatrolsScreen
    else Route.AuthScreen
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}