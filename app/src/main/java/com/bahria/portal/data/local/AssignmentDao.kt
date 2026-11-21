package com.bahria.portal.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bahria.portal.data.model.Assignment
import kotlinx.coroutines.flow.Flow

@Dao
interface AssignmentDao {
    @Query("SELECT * FROM assignments ORDER BY dueTimestamp ASC, updatedAt DESC")
    fun getAllAssignments(): Flow<List<Assignment>>

    @Query("SELECT * FROM assignments WHERE status = 'Pending' ORDER BY dueTimestamp ASC, updatedAt DESC")
    fun getPendingAssignments(): Flow<List<Assignment>>

    @Query("SELECT * FROM assignments")
    suspend fun getAllAssignmentsSync(): List<Assignment>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(assignments: List<Assignment>)

    @Query("DELETE FROM assignments")
    suspend fun deleteAll()
}
