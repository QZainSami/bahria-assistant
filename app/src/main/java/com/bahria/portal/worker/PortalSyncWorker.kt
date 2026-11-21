package com.bahria.portal.worker

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.bahria.portal.data.local.PreferencesManager
import com.bahria.portal.data.repository.PortalRepository
import java.util.concurrent.TimeUnit

class PortalSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val TAG = "PortalSyncWorker"
        const val WORK_NAME = "BahriaPortalPeriodicSync"

        fun schedule(context: Context, intervalMinutes: Long = 30) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val actualInterval = intervalMinutes.coerceAtLeast(15) // Android WorkManager enforces minimum 15 mins

            val syncRequest = PeriodicWorkRequestBuilder<PortalSyncWorker>(
                actualInterval, TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                syncRequest
            )
            Log.d(TAG, "Scheduled sync worker every $actualInterval minutes.")
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }

    override suspend fun doWork(): Result {
        val prefs = PreferencesManager(applicationContext)
        if (!prefs.isLoggedIn && !prefs.isDemoMode) {
            return Result.success()
        }

        if (!prefs.isAutoCheckEnabled) {
            return Result.success()
        }

        return try {
            val repo = PortalRepository.getInstance(applicationContext)
            val res = repo.refreshAll()
            if (res.isSuccess) {
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in sync worker: ${e.message}", e)
            Result.retry()
        }
    }
}
