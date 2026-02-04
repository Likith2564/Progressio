package com.progressio.strongclone.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.progressio.strongclone.data.local.dao.BodyMeasurementDao
import com.progressio.strongclone.data.local.dao.ExerciseDao
import com.progressio.strongclone.data.local.dao.PersonalRecordDao
import com.progressio.strongclone.data.local.dao.WorkoutDao
import com.progressio.strongclone.data.local.dao.WorkoutTemplateDao
import com.progressio.strongclone.data.local.entity.BodyMeasurement
import com.progressio.strongclone.data.local.entity.Exercise
import com.progressio.strongclone.data.local.entity.PersonalRecord
import com.progressio.strongclone.data.local.entity.TemplateExercise
import com.progressio.strongclone.data.local.entity.Workout
import com.progressio.strongclone.data.local.entity.WorkoutSet
import com.progressio.strongclone.data.local.entity.WorkoutTemplate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Exercise::class,
        WorkoutTemplate::class,
        TemplateExercise::class,
        Workout::class,
        WorkoutSet::class,
        BodyMeasurement::class,
        PersonalRecord::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutTemplateDao(): WorkoutTemplateDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun bodyMeasurementDao(): BodyMeasurementDao
    abstract fun personalRecordDao(): PersonalRecordDao
}
