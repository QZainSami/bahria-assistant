package com.bahria.portal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val message: String,
    val type: String, // "ATTENDANCE", "ASSIGNMENT", "SYSTEM"
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)
