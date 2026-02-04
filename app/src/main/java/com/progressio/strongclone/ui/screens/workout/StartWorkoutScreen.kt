package com.progressio.strongclone.ui.screens.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.progressio.strongclone.ui.theme.OnSurface
import com.progressio.strongclone.ui.theme.Primary
import com.progressio.strongclone.ui.theme.Surface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartWorkoutScreen(
    navController: NavController,
    templateId: Long,
    viewModel: StartWorkoutViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(templateId) { viewModel.loadTemplate(templateId) }

    LaunchedEffect(state.workoutId) {
        state.workoutId?.let { id ->
            if (id > 0L) navController.navigate(com.progressio.strongclone.ui.navigation.Screen.ActiveWorkout.createRoute(id)) {
                popUpTo(com.progressio.strongclone.ui.navigation.Screen.StartWorkout.createRoute(templateId)) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Start Workout", color = OnSurface) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface, titleContentColor = OnSurface),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (state.exerciseItems.isEmpty() && state.templateName != null && state.templateId != 0L) {
            Text("No exercises in template", modifier = Modifier.padding(padding).padding(16.dp), color = OnSurface)
            return@Scaffold
        }
        if (state.templateName == null && templateId != 0L) {
            Text("Loading...", modifier = Modifier.padding(padding).padding(16.dp), color = OnSurface)
            return@Scaffold
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.startWorkout() },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("Start Workout")
                }
            }
            items(state.exerciseItems) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Surface)
                ) {
                    Text(
                        text = item.exerciseName,
                        modifier = Modifier.padding(16.dp),
                        color = OnSurface
                    )
                }
            }
        }
    }
}
