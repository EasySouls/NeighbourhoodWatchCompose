package dev.htmlastic.neighbourhoodwatchcompose.patrols.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.htmlastic.neighbourhoodwatchcompose.core.data.models.CivilGuard
import dev.htmlastic.neighbourhoodwatchcompose.core.domain.DateTimeUtils
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.Patrol
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.PatrolType
import dev.htmlastic.neighbourhoodwatchcompose.ui.theme.NeighbourhoodWatchComposeTheme
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import kotlinx.datetime.Clock

@Composable
fun ActivePatrol(
    patrol: Patrol,
    modifier: Modifier = Modifier
) {
    val patrolStartedInText = DateTimeUtils.toFormattedText(patrol.startedAt.epochSeconds)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(6.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "Rendszám: ${patrol.patrolCarLicensePlate}"
            )
//            Text(
//                text = "Típus: ${patrol.patrolType.state}"
//            )
            Text(
                text = "Kezdve: $patrolStartedInText"
            )
        }
            for (participant in patrol.participants) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = participant.name
                    )
                    Text(
                        text = participant.phoneNumber
                    )
                }
                HorizontalDivider()
            }
        }
}

@Preview
@Composable
private fun ActivePatrolPreview() {
    NeighbourhoodWatchComposeTheme {
        val patrol = Patrol().apply {
            startedAt = RealmInstant.from(Clock.System.now().toEpochMilliseconds(), 1)
            patrolType = PatrolType.STARTED
            patrolCarLicensePlate = "142IEW"
            participants = realmListOf(
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
        ActivePatrol(patrol = patrol)
    }
}