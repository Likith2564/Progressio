package com.progressio.strongclone.ui.screens.template

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
fun CreateTemplateScreen(
    navController: NavController,
    viewModel: CreateTemplateViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(state.saved) {
        if (state.saved) navController.navigateUp()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New template", color = OnSurface) },
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { viewModel.setName(it) },
                    label = { Text("Template name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = state.description,
                    onValueChange = { viewModel.setDescription(it) },
                    label = { Text("Description (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Text("Select exercises", color = OnSurface, style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
            }
            items(state.exercises) { ex ->
                val selected = ex.id in state.selectedIds
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { viewModel.toggleExercise(ex.id) },
                    colors = CardDefaults.cardColors(containerColor = if (selected) Primary.copy(alpha = 0.3f) else Surface)
                ) {
                    Text(
                        text = "${ex.name} · ${ex.muscleGroup}",
                        modifier = Modifier.padding(16.dp),
                        color = OnSurface
                    )
                }
            }
            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.save() },
                    enabled = state.name.isNotBlank() && state.selectedIds.isNotEmpty(),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("Save template")
                }
            }
        }
    }
}
