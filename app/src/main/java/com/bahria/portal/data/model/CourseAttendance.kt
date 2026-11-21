package com.bahria.portal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max

@Entity(tableName = "attendance")
data class CourseAttendance(
    @PrimaryKey
    val courseName: String,
    val courseCode: String = "",
    val present: Double,
    val absent: Double,
    val total: Double,
    val percentage: Double,
    val updatedAt: Long = System.currentTimeMillis()
) {
    val isLab: Boolean
        get() = courseName.contains("Lab", ignoreCase = true) || courseCode.contains("L", ignoreCase = true)

    val creditHours: Int
        get() = if (isLab) 1 else 3

    val displayCode: String
        get() {
            if (courseCode.isNotBlank()) return courseCode
            val words = courseName.split(" ").filter { it.isNotBlank() }
            val acronym = words.mapNotNull { it.firstOrNull()?.uppercaseChar() }.joinToString("")
            return if (isLab) "CSC $acronym-L" else "CSC $acronym"
        }

    val isCritical: Boolean
        get() = percentage < 75.0

    val isWarning: Boolean
        get() = percentage in 75.0..79.99

    val isSafe: Boolean
        get() = percentage >= 80.0

    /**
     * Maximum allowed absence hours in semester (25% rule).
     * 3 Credit Hours = 48 contact hours -> 12 hours max absence.
     * 1 Credit Hour Lab = 32 contact hours -> 8 hours max absence.
     */
    val maxAllowedAbsentHours: Int
        get() = if (isLab) 8 else 12

    val usedAbsentHours: Int
        get() = absent.toInt()

    val leftAbsentHours: Int
        get() = max(0, maxAllowedAbsentHours - usedAbsentHours)

    /**
     * Hours student needs to attend consecutively to reach 75%.
     */
    val hoursNeededFor75: Int
        get() {
            if (percentage >= 75.0) return 0
            val needed = ceil(3.0 * total - 4.0 * present).toInt()
            return max(1, needed)
        }

    /**
     * Hours student can afford to miss while remaining >= 75%.
     */
    val safeBunksAvailable: Int
        get() {
            if (percentage < 75.0) return 0
            val maxAllowedByFormula = floor((present / 0.75) - total).toInt()
            return max(0, minOf(maxAllowedByFormula, leftAbsentHours))
        }
}
