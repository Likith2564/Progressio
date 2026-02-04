package com.progressio.strongclone

import android.app.Application
import com.progressio.strongclone.data.local.AppDatabase
import com.progressio.strongclone.data.local.DefaultExercises
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class StrongCloneApp : Application() {

    @Inject
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            if (database.exerciseDao().getCount() == 0) {
                database.exerciseDao().insertAll(DefaultExercises.list)
            }
        }
    }
}
