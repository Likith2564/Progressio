package com.progressio.strongclone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "body_measurements")
data class BodyMeasurement(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val measuredAt: Long = System.currentTimeMillis(),
    val weight: Double? = null,
    val bodyFatPercentage: Double? = null,
    val chest: Double? = null,
    val waist: Double? = null,
    val hips: Double? = null,
    val bicepsLeft: Double? = null,
    val bicepsRight: Double? = null,
    val thighsLeft: Double? = null,
    val thighsRight: Double? = null
)
