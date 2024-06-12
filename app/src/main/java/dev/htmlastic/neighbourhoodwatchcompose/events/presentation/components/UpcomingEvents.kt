package dev.htmlastic.neighbourhoodwatchcompose.events.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.htmlastic.neighbourhoodwatchcompose.core.data.models.Event
import dev.htmlastic.neighbourhoodwatchcompose.core.domain.DateTimeUtils
import dev.htmlastic.neighbourhoodwatchcompose.ui.theme.NeighbourhoodWatchComposeTheme
import io.realm.kotlin.types.RealmInstant

@Composable
fun UpcomingEvents(
    events: List<Event>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(5)),
        tonalElevation = 5.dp,
        shape = RoundedCornerShape(5)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            Text(
                text = "Közelgő események",
                modifier = Modifier.padding(bottom = 4.dp),
                style = MaterialTheme.typography.titleLarge,
            )

            if (events.isEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                ) {
                    Text(text = "Nincsenek közelgő események")
                }
            } else {
                Column {
                    events.forEach { event ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = event.name)
                            Text(text = DateTimeUtils.toFormattedText(event.date.epochSeconds))
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun UpcomingEventsPreview() {
    NeighbourhoodWatchComposeTheme {
        UpcomingEvents(events = listOf(
            Event().apply {
                name = "Járőrözés"
                description = "Rutinmunka"
                date = RealmInstant.now()
            },
            Event().apply {
                name = "Óbuda napok"
                description = "Bemutató a Polgárőrségről"
                date = RealmInstant.now()
            }
        ))
    }
}