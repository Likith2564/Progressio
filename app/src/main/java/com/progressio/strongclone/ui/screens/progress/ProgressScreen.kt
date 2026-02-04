package com.progressio.strongclone.ui.screens.progress

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.progressio.strongclone.ui.theme.OnSurface
import com.progressio.strongclone.ui.theme.OnSurfaceVariant
import com.progressio.strongclone.ui.theme.Surface

@Composable
fun ProgressScreen(viewModel: ProgressViewModel = hiltViewModel()) {
    val stats by viewModel.stats.collectAsState(initial = ProgressStats(0, 0.0))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Progress", color = OnSurface) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface, titleContentColor = OnSurface)
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text(text = "Workouts completed: ${stats.totalWorkouts}", color = OnSurface, style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
            Text(text = "Total volume: ${stats.totalVolume.toInt()} kg", color = OnSurfaceVariant)
        }
    }
}
