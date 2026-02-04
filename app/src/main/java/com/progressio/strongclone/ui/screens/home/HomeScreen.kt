package com.progressio.strongclone.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.progressio.strongclone.data.local.entity.WorkoutTemplate
import com.progressio.strongclone.ui.navigation.Screen
import com.progressio.strongclone.ui.theme.OnSurface
import com.progressio.strongclone.ui.theme.OnSurfaceVariant
import com.progressio.strongclone.ui.theme.Primary
import com.progressio.strongclone.ui.theme.Surface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val templates by viewModel.templates.collectAsState(initial = emptyList())
    HomeScreenContent(
        templates = templates,
        onTemplateClick = { templateId ->
            navController.navigate(Screen.StartWorkout.createRoute(templateId))
        },
        onStartEmpty = {
            navController.navigate(Screen.StartWorkout.createRoute(0L))
        }
    )
}

@Composable
private fun HomeScreenContent(
    templates: List<WorkoutTemplate>,
    onTemplateClick: (Long) -> Unit,
    onStartEmpty: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workouts", color = OnSurface) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface,
                    titleContentColor = OnSurface
                ),
                actions = {
                    androidx.compose.material3.TextButton(
                        onClick = { navController.navigate(com.progressio.strongclone.ui.navigation.Screen.CreateTemplate.route) }
                    ) {
                        Text("New template", color = Primary)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onStartEmpty,
                containerColor = Primary,
                content = { Icon(Icons.Default.Add, contentDescription = "Start workout") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(templates) { template ->
                TemplateCard(
                    name = template.name,
                    description = template.description,
                    onClick = { onTemplateClick(template.id) }
                )
            }
        }
    }
}

@Composable
private fun TemplateCard(
    name: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = name, color = OnSurface, style = MaterialTheme.typography.titleLarge)
            if (description.isNotEmpty()) {
                Text(text = description, color = OnSurfaceVariant, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
