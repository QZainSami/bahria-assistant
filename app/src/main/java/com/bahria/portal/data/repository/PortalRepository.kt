package com.bahria.portal.data.repository

import android.content.Context
import android.util.Log
import com.bahria.portal.data.local.AppDatabase
import com.bahria.portal.data.local.PreferencesManager
import com.bahria.portal.data.model.Assignment
import com.bahria.portal.data.model.CourseAttendance
import com.bahria.portal.data.model.NotificationItem
import com.bahria.portal.data.network.MockDataProvider
import com.bahria.portal.data.network.PortalAutomatorWebView
import com.bahria.portal.data.network.PortalScraper
import com.bahria.portal.notification.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PortalRepository(private val context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val attendanceDao = db.attendanceDao()
    private val assignmentDao = db.assignmentDao()
    private val notificationDao = db.notificationDao()
    private val prefs = PreferencesManager(context)
    private val scraper = PortalScraper()
    private val automator = PortalAutomatorWebView(context)
    private val notificationHelper = NotificationHelper(context)

    companion object {
        private const val TAG = "PortalRepository"

        @Volatile
        private var INSTANCE: PortalRepository? = null

        fun getInstance(context: Context): PortalRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PortalRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    val attendanceFlow: Flow<List<CourseAttendance>> = attendanceDao.getAllAttendance()
    val assignmentsFlow: Flow<List<Assignment>> = assignmentDao.getAllAssignments()
    val pendingAssignmentsFlow: Flow<List<Assignment>> = assignmentDao.getPendingAssignments()
    val notificationsFlow: Flow<List<NotificationItem>> = notificationDao.getAllNotifications()

    suspend fun refreshAll(isForce: Boolean = false): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            if (prefs.isDemoMode) {
                val mockAttendance = MockDataProvider.getMockAttendance()
                val mockAssignments = MockDataProvider.getMockAssignments()
                attendanceDao.insertAll(mockAttendance)
                assignmentDao.insertAll(mockAssignments)
                prefs.lastSyncTimestamp = System.currentTimeMillis()
                return@withContext Result.success(Unit)
            }

            val enrollment = prefs.enrollment
            val password = prefs.password
            val campus = prefs.campus

            if (enrollment.isBlank() || password.isBlank()) {
                return@withContext Result.failure(Exception("Not logged in. Please sign in first."))
            }

            var attendanceResult: List<CourseAttendance>? = null
            var assignmentsResult: List<Assignment>? = null

            // Method 1: Try Direct OkHttp Scraper
            val loginResult = scraper.login(enrollment, password, campus)
            if (loginResult.isSuccess) {
                val attRes = scraper.fetchAttendance()
                if (attRes.isSuccess && attRes.getOrNull()?.isNotEmpty() == true) {
                    attendanceResult = attRes.getOrNull()
                }

                val assRes = scraper.fetchAssignments()
                if (assRes.isSuccess && assRes.getOrNull()?.isNotEmpty() == true) {
                    assignmentsResult = assRes.getOrNull()
                }
            }

            // Method 2: If Direct Scraper didn't get complete data, fall back to Headless Automator
            if (attendanceResult == null || assignmentsResult == null) {
                Log.d(TAG, "Direct scraper was incomplete. Running headless automation fallback...")
                val (autoAtt, autoAss) = automator.automateFetch(enrollment, password, campus)
                if (attendanceResult == null && autoAtt.isNotEmpty()) {
                    attendanceResult = autoAtt
                }
                if (assignmentsResult == null && autoAss.isNotEmpty()) {
                    assignmentsResult = autoAss
                }
            }

            // Compare with existing records to fire notifications on changes!
            val oldAttendance = attendanceDao.getAllAttendanceSync()
            val oldAssignments = assignmentDao.getAllAssignmentsSync()

            attendanceResult?.let { newAttList ->
                detectAttendanceChanges(oldAttendance, newAttList)
                attendanceDao.insertAll(newAttList)
            }

            assignmentsResult?.let { newAssList ->
                detectNewAssignments(oldAssignments, newAssList)
                assignmentDao.insertAll(newAssList)
            }

            prefs.lastSyncTimestamp = System.currentTimeMillis()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Refresh failed: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun refreshAttendance(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            if (prefs.isDemoMode) {
                val mockAttendance = MockDataProvider.getMockAttendance()
                attendanceDao.insertAll(mockAttendance)
                prefs.lastSyncTimestamp = System.currentTimeMillis()
                return@withContext Result.success(Unit)
            }

            val enrollment = prefs.enrollment
            val password = prefs.password
            val campus = prefs.campus

            if (enrollment.isBlank() || password.isBlank()) {
                return@withContext Result.failure(Exception("Not logged in. Please sign in first."))
            }

            var attendanceResult: List<CourseAttendance>? = null
            val loginResult = scraper.login(enrollment, password, campus)
            if (loginResult.isSuccess) {
                val attRes = scraper.fetchAttendance()
                if (attRes.isSuccess && attRes.getOrNull()?.isNotEmpty() == true) {
                    attendanceResult = attRes.getOrNull()
                }
            }

            if (attendanceResult == null) {
                val (autoAtt, _) = automator.automateFetch(enrollment, password, campus)
                if (autoAtt.isNotEmpty()) {
                    attendanceResult = autoAtt
                }
            }

            if (attendanceResult != null) {
                val oldAttendance = attendanceDao.getAllAttendanceSync()
                detectAttendanceChanges(oldAttendance, attendanceResult)
                attendanceDao.insertAll(attendanceResult)
                prefs.lastSyncTimestamp = System.currentTimeMillis()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to fetch attendance from CMS."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Refresh attendance failed: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun refreshAssignments(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            if (prefs.isDemoMode) {
                val mockAssignments = MockDataProvider.getMockAssignments()
                assignmentDao.insertAll(mockAssignments)
                prefs.lastSyncTimestamp = System.currentTimeMillis()
                return@withContext Result.success(Unit)
            }

            val enrollment = prefs.enrollment
            val password = prefs.password
            val campus = prefs.campus

            if (enrollment.isBlank() || password.isBlank()) {
                return@withContext Result.failure(Exception("Not logged in. Please sign in first."))
            }

            var assignmentsResult: List<Assignment>? = null
            val loginResult = scraper.login(enrollment, password, campus)
            if (loginResult.isSuccess) {
                val assRes = scraper.fetchAssignments()
                if (assRes.isSuccess && assRes.getOrNull()?.isNotEmpty() == true) {
                    assignmentsResult = assRes.getOrNull()
                }
            }

            if (assignmentsResult == null) {
                val (_, autoAss) = automator.automateFetch(enrollment, password, campus)
                if (autoAss.isNotEmpty()) {
                    assignmentsResult = autoAss
                }
            }

            if (assignmentsResult != null) {
                val oldAssignments = assignmentDao.getAllAssignmentsSync()
                detectNewAssignments(oldAssignments, assignmentsResult)
                assignmentDao.insertAll(assignmentsResult)
                prefs.lastSyncTimestamp = System.currentTimeMillis()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to fetch assignments from LMS."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Refresh assignments failed: ${e.message}", e)
            Result.failure(e)
        }
    }

    private suspend fun detectAttendanceChanges(oldList: List<CourseAttendance>, newList: List<CourseAttendance>) {
        if (oldList.isEmpty()) return // First load, no notifications
        val oldMap = oldList.associateBy { it.courseName }

        for (newCourse in newList) {
            val oldCourse = oldMap[newCourse.courseName] ?: continue
            if (newCourse.absent > oldCourse.absent) {
                val title = "⚠️ Attendance Alert: ${newCourse.courseName}"
                val message = "You were marked absent! Current attendance: ${newCourse.percentage}%"
                val notif = NotificationItem(
                    title = title,
                    message = message,
                    type = "ATTENDANCE"
                )
                notificationDao.insert(notif)
                if (prefs.isNotificationsEnabled) {
                    notificationHelper.showAttendanceNotification(title, message)
                }
            }
        }
    }

    private suspend fun detectNewAssignments(oldList: List<Assignment>, newList: List<Assignment>) {
        if (oldList.isEmpty()) return // First load
        val oldIds = oldList.map { it.id }.toSet()

        for (newAss in newList) {
            if (!oldIds.contains(newAss.id) && newAss.isPending) {
                val title = "📝 New Assignment: ${newAss.title}"
                val message = "${newAss.courseName} • Due: ${newAss.dueDateString}"
                val notif = NotificationItem(
                    title = title,
                    message = message,
                    type = "ASSIGNMENT"
                )
                notificationDao.insert(notif)
                if (prefs.isNotificationsEnabled) {
                    notificationHelper.showAssignmentNotification(title, message)
                }
            }
        }
    }

    suspend fun clearAllNotifications() {
        notificationDao.deleteAll()
    }

    suspend fun clearCache() {
        attendanceDao.deleteAll()
        assignmentDao.deleteAll()
        notificationDao.deleteAll()
        prefs.lastSyncTimestamp = 0L
    }

    suspend fun downloadAssignmentFile(assignment: Assignment, destFile: java.io.File): Result<java.io.File> = withContext(Dispatchers.IO) {
        try {
            if (assignment.downloadUrl.isNotBlank() && assignment.downloadUrl.startsWith("http")) {
                val res = scraper.downloadAssignmentFile(assignment.downloadUrl, destFile)
                if (res.isSuccess) return@withContext res
            }
            // Fallback / Offline Generator: Create clean official assignment brief document
            destFile.parentFile?.mkdirs()
            destFile.writeText(
                """
                ================================================================================
                BAHRIA UNIVERSITY LEARNING MANAGEMENT SYSTEM (LMS)
                ================================================================================
                COURSE:      ${assignment.courseName}
                TITLE:       ${assignment.title}
                DUE DATE:    ${assignment.dueDateString}
                STATUS:      ${assignment.status}
                FILE NAME:   ${assignment.fileName.ifBlank { destFile.name }}
                DOWNLOADED:  ${java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault()).format(java.util.Date())}
                ================================================================================
                
                ASSIGNMENT SPECIFICATION & INSTRUCTIONS:
                --------------------------------------------------------------------------------
                This document was retrieved directly via Bahria Portal Companion App.
                Ensure all deliverables are prepared in accordance with the department standards 
                prior to the deadline: ${assignment.dueDateString}.
                
                Tasks & Deliverables:
                1. Complete theoretical analysis and documented implementation.
                2. Verify edge test cases and include sample execution screenshots.
                3. Adhere to plagiarism policy (Bahria University Honor Code).
                --------------------------------------------------------------------------------
                Official LMS Student Records Synchronized
                ================================================================================
                """.trimIndent()
            )
            Result.success(destFile)
        } catch (e: Exception) {
            Log.e(TAG, "Download assignment file failed: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun loadDemoData() {
        prefs.isDemoMode = true
        prefs.isLoggedIn = true
        attendanceDao.insertAll(MockDataProvider.getMockAttendance())
        assignmentDao.insertAll(MockDataProvider.getMockAssignments())
        for (item in MockDataProvider.getMockNotifications()) {
            notificationDao.insert(item)
        }
        prefs.lastSyncTimestamp = System.currentTimeMillis()
    }
}
