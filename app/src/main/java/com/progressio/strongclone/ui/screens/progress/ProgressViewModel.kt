package com.progressio.strongclone.ui.screens.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progressio.strongclone.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class ProgressStats(val totalWorkouts: Int, val totalVolume: Double)

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    val stats: StateFlow<ProgressStats> = workoutRepository
        .getAllWorkouts()
        .map { workouts ->
            val finished = workouts.filter { it.finishedAt != null }
            ProgressStats(
                totalWorkouts = finished.size,
                totalVolume = finished.sumOf { it.totalVolume }
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProgressStats(0, 0.0))
}
