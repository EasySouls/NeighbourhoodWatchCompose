package dev.htmlastic.neighbourhoodwatchcompose.patrols.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.htmlastic.neighbourhoodwatchcompose.core.data.models.CivilGuard
import dev.htmlastic.neighbourhoodwatchcompose.core.data.models.Event
import dev.htmlastic.neighbourhoodwatchcompose.core.presentation.CustomNavigationBar
import dev.htmlastic.neighbourhoodwatchcompose.core.presentation.LocationPermissionTextProvider
import dev.htmlastic.neighbourhoodwatchcompose.core.presentation.NotificationPermissionTextProvider
import dev.htmlastic.neighbourhoodwatchcompose.core.presentation.PermissionDialog
import dev.htmlastic.neighbourhoodwatchcompose.core.presentation.PermissionViewModel
import dev.htmlastic.neighbourhoodwatchcompose.events.presentation.components.UpcomingEvents
import dev.htmlastic.neighbourhoodwatchcompose.openAppSettings
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.Patrol
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.PatrolService
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.PatrolState
import dev.htmlastic.neighbourhoodwatchcompose.patrols.presentation.components.ActivePatrol
import dev.htmlastic.neighbourhoodwatchcompose.patrols.presentation.components.OngoingPatrols
import dev.htmlastic.neighbourhoodwatchcompose.ui.theme.NeighbourhoodWatchComposeTheme
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatrolsScreen(
    navController: NavController,
    currentPatrol: Patrol?,
    ongoingPatrols: List<Patrol>,
    upcomingEvents: List<Event>,
    modifier: Modifier = Modifier,
) {
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current.applicationContext

    val permissionViewModel = viewModel<PermissionViewModel>()
    val dialogQueue = permissionViewModel.visiblePermissionDialogQueue

    val permissionsResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissions.keys.forEach { permission ->
                permissionViewModel.onPermissionResult(
                    permission = permission,
                    isGranted = permissions[permission] == true
                )
            }
        }
    )

    dialogQueue
        .reversed()
        .forEach { permission ->
            val activity = context as Activity
            PermissionDialog(
                permissionTextProvider = when (permission) {
                    Manifest.permission.ACCESS_COARSE_LOCATION -> LocationPermissionTextProvider()
                    Manifest.permission.POST_NOTIFICATIONS -> NotificationPermissionTextProvider()
                    else -> return@forEach
                },
                isPermanentlyDeclined = !shouldShowRequestPermissionRationale(activity, permission),
                onDismiss = permissionViewModel::dismissDialog,
                onOkClick = {
                    permissionViewModel.dismissDialog()
                    permissionsResultLauncher.launch(
                        arrayOf(permission)
                    )
                },
                onGoToAppSettingsClick = { activity.openAppSettings() }
            )
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Patrols")
                },
                colors = TopAppBarDefaults.topAppBarColors(),
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
            if (currentPatrol != null) {
                ActivePatrol(
                    patrol = currentPatrol,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            UpcomingEvents(
                events = upcomingEvents,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Box(modifier = Modifier.padding(16.dp)) {
                OngoingPatrols(
                    ongoingPatrols = ongoingPatrols,
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
                            permissionsResultLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                        Manifest.permission.POST_NOTIFICATIONS
                                    else ""
                                )
                            )
                            // TODO: Only start service if permissions are granted
                            // TODO: Handle starting service in the ViewModel
                            Intent(context, PatrolService::class.java).also {
                                it.action = PatrolService.Actions.START.toString()
                                context.startService(it)
                            }
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
        PatrolsScreen(
            navController,
            currentPatrol = Patrol().apply {
                startedAt = RealmInstant.from(Clock.System.now().toEpochMilliseconds(), 1)
                patrolState = PatrolState()
                patrolCarLicensePlate = "142IEW"
                participants = realmListOf(
                    CivilGuard().apply {
                        name = "Kis Pista"
                        phoneNumber = "06301736282"
                    },
                )
            },
            ongoingPatrols = emptyList(),
            upcomingEvents = emptyList()
        )
    }
}