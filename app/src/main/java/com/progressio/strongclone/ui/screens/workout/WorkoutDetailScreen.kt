package com.progressio.strongclone.ui.screens.workout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.progressio.strongclone.ui.theme.OnSurface
import com.progressio.strongclone.ui.theme.OnSurfaceVariant
import com.progressio.strongclone.ui.theme.Surface

@Composable
fun WorkoutDetailScreen(
    navController: NavController,
    workoutId: Long,
    viewModel: WorkoutDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.workoutName.ifEmpty { "Workout" }, color = OnSurface) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface, titleContentColor = OnSurface),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp)
        ) {
            state.summaryLines.forEach { line ->
                item { Text(text = line, color = OnSurfaceVariant, modifier = Modifier.padding(vertical = 4.dp)) }
            }
            items(state.setsByExercise) { (exerciseName, sets) ->
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Text(text = exerciseName, color = OnSurface, style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
                    sets.forEach { set ->
                        Text(text = "Set ${set.setNumber}: ${set.weight ?: "-"} kg x ${set.reps ?: "-"} reps", color = OnSurfaceVariant)
                    }
                }
            }
        }
    }
}
