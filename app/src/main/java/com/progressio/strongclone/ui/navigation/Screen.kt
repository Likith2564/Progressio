package com.progressio.strongclone.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object History : Screen("history")
    data object Progress : Screen("progress")
    data object Profile : Screen("profile")
    data object ActiveWorkout : Screen("workout/{workoutId}") {
        fun createRoute(workoutId: Long) = "workout/$workoutId"
    }
    data object StartWorkout : Screen("start_workout/{templateId}") {
        fun createRoute(templateId: Long) = "start_workout/$templateId"
    }
    data object WorkoutDetail : Screen("workout_detail/{workoutId}") {
        fun createRoute(workoutId: Long) = "workout_detail/$workoutId"
    }
    data object ExerciseList : Screen("exercises")
    data object CreateTemplate : Screen("create_template")
}
