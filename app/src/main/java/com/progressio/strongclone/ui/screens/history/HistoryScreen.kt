package com.progressio.strongclone.ui.screens.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.progressio.strongclone.data.local.entity.Workout
import com.progressio.strongclone.ui.navigation.Screen
import com.progressio.strongclone.ui.theme.OnSurface
import com.progressio.strongclone.ui.theme.OnSurfaceVariant
import com.progressio.strongclone.ui.theme.Surface
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val workouts by viewModel.workouts.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("History", color = OnSurface) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface, titleContentColor = OnSurface)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(workouts) { workout ->
                WorkoutHistoryCard(
                    workout = workout,
                    onClick = { navController.navigate(Screen.WorkoutDetail.createRoute(workout.id)) }
                )
            }
        }
    }
}

@Composable
private fun WorkoutHistoryCard(workout: Workout, onClick: () -> Unit) {
    val dateStr = SimpleDateFormat("MMM d, yyyy · HH:mm", Locale.getDefault()).format(Date(workout.startedAt))
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        androidx.compose.foundation.layout.Column(modifier = Modifier.padding(16.dp)) {
            Text(text = workout.name, color = OnSurface, style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
            Text(text = dateStr, color = OnSurfaceVariant, style = androidx.compose.material3.MaterialTheme.typography.bodyLarge)
            workout.durationSeconds?.let { Text(text = "Duration: ${it / 60} min", color = OnSurfaceVariant) }
            if (workout.totalVolume > 0) Text(text = "Volume: ${workout.totalVolume.toInt()} kg", color = OnSurfaceVariant)
        }
    }
}
