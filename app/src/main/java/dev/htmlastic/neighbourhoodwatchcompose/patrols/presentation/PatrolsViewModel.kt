package dev.htmlastic.neighbourhoodwatchcompose.patrols.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.htmlastic.neighbourhoodwatchcompose.patrols.data.PatrolsRepository

@HiltViewModel
class PatrolsViewModel(
    private val repository: PatrolsRepository
): ViewModel() {
}