package com.progressio.strongclone.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.progressio.strongclone.data.local.entity.BodyMeasurement
import kotlinx.coroutines.flow.Flow

@Dao
interface BodyMeasurementDao {
    @Query("SELECT * FROM body_measurements ORDER BY measured_at DESC")
    fun getAllMeasurements(): Flow<List<BodyMeasurement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(measurement: BodyMeasurement): Long
}
