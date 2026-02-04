package com.progressio.strongclone.ui.screens.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progressio.strongclone.data.repository.WorkoutRepository
import com.progressio.strongclone.data.repository.WorkoutTemplateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

data class ExerciseItem(val templateExerciseId: Long, val exerciseId: Long, val exerciseName: String)

data class StartWorkoutState(
    val templateId: Long = 0L,
    val templateName: String? = null,
    val exerciseItems: List<ExerciseItem> = emptyList(),
    val workoutId: Long? = null
)

@HiltViewModel
class StartWorkoutViewModel @Inject constructor(
    private val templateRepository: WorkoutTemplateRepository,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StartWorkoutState())
    val state: StateFlow<StartWorkoutState> = _state.asStateFlow()

    fun loadTemplate(templateId: Long) {
        viewModelScope.launch {
            _state.value = _state.value.copy(templateId = templateId)
            if (templateId == 0L) {
                _state.value = _state.value.copy(templateName = "Quick workout", exerciseItems = emptyList())
                return@launch
            }
            val (template, exercisesWithNames) = templateRepository.getTemplateWithExerciseNames(templateId)
            _state.value = _state.value.copy(
                templateName = template?.name,
                exerciseItems = exercisesWithNames.map { (te, name) ->
                    ExerciseItem(te.id, te.exerciseId, name)
                }
            )
        }
    }

    fun startWorkout() {
        viewModelScope.launch {
            val name = _state.value.templateName ?: "Workout"
            val templateId = _state.value.templateId
            val id = workoutRepository.startWorkout(name, if (templateId == 0L) null else templateId)
            templateRepository.updateLastUsed(templateId)
            _state.value = _state.value.copy(workoutId = id)
        }
    }
}
