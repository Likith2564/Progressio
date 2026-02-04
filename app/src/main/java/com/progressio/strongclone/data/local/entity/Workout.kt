package com.progressio.strongclone.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workouts",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutTemplate::class,
            parentColumns = ["id"],
            childColumns = ["templateId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("templateId"), Index("startedAt")]
)
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val templateId: Long? = null,
    val name: String,
    val startedAt: Long = System.currentTimeMillis(),
    val finishedAt: Long? = null,
    val durationSeconds: Int? = null,
    val totalVolume: Double = 0.0,
    val notes: String = ""
)
