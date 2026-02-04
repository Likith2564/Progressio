package com.progressio.strongclone.ui.screens.workout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progressio.strongclone.data.local.entity.WorkoutSet
import com.progressio.strongclone.data.repository.WorkoutRepository
import com.progressio.strongclone.data.repository.WorkoutTemplateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExerciseSetUi(
    val setId: Long,
    val setNumber: Int,
    val weight: Double?,
    val reps: Int?,
    val isCompleted: Boolean
)

data class ExerciseInWorkout(
    val exerciseId: Long,
    val exerciseName: String,
    val sets: List<ExerciseSetUi>,
    val lastWorkoutSets: List<Pair<Double?, Int>>
)

data class ActiveWorkoutState(
    val workoutId: Long = 0L,
    val workoutName: String = "",
    val exercises: List<ExerciseInWorkout> = emptyList(),
    val restSecondsRemaining: Int = 0,
    val isRestTimerRunning: Boolean = false,
    val totalVolume: Double = 0.0,
    val durationSeconds: Int = 0
)

@HiltViewModel
class ActiveWorkoutViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository,
    private val templateRepository: WorkoutTemplateRepository
) : ViewModel() {

    private val workoutId: Long = savedStateHandle.get<Long>("workoutId") ?: 0L

    private val _restTimer = MutableStateFlow(0)
    val restTimer: StateFlow<Int> = _restTimer.asStateFlow()

    private val _state = MutableStateFlow(ActiveWorkoutState(workoutId = workoutId))
    val state: StateFlow<ActiveWorkoutState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            loadWorkout()
        }
    }

    private suspend fun loadWorkout() {
        val workout = workoutRepository.getWorkoutById(workoutId) ?: return
        val sets = workoutRepository.getSetsByWorkout(workoutId)
        var exercises: List<ExerciseInWorkout>
        if (sets.isEmpty() && workout.templateId != null) {
            val (_, templateExercisesWithNames) = templateRepository.getTemplateWithExerciseNames(workout.templateId!!)
            exercises = templateExercisesWithNames.map { (te, name) ->
                val lastSets = workoutRepository.getLastCompletedSetsForExercise(te.exerciseId)
                    .map { it.weight to (it.reps ?: 0) }
                ExerciseInWorkout(
                    exerciseId = te.exerciseId,
                    exerciseName = name,
                    sets = emptyList(),
                    lastWorkoutSets = lastSets
                )
            }
        } else {
            val byExercise = sets.groupBy { it.exerciseId }
            exercises = byExercise.map { (exerciseId, setsForEx) ->
                val name = workoutRepository.getExerciseName(exerciseId) ?: "Exercise"
                val lastSets = workoutRepository.getLastCompletedSetsForExercise(exerciseId)
                    .map { it.weight to (it.reps ?: 0) }
                ExerciseInWorkout(
                    exerciseId = exerciseId,
                    exerciseName = name,
                    sets = setsForEx.sortedBy { it.setNumber }.map { s ->
                        ExerciseSetUi(s.id, s.setNumber, s.weight, s.reps, s.isCompleted)
                    },
                    lastWorkoutSets = lastSets
                )
            }
        }
        val startedAt = workout.startedAt
        _state.value = _state.value.copy(
            workoutName = workout.name,
            exercises = exercises.ifEmpty {
                listOf(ExerciseInWorkout(0L, "Add exercise", emptyList(), emptyList()))
            },
            durationSeconds = ((System.currentTimeMillis() - startedAt) / 1000).toInt()
        )
    }

    fun addSet(exerciseId: Long, weight: Double?, reps: Int?) {
        viewModelScope.launch {
            val sets = workoutRepository.getSetsByWorkout(workoutId).filter { it.exerciseId == exerciseId }
            val setNumber = (sets.maxOfOrNull { it.setNumber } ?: 0) + 1
            workoutRepository.addSet(workoutId, exerciseId, setNumber, weight, reps)
            loadWorkout()
        }
    }

    fun startRestTimer(seconds: Int) {
        _state.value = _state.value.copy(restSecondsRemaining = seconds, isRestTimerRunning = true)
        viewModelScope.launch {
            for (s in seconds downTo 0) {
                _state.value = _state.value.copy(restSecondsRemaining = s)
                kotlinx.coroutines.delay(1000)
            }
            _state.value = _state.value.copy(isRestTimerRunning = false)
        }
    }

    fun finishWorkout(notes: String) {
        viewModelScope.launch {
            workoutRepository.finishWorkout(workoutId, notes)
        }
    }
}
