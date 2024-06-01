package dev.htmlastic.neighbourhoodwatchcompose.patrols.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.htmlastic.neighbourhoodwatchcompose.core.data.CivilGuard
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.Patrol
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.PatrolType
import dev.htmlastic.neighbourhoodwatchcompose.patrols.presentation.widgets.ActivePatrol
import dev.htmlastic.neighbourhoodwatchcompose.ui.theme.NeighbourhoodWatchComposeTheme
import io.realm.kotlin.types.RealmInstant
import kotlinx.datetime.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatrolsScreen(navController: NavController, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Patrols")
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
            )
        },
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // If (activePatrol) {
            //     ActivePatrolScreen()
            // } else {
            //     OncomingEventsScreen()
            // }
            val patrol = Patrol().apply {
                startedAt = RealmInstant.from(Clock.System.now().toEpochMilliseconds(), 1)
                patrolType = PatrolType.STARTED
                patrolCarLicensePlate = "142IEW"
                participants = listOf(
                    CivilGuard().apply {
                        name = "Kis Pista"
                        phoneNumber = "06301736282"
                    },
                    CivilGuard().apply {
                        name = "Nagy József"
                        phoneNumber = "06302856296"
                    }
                )
            }
            Box(modifier = Modifier.padding(16.dp)) {
                ActivePatrol(
                    patrol = patrol,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    NeighbourhoodWatchComposeTheme {
        val navController = rememberNavController()
        PatrolsScreen(navController)
    }
}