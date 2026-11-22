package com.bahria.portal

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.bahria.portal.data.local.PreferencesManager
import com.bahria.portal.notification.NotificationHelper
import com.bahria.portal.worker.PortalSyncWorker

class BahriaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Force dark mode for sleek, modern appearance
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        // Init notification channels
        NotificationHelper(this)

        // Schedule periodic sync & start background service if enabled
        val prefs = PreferencesManager(this)
        if ((prefs.isLoggedIn || prefs.isDemoMode) && prefs.isAutoCheckEnabled) {
            com.bahria.portal.service.BahriaBackgroundService.start(this)
            PortalSyncWorker.schedule(this, prefs.checkIntervalMinutes.toLong())
        }
    }
}
