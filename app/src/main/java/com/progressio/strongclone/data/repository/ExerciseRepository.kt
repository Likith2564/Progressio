package com.progressio.strongclone.data.repository

import com.progressio.strongclone.data.local.dao.ExerciseDao
import com.progressio.strongclone.data.local.entity.Exercise
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepository @Inject constructor(
    private val exerciseDao: ExerciseDao
) {
    fun getAllExercises(): Flow<List<Exercise>> = exerciseDao.getAllExercises()
    fun getExercisesByMuscleGroup(muscleGroup: String): Flow<List<Exercise>> = exerciseDao.getExercisesByMuscleGroup(muscleGroup)
    suspend fun getExerciseById(id: Long) = exerciseDao.getExerciseById(id)
    suspend fun insertExercise(exercise: Exercise) = exerciseDao.insert(exercise)
}
