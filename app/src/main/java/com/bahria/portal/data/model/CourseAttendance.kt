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
    val isCritical: Boolean
        get() = percentage < 75.0

    val isWarning: Boolean
        get() = percentage in 75.0..79.99

    val isSafe: Boolean
        get() = percentage >= 80.0

    /**
     * Number of upcoming classes the student can safely skip while keeping attendance >= 75%.
     */
    val safeBunksAvailable: Int
        get() {
            if (percentage < 75.0) return 0
            val maxAllowed = floor((present / 0.75) - total).toInt()
            return max(0, maxAllowed)
        }

    /**
     * Number of consecutive upcoming classes the student must attend to bring attendance to >= 75%.
     */
    val classesNeededFor75: Int
        get() {
            if (percentage >= 75.0) return 0
            val needed = ceil(3.0 * total - 4.0 * present).toInt()
            return max(1, needed)
        }
}
