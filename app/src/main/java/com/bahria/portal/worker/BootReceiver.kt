package com.bahria.portal.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bahria.portal.data.local.PreferencesManager

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        val prefs = PreferencesManager(context)
        if (prefs.isLoggedIn && prefs.isAutoCheckEnabled) {
            com.bahria.portal.service.BahriaBackgroundService.start(context)
            PortalSyncWorker.schedule(context, prefs.checkIntervalMinutes.toLong())
        }
    }
}
