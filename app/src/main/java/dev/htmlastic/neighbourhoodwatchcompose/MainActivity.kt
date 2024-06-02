package dev.htmlastic.neighbourhoodwatchcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
