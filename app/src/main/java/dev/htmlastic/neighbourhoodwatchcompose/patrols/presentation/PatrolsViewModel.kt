package dev.htmlastic.neighbourhoodwatchcompose.patrols.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.Patrol
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.PatrolsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
class PatrolsViewModel @Inject constructor(
    private val repository: PatrolsRepository
): ViewModel() {

    private val _currentPatrol = MutableStateFlow<Patrol?>(null)
    val currentPatrol = _currentPatrol.asStateFlow()

    private val _ongoingPatrols = MutableStateFlow(emptyList<Patrol>())
    val ongoingPatrols = _ongoingPatrols.asStateFlow()

    // TODO: Get the user id
    private val userId = ObjectId()
    private val departmentId = ObjectId()

    init {
        viewModelScope.launch {
            repository.getOngoingParticipatedPatrol(userId).collect {
                _currentPatrol.value = it
            }

            repository.getOngoingPatrolsByDepartment(departmentId).collect {
                _ongoingPatrols.value = it
            }
        }
    }
}