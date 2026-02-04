package com.progressio.strongclone.ui.screens.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import com.progressio.strongclone.ui.theme.Primary
import com.progressio.strongclone.ui.theme.Surface
import com.progressio.strongclone.ui.theme.TimerAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveWorkoutScreen(
    navController: NavController,
    workoutId: Long,
    viewModel: ActiveWorkoutViewModel = hiltViewModel()
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (state.restSecondsRemaining > 0) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = TimerAccent),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Rest: ${state.restSecondsRemaining}s",
                            modifier = Modifier.padding(24.dp),
                            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
            items(state.exercises) { ex ->
                ExerciseCard(
                    exerciseName = ex.exerciseName,
                    sets = ex.sets,
                    lastSets = ex.lastWorkoutSets,
                    onAddSet = { w, r -> viewModel.addSet(ex.exerciseId, w, r) },
                    onStartRest = { viewModel.startRestTimer(90) }
                )
            }
            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.finishWorkout("")
                        navController.navigate(com.progressio.strongclone.ui.navigation.Screen.History.route) {
                            popUpTo(com.progressio.strongclone.ui.navigation.Screen.Home.route) { inclusive = false }
                        }
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("Finish Workout")
                }
            }
        }
    }
}

@Composable
private fun ExerciseCard(
    exerciseName: String,
    sets: List<ExerciseSetUi>,
    lastSets: List<Pair<Double?, Int>>,
    onAddSet: (Double?, Int?) -> Unit,
    onStartRest: () -> Unit
) {
    val weightState = androidx.compose.runtime.remember(sets.size) { androidx.compose.runtime.mutableStateOf(lastSets.firstOrNull()?.first?.toString() ?: "") }
    val repsState = androidx.compose.runtime.remember(sets.size) { androidx.compose.runtime.mutableStateOf(lastSets.firstOrNull()?.second?.toString() ?: "") }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = exerciseName, color = OnSurface, style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
            sets.forEach { set ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Set ${set.setNumber}", color = OnSurface)
                    Text("${set.weight ?: "-"} kg × ${set.reps ?: "-"} reps", color = OnSurface)
                }
            }
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = weightState.value,
                    onValueChange = { weightState.value = it },
                    label = { Text("Weight") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = repsState.value,
                    onValueChange = { repsState.value = it },
                    label = { Text("Reps") },
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = {
                    onAddSet(weightState.value.toDoubleOrNull(), repsState.value.toIntOrNull())
                    onStartRest()
                }) { Text("+ Set") }
            }
        }
    }
}
