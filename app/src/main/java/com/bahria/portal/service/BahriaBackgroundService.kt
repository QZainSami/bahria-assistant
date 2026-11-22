package com.bahria.portal.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.bahria.portal.data.local.PreferencesManager
import com.bahria.portal.data.repository.PortalRepository
import com.bahria.portal.notification.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BahriaBackgroundService : Service() {

    companion object {
        private const val TAG = "BahriaBackgroundService"
        const val NOTIFICATION_ID = 2001
        const val ACTION_START = "com.bahria.portal.service.ACTION_START"
        const val ACTION_STOP = "com.bahria.portal.service.ACTION_STOP"
        const val ACTION_SYNC_NOW = "com.bahria.portal.service.ACTION_SYNC_NOW"

        fun start(context: Context) {
            val intent = Intent(context, BahriaBackgroundService::class.java).apply {
                action = ACTION_START
            }
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to start foreground service: ${e.message}", e)
            }
        }

        fun stop(context: Context) {
            val intent = Intent(context, BahriaBackgroundService::class.java).apply {
                action = ACTION_STOP
            }
            try {
                context.startService(intent)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to stop foreground service: ${e.message}", e)
            }
        }

        fun syncNow(context: Context) {
            val intent = Intent(context, BahriaBackgroundService::class.java).apply {
                action = ACTION_SYNC_NOW
            }
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to send syncNow intent: ${e.message}", e)
            }
        }
    }

    private lateinit var prefs: PreferencesManager
    private lateinit var repository: PortalRepository
    private lateinit var notificationHelper: NotificationHelper
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
    private var syncLoopJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        prefs = PreferencesManager(this)
        repository = PortalRepository.getInstance(this)
        notificationHelper = NotificationHelper(this)
        Log.d(TAG, "BahriaBackgroundService created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action ?: ACTION_START

        if (action == ACTION_STOP) {
            Log.d(TAG, "Stopping BahriaBackgroundService")
            stopSyncLoop()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stopForeground(STOP_FOREGROUND_REMOVE)
            } else {
                @Suppress("DEPRECATION")
                stopForeground(true)
            }
            stopSelf()
            return START_NOT_STICKY
        }

        // Always show the ongoing "Running in background" foreground notification
        val initialText = if (prefs.lastSyncTimestamp > 0) {
            val lastTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(prefs.lastSyncTimestamp))
            "Running in background • Last checked: $lastTime"
        } else {
            "Running in background"
        }
        startInForeground(initialText)

        if (action == ACTION_SYNC_NOW) {
            serviceScope.launch {
                performBackgroundSync()
            }
        }

        startSyncLoop()
        return START_STICKY
    }

    private fun startInForeground(statusText: String) {
        val notification = notificationHelper.buildForegroundNotification(statusText)
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
        } catch (e: Exception) {
            Log.e(TAG, "startForeground failed: ${e.message}", e)
        }
    }

    private fun updateForegroundNotification(statusText: String) {
        try {
            val notification = notificationHelper.buildForegroundNotification(statusText)
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.notify(NOTIFICATION_ID, notification)
        } catch (e: Exception) {
            Log.w(TAG, "updateForegroundNotification failed: ${e.message}")
        }
    }

    private fun startSyncLoop() {
        syncLoopJob?.cancel()
        syncLoopJob = serviceScope.launch {
            while (isActive) {
                val intervalMins = prefs.checkIntervalMinutes.coerceAtLeast(5)
                val delayMillis = intervalMins * 60 * 1000L

                delay(delayMillis)

                if (prefs.isAutoCheckEnabled && (prefs.isLoggedIn || prefs.isDemoMode)) {
                    performBackgroundSync()
                }
            }
        }
    }

    private suspend fun performBackgroundSync() {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val checkTime = timeFormat.format(Date())
        Log.d(TAG, "Performing background sync at $checkTime")

        updateForegroundNotification("Running in background • Checking portal...")

        repository.refreshAll()
        repository.checkDueAssignmentAlerts()

        val nextIn = prefs.checkIntervalMinutes
        updateForegroundNotification("Running in background • Last checked: $checkTime (Next in ${nextIn}m)")
    }

    private fun stopSyncLoop() {
        syncLoopJob?.cancel()
        syncLoopJob = null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSyncLoop()
        Log.d(TAG, "BahriaBackgroundService destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
