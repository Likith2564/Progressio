package com.progressio.strongclone.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.progressio.strongclone.data.local.entity.PersonalRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonalRecordDao {
    @Query("SELECT * FROM personal_records WHERE exercise_id = :exerciseId ORDER BY achieved_at DESC")
    fun getRecordsForExercise(exerciseId: Long): Flow<List<PersonalRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: PersonalRecord)
}
