package com.progressio.strongclone.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.progressio.strongclone.ui.theme.OnSurface
import com.progressio.strongclone.ui.theme.Surface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", color = OnSurface) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface, titleContentColor = OnSurface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Strong Clone", color = OnSurface, style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
            Text(text = "Workout tracker · 100% offline", color = com.progressio.strongclone.ui.theme.OnSurfaceVariant)
        }
    }
}
