package com.progressio.strongclone.ui.screens.workout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progressio.strongclone.data.local.entity.WorkoutSet
import com.progressio.strongclone.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WorkoutDetailState(
    val workoutName: String = "",
    val summaryLines: List<String> = emptyList(),
    val setsByExercise: List<Pair<String, List<WorkoutSet>>> = emptyList()
)

@HiltViewModel
class WorkoutDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val workoutId: Long = savedStateHandle.get<Long>("workoutId") ?: 0L
    private val _state = MutableStateFlow(WorkoutDetailState())
    val state: StateFlow<WorkoutDetailState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val workout = workoutRepository.getWorkoutById(workoutId) ?: return@launch
            val sets = workoutRepository.getSetsByWorkout(workoutId)
            val byExercise = sets.groupBy { it.exerciseId }
            val pairs = byExercise.map { (exId, setsForEx) ->
                (workoutRepository.getExerciseName(exId) ?: "Exercise") to setsForEx.sortedBy { it.setNumber }
            }
            _state.value = WorkoutDetailState(
                workoutName = workout.name,
                summaryLines = listOf(
                    "Duration: ${(workout.durationSeconds ?: 0) / 60} min",
                    "Total volume: ${workout.totalVolume.toInt()} kg"
                ),
                setsByExercise = pairs
            )
        }
    }
}
