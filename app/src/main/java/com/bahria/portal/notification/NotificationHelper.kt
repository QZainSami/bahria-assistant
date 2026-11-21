package com.bahria.portal.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.bahria.portal.R
import com.bahria.portal.ui.MainActivity

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ASSIGNMENTS = "channel_assignments"
        const val CHANNEL_ATTENDANCE = "channel_attendance"
        const val CHANNEL_SYNC = "channel_sync"

        private const val NOTIF_ID_ASSIGNMENT = 1001
        private const val NOTIF_ID_ATTENDANCE = 1002
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val assignChannel = NotificationChannel(
                CHANNEL_ASSIGNMENTS,
                "Assignments Updates",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for newly posted assignments and upcoming deadlines"
                enableVibration(true)
            }

            val attendChannel = NotificationChannel(
                CHANNEL_ATTENDANCE,
                "Attendance Warnings",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts when an absence is marked or attendance drops below 75%"
                enableVibration(true)
            }

            val syncChannel = NotificationChannel(
                CHANNEL_SYNC,
                "Background Sync",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Status for background sync checks"
            }

            notificationManager.createNotificationChannel(assignChannel)
            notificationManager.createNotificationChannel(attendChannel)
            notificationManager.createNotificationChannel(syncChannel)
        }
    }

    fun showAssignmentNotification(title: String, message: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra(MainActivity.EXTRA_OPEN_TAB, "ASSIGNMENTS")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ASSIGNMENTS)
            .setSmallIcon(R.drawable.ic_assignment)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            notificationManager.notify((System.currentTimeMillis() % 10000).toInt(), notification)
        } catch (_: Exception) {}
    }

    fun showAttendanceNotification(title: String, message: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra(MainActivity.EXTRA_OPEN_TAB, "ATTENDANCE")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            2,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ATTENDANCE)
            .setSmallIcon(R.drawable.ic_warning)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            notificationManager.notify((System.currentTimeMillis() % 10000).toInt() + 10, notification)
        } catch (_: Exception) {}
    }
}
