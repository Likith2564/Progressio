package com.progressio.strongclone.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progressio.strongclone.data.local.entity.WorkoutTemplate
import com.progressio.strongclone.data.repository.WorkoutTemplateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val templateRepository: WorkoutTemplateRepository
) : ViewModel() {

    val templates: StateFlow<List<WorkoutTemplate>> = templateRepository
        .getAllTemplates()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
