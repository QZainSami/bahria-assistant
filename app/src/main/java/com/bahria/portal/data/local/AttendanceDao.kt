package com.bahria.portal.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bahria.portal.data.model.CourseAttendance
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {
    @Query("SELECT * FROM attendance ORDER BY courseName ASC")
    fun getAllAttendance(): Flow<List<CourseAttendance>>

    @Query("SELECT * FROM attendance ORDER BY courseName ASC")
    suspend fun getAllAttendanceSync(): List<CourseAttendance>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(courses: List<CourseAttendance>)

    @Query("DELETE FROM attendance")
    suspend fun deleteAll()
}
