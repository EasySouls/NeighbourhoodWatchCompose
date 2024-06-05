package dev.htmlastic.neighbourhoodwatchcompose.patrols.presentation

import android.content.Intent
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.htmlastic.neighbourhoodwatchcompose.core.data.CivilGuard
import dev.htmlastic.neighbourhoodwatchcompose.core.presentation.CustomNavigationBar
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.Patrol
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.PatrolService
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.PatrolType
import dev.htmlastic.neighbourhoodwatchcompose.patrols.presentation.widgets.ActivePatrol
import dev.htmlastic.neighbourhoodwatchcompose.ui.theme.NeighbourhoodWatchComposeTheme
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatrolsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    permissionResultLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>?
) {
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current.applicationContext

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Patrols")
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
            )
        },
        bottomBar = {
            CustomNavigationBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showBottomSheet = true
                }
            ) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Új szolgálat kezdése")
            }
        }
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

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = bottomSheetState
                ) {
                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                if (!bottomSheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        }
                    ) {
                        Text(text = "Elrejtés")
                    }
                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            permissionResultLauncher?.launch(arrayOf(
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                    android.Manifest.permission.POST_NOTIFICATIONS
                                else ""
                            ))
                            // TODO: Only start service if permissions are granted
                            // TODO: Handle starting service in the ViewModel
//                            Intent(context, PatrolService::class.java).also {
//                                it.action = PatrolService.Actions.START.toString()
//                                context.startService(it)
//                            }
                        }) {
                        Text(text = "Járőrözés kezdése")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    NeighbourhoodWatchComposeTheme {
        val navController = rememberNavController()
        PatrolsScreen(navController, permissionResultLauncher = null)
    }
}