package com.progressio.strongclone.di

import android.content.Context
import androidx.room.Room
import com.progressio.strongclone.data.local.AppDatabase
import com.progressio.strongclone.data.local.dao.BodyMeasurementDao
import com.progressio.strongclone.data.local.dao.ExerciseDao
import com.progressio.strongclone.data.local.dao.PersonalRecordDao
import com.progressio.strongclone.data.local.dao.WorkoutDao
import com.progressio.strongclone.data.local.dao.WorkoutTemplateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "strong_clone.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideExerciseDao(db: AppDatabase): ExerciseDao = db.exerciseDao()

    @Provides
    @Singleton
    fun provideWorkoutTemplateDao(db: AppDatabase): WorkoutTemplateDao = db.workoutTemplateDao()

    @Provides
    @Singleton
    fun provideWorkoutDao(db: AppDatabase): WorkoutDao = db.workoutDao()

    @Provides
    @Singleton
    fun provideBodyMeasurementDao(db: AppDatabase): BodyMeasurementDao = db.bodyMeasurementDao()

    @Provides
    @Singleton
    fun providePersonalRecordDao(db: AppDatabase): PersonalRecordDao = db.personalRecordDao()
}
