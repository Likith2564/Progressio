package com.progressio.strongclone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val muscleGroup: String,
    val equipmentType: String = "",
    val instructions: String = "",
    val imageUrl: String? = null,
    val isCustom: Boolean = false
)
