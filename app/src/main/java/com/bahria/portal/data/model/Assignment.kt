package com.bahria.portal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "assignments")
data class Assignment(
    @PrimaryKey
    val id: String,
    val title: String,
    val courseName: String,
    val dueDateString: String,
    val dueTimestamp: Long = 0L,
    val status: String = "Pending", // "Pending", "Submitted", "Overdue"
    val downloadUrl: String = "",
    val fileName: String = "",
    val updatedAt: Long = System.currentTimeMillis()
) {
    val isPending: Boolean
        get() = status.equals("Pending", ignoreCase = true)

    val isSubmitted: Boolean
        get() = status.equals("Submitted", ignoreCase = true)

    val calculatedHoursRemaining: Long
        get() {
            val target = if (dueTimestamp > 0) dueTimestamp else parseDate(dueDateString)
            val diff = target - System.currentTimeMillis()
            return if (diff > 0) diff / (1000 * 60 * 60) else 0L
        }

    val calculatedDaysRemaining: Long
        get() {
            val hours = calculatedHoursRemaining
            return (hours + 23) / 24
        }

    val isOverdue: Boolean
        get() {
            val target = if (dueTimestamp > 0) dueTimestamp else parseDate(dueDateString)
            return target in 1 until System.currentTimeMillis()
        }

    companion object {
        fun parseDate(dateStr: String): Long {
            if (dateStr.isBlank()) return 0L
            // Bahria LMS format example: "25 November 2026-03:00 pm"
            val raw = dateStr.trim()
            val sanitized = raw.replace("-", " ").replace("\\s+".toRegex(), " ")

            val formats = arrayOf(
                "dd MMMM yyyy hh:mm a",
                "dd MMMM yyyy - hh:mm a",
                "dd MMMM yyyy-hh:mm a",
                "dd MMM yyyy hh:mm a",
                "dd MMM yyyy - hh:mm a",
                "yyyy-MM-dd HH:mm:ss",
                "dd-MM-yyyy HH:mm",
                "yyyy-MM-dd",
                "dd/MM/yyyy hh:mm a"
            )

            // Try sanitized first
            for (fmt in formats) {
                try {
                    val sdf = SimpleDateFormat(fmt, Locale.ENGLISH)
                    val d = sdf.parse(sanitized)
                    if (d != null) return d.time
                } catch (_: Exception) {}
            }

            // Fallback to raw string
            for (fmt in formats) {
                try {
                    val sdf = SimpleDateFormat(fmt, Locale.ENGLISH)
                    val d = sdf.parse(raw)
                    if (d != null) return d.time
                } catch (_: Exception) {}
            }

            return 0L
        }
    }
}
