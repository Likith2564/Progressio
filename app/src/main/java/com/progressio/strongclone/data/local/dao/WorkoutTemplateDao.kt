package com.progressio.strongclone.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.progressio.strongclone.data.local.entity.TemplateExercise
import com.progressio.strongclone.data.local.entity.WorkoutTemplate
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutTemplateDao {
    @Query("SELECT * FROM workout_templates ORDER BY last_used_at DESC, created_at DESC")
    fun getAllTemplates(): Flow<List<WorkoutTemplate>>

    @Query("SELECT * FROM workout_templates WHERE id = :id")
    suspend fun getTemplateById(id: Long): WorkoutTemplate?

    @Query("SELECT * FROM template_exercises WHERE template_id = :templateId ORDER BY order_index")
    suspend fun getTemplateExercises(templateId: Long): List<TemplateExercise>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplate(template: WorkoutTemplate): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplateExercise(templateExercise: TemplateExercise)

    @Query("UPDATE workout_templates SET last_used_at = :time WHERE id = :templateId")
    suspend fun updateLastUsed(templateId: Long, time: Long)

    @Query("DELETE FROM workout_templates WHERE id = :id")
    suspend fun deleteTemplate(id: Long)

    @Query("DELETE FROM template_exercises WHERE template_id = :templateId")
    suspend fun deleteTemplateExercises(templateId: Long)
}
