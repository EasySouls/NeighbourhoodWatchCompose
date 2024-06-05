package dev.htmlastic.neighbourhoodwatchcompose.core.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss, modifier = modifier
    ) {
        Surface(
            modifier = Modifier.wrapContentSize(),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Engedély szükséges",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = permissionTextProvider.getDescription(isPermanentlyDeclined)
                )
                HorizontalDivider()
                Button(onClick = {
                    if (isPermanentlyDeclined) {
                        onGoToAppSettingsClick()
                    } else {
                        onOkClick()
                    }
                }) {
                    Text(
                        text = if (isPermanentlyDeclined) {
                            "Beállítások"
                        } else {
                            "Elfogadás"
                        }
                    )
                }
            }
        }
    }
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class NotificationPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "Véglegesen letiltotadd az engedélyt az értesítésekhez. A beállításokban újra engedélyezheted a hozzáférést."
        } else {
            "Az appllikáció hozzáférést kér az értesítéseidhez, hogy üzeneteket küldhessen."
        }
    }
}

class LocationPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "Véglegesen letiltotadd az engedélyt a gps használatához. A beállításokban újra engedélyezheted a hozzáférést."
        } else {
            "Az appllikáció hozzáférést kér a gps-hez, hogy járőrözés közben figyelhesse a helyzetedet."
        }
    }
}