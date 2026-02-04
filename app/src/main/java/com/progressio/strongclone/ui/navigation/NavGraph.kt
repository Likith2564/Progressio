package com.progressio.strongclone.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.progressio.strongclone.ui.screens.history.HistoryScreen
import com.progressio.strongclone.ui.screens.home.HomeScreen
import com.progressio.strongclone.ui.screens.progress.ProgressScreen
import com.progressio.strongclone.ui.screens.profile.ProfileScreen
import com.progressio.strongclone.ui.screens.workout.ActiveWorkoutScreen
import com.progressio.strongclone.ui.screens.workout.StartWorkoutScreen
import com.progressio.strongclone.ui.screens.workout.WorkoutDetailScreen
import com.progressio.strongclone.ui.screens.template.CreateTemplateScreen

@Composable
fun StrongCloneNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = currentDestination?.route in listOf(
        Screen.Home.route,
        Screen.History.route,
        Screen.Progress.route,
        Screen.Profile.route
    )

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) { HomeScreen(navController = navController) }
            composable(Screen.CreateTemplate.route) { CreateTemplateScreen(navController = navController) }
            composable(Screen.History.route) { HistoryScreen(navController = navController) }
            composable(Screen.Progress.route) { ProgressScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
            composable(
                route = Screen.StartWorkout.route,
                arguments = listOf(navArgument("templateId") { type = NavType.LongType; defaultValue = 0L })
            ) { backStackEntry ->
                val templateId = backStackEntry.arguments?.getLong("templateId") ?: 0L
                StartWorkoutScreen(navController = navController, templateId = templateId)
            }
            composable(
                route = Screen.ActiveWorkout.route,
                arguments = listOf(navArgument("workoutId") { type = NavType.LongType })
            ) { backStackEntry ->
                val workoutId = backStackEntry.arguments?.getLong("workoutId") ?: 0L
                ActiveWorkoutScreen(navController = navController, workoutId = workoutId)
            }
            composable(
                route = Screen.WorkoutDetail.route,
                arguments = listOf(navArgument("workoutId") { type = NavType.LongType })
            ) { backStackEntry ->
                val workoutId = backStackEntry.arguments?.getLong("workoutId") ?: 0L
                WorkoutDetailScreen(navController = navController, workoutId = workoutId)
            }
        }

        if (showBottomBar) {
            NavigationBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                containerColor = com.progressio.strongclone.ui.theme.Surface
            ) {
                val items = listOf(
                    BottomNavItem(Screen.Home.route, "Home", Icons.Default.Home),
                    BottomNavItem(Screen.History.route, "History", Icons.Default.DateRange),
                    BottomNavItem(Screen.Progress.route, "Progress", Icons.Default.TrendingUp),
                    BottomNavItem(Screen.Profile.route, "Profile", Icons.Default.Person)
                )
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    }
}

private data class BottomNavItem(val route: String, val title: String, val icon: ImageVector)
