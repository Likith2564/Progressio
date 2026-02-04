package com.progressio.strongclone.ui.screens.template

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progressio.strongclone.data.local.entity.Exercise
import com.progressio.strongclone.data.repository.ExerciseRepository
import com.progressio.strongclone.data.repository.WorkoutTemplateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreateTemplateState(
    val name: String = "",
    val description: String = "",
    val exercises: List<Exercise> = emptyList(),
    val selectedIds: Set<Long> = emptySet(),
    val saved: Boolean = false
)

@HiltViewModel
class CreateTemplateViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val templateRepository: WorkoutTemplateRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CreateTemplateState())
    val state: StateFlow<CreateTemplateState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            exerciseRepository.getAllExercises().collect { list ->
                _state.update { it.copy(exercises = list) }
            }
        }
    }

    fun setName(name: String) { _state.update { it.copy(name = name) } }
    fun setDescription(desc: String) { _state.update { it.copy(description = desc) } }
    fun toggleExercise(id: Long) {
        _state.update { s ->
            s.copy(selectedIds = if (id in s.selectedIds) s.selectedIds - id else s.selectedIds + id)
        }
    }

    fun save() {
        viewModelScope.launch {
            val s = _state.value
            if (s.name.isNotBlank() && s.selectedIds.isNotEmpty()) {
                templateRepository.insertTemplate(s.name, s.description, s.selectedIds.toList())
                _state.update { it.copy(saved = true) }
            }
        }
    }
}
