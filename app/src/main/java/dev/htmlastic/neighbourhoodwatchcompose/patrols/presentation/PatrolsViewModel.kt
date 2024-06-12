package dev.htmlastic.neighbourhoodwatchcompose.patrols.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.htmlastic.neighbourhoodwatchcompose.core.data.models.CivilGuard
import dev.htmlastic.neighbourhoodwatchcompose.core.data.models.Department
import dev.htmlastic.neighbourhoodwatchcompose.core.data.models.Event
import dev.htmlastic.neighbourhoodwatchcompose.core.domain.EventRepository
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.Patrol
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.PatrolsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatrolsViewModel @Inject constructor(
    private val patrolRepository: PatrolsRepository,
    private val eventRepository: EventRepository
): ViewModel() {

    private val _currentPatrol = MutableStateFlow<Patrol?>(null)
    val currentPatrol = _currentPatrol.asStateFlow()

    private val _ongoingPatrols = MutableStateFlow(emptyList<Patrol>())
    val ongoingPatrols = _ongoingPatrols.asStateFlow()

    private val _upcomingEvents = MutableStateFlow(emptyList<Event>())
    val upcomingEvents = _upcomingEvents.asStateFlow()

    // TODO: Get the current user as Civil Guard
    private val user = CivilGuard().apply {
        name = "Teszt Elek"
        department = Department().apply {
            name = "Teszt Department"
        }
        phoneNumber = "06703676834"
    }

    init {
        viewModelScope.launch {
            patrolRepository.getOngoingParticipatedPatrol(user).collect {
                _currentPatrol.value = it
            }

            user.department?._id?.let { id ->
                patrolRepository.getOngoingPatrolsByDepartment(id).collect { patrols ->
                    _ongoingPatrols.value = patrols
                }

                eventRepository.getUpcomingEventsByDepartment(id).collect { events ->
                    _upcomingEvents.value = events
                }
            }
        }
    }
}