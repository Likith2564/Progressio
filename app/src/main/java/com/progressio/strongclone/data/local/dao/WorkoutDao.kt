package com.progressio.strongclone.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.progressio.strongclone.data.local.entity.Workout
import com.progressio.strongclone.data.local.entity.WorkoutSet
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workouts ORDER BY started_at DESC")
    fun getAllWorkouts(): Flow<List<Workout>>

    @Query("SELECT * FROM workouts WHERE id = :id")
    suspend fun getWorkoutById(id: Long): Workout?

    @Query("SELECT * FROM workouts ORDER BY started_at DESC LIMIT :limit")
    fun getRecentWorkouts(limit: Int = 50): Flow<List<Workout>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(workoutSet: WorkoutSet)

    @Query("UPDATE workouts SET finished_at = :finishedAt, duration_seconds = :durationSeconds, total_volume = :totalVolume, notes = :notes WHERE id = :workoutId")
    suspend fun finishWorkout(workoutId: Long, finishedAt: Long, durationSeconds: Int, totalVolume: Double, notes: String)

    @Query("SELECT * FROM workout_sets WHERE workout_id = :workoutId AND exercise_id = :exerciseId ORDER BY set_number")
    fun getSetsForExercise(workoutId: Long, exerciseId: Long): Flow<List<WorkoutSet>>

    @Query("SELECT * FROM workout_sets WHERE workout_id = :workoutId ORDER BY set_number")
    suspend fun getSetsByWorkout(workoutId: Long): List<WorkoutSet>

    @Query("SELECT * FROM workout_sets WHERE exercise_id = :exerciseId AND workout_id = (SELECT w.id FROM workouts w INNER JOIN workout_sets ws ON ws.workout_id = w.id WHERE ws.exercise_id = :exerciseId AND w.finished_at IS NOT NULL ORDER BY w.started_at DESC LIMIT 1) ORDER BY set_number")
    suspend fun getLastCompletedSetsForExercise(exerciseId: Long): List<WorkoutSet>
}
