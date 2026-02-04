package com.progressio.strongclone.data.repository

import com.progressio.strongclone.data.local.dao.ExerciseDao
import com.progressio.strongclone.data.local.dao.WorkoutDao
import com.progressio.strongclone.data.local.entity.Workout
import com.progressio.strongclone.data.local.entity.WorkoutSet
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRepository @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val exerciseDao: ExerciseDao
) {
    fun getAllWorkouts(): Flow<List<Workout>> = workoutDao.getAllWorkouts()
    fun getRecentWorkouts(limit: Int = 50): Flow<List<Workout>> = workoutDao.getRecentWorkouts(limit)
    suspend fun getWorkoutById(id: Long) = workoutDao.getWorkoutById(id)
    suspend fun getSetsByWorkout(workoutId: Long) = workoutDao.getSetsByWorkout(workoutId)
    suspend fun getLastCompletedSetsForExercise(exerciseId: Long) = workoutDao.getLastCompletedSetsForExercise(exerciseId)
    suspend fun getExerciseName(id: Long) = exerciseDao.getExerciseById(id)?.name

    suspend fun startWorkout(name: String, templateId: Long?): Long {
        return workoutDao.insertWorkout(Workout(name = name, templateId = templateId))
    }

    suspend fun addSet(workoutId: Long, exerciseId: Long, setNumber: Int, weight: Double?, reps: Int?) {
        workoutDao.insertSet(
            WorkoutSet(
                workoutId = workoutId,
                exerciseId = exerciseId,
                setNumber = setNumber,
                weight = weight,
                reps = reps,
                isCompleted = true
            )
        )
    }

    suspend fun finishWorkout(workoutId: Long, notes: String = "") {
        val workout = workoutDao.getWorkoutById(workoutId) ?: return
        val sets = workoutDao.getSetsByWorkout(workoutId)
        val totalVolume = sets.filter { it.isCompleted }.sumOf { (it.weight ?: 0.0) * (it.reps ?: 0) }
        val duration = ((System.currentTimeMillis() - workout.startedAt) / 1000).toInt()
        workoutDao.finishWorkout(workoutId, System.currentTimeMillis(), duration, totalVolume, notes)
    }
}
