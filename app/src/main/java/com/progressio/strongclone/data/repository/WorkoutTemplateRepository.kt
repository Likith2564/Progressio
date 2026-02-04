package com.progressio.strongclone.data.repository

import com.progressio.strongclone.data.local.dao.ExerciseDao
import com.progressio.strongclone.data.local.dao.WorkoutTemplateDao
import com.progressio.strongclone.data.local.entity.TemplateExercise
import com.progressio.strongclone.data.local.entity.WorkoutTemplate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutTemplateRepository @Inject constructor(
    private val templateDao: WorkoutTemplateDao,
    private val exerciseDao: ExerciseDao
) {
    fun getAllTemplates(): Flow<List<WorkoutTemplate>> = templateDao.getAllTemplates()

    suspend fun getTemplateById(id: Long) = templateDao.getTemplateById(id)
    suspend fun getTemplateExercises(templateId: Long) = templateDao.getTemplateExercises(templateId)

    suspend fun getTemplateWithExerciseNames(templateId: Long): Pair<WorkoutTemplate?, List<Pair<TemplateExercise, String>>> {
        val template = templateDao.getTemplateById(templateId) ?: return null to emptyList()
        val templateExercises = templateDao.getTemplateExercises(templateId)
        val names = templateExercises.map { ex -> ex to (exerciseDao.getExerciseById(ex.exerciseId)?.name ?: "") }
        return template to names
    }

    suspend fun insertTemplate(name: String, description: String, exerciseIds: List<Long>): Long {
        val id = templateDao.insertTemplate(WorkoutTemplate(name = name, description = description))
        exerciseIds.forEachIndexed { index, exerciseId ->
            templateDao.insertTemplateExercise(
                TemplateExercise(templateId = id, exerciseId = exerciseId, orderIndex = index)
            )
        }
        return id
    }

    suspend fun updateLastUsed(templateId: Long) {
        templateDao.updateLastUsed(templateId, System.currentTimeMillis())
    }

    suspend fun deleteTemplate(id: Long) {
        templateDao.deleteTemplateExercises(id)
        templateDao.deleteTemplate(id)
    }
}
