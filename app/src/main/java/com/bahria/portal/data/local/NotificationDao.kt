package com.bahria.portal.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bahria.portal.data.model.NotificationItem
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: NotificationItem): Long

    @Query("DELETE FROM notifications")
    suspend fun deleteAll()

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()
}
