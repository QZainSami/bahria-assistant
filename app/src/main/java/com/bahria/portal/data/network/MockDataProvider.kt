package com.bahria.portal.data.network

import com.bahria.portal.data.model.Assignment
import com.bahria.portal.data.model.CourseAttendance
import com.bahria.portal.data.model.NotificationItem

object MockDataProvider {

    fun getMockAttendance(): List<CourseAttendance> {
        val now = System.currentTimeMillis()
        return listOf(
            CourseAttendance(
                courseName = "Artificial Intelligence",
                courseCode = "CSC 412",
                present = 19.0,
                absent = 8.0,
                total = 27.0,
                percentage = 70.37,
                updatedAt = now
            ),
            CourseAttendance(
                courseName = "Theory of Automata",
                courseCode = "CSC 315",
                present = 26.0,
                absent = 7.0,
                total = 33.0,
                percentage = 78.79,
                updatedAt = now
            ),
            CourseAttendance(
                courseName = "Computer Architecture",
                courseCode = "CSC 302",
                present = 12.0,
                absent = 8.0,
                total = 20.0,
                percentage = 60.00,
                updatedAt = now
            ),
            CourseAttendance(
                courseName = "Operating Systems",
                courseCode = "CSC 312",
                present = 31.0,
                absent = 1.0,
                total = 32.0,
                percentage = 96.88,
                updatedAt = now
            ),
            CourseAttendance(
                courseName = "Computer Architecture Lab",
                courseCode = "CSC 302-L",
                present = 19.0,
                absent = 5.0,
                total = 24.0,
                percentage = 79.17,
                updatedAt = now
            ),
            CourseAttendance(
                courseName = "Artificial Intelligence Lab",
                courseCode = "CSC 412-L",
                present = 20.0,
                absent = 2.0,
                total = 22.0,
                percentage = 90.91,
                updatedAt = now
            ),
            CourseAttendance(
                courseName = "Design & Analysis of Algorithms",
                courseCode = "CSC 310",
                present = 28.0,
                absent = 4.0,
                total = 32.0,
                percentage = 87.50,
                updatedAt = now
            )
        )
    }

    fun getMockAssignments(): List<Assignment> {
        val now = System.currentTimeMillis()
        val oneDay = 24 * 60 * 60 * 1000L
        return listOf(
            Assignment(
                id = "cs310_ccp",
                title = "Complex Computing Problem",
                courseName = "Design & Analysis of Algorithms",
                dueDateString = "15 Dec 2026 - 11:55 PM",
                dueTimestamp = now + (24 * oneDay),
                status = "Pending",
                downloadUrl = "https://lms.bahria.edu.pk/files/DAA_Complex_Problem_2026.pdf",
                fileName = "DAA_Complex_Computing_Problem.pdf",
                updatedAt = now
            ),
            Assignment(
                id = "cs312l_ass10",
                title = "Assignment 10: Multithreading & POSIX",
                courseName = "Operating Systems Lab",
                dueDateString = "22 Nov 2026 - 11:55 PM",
                dueTimestamp = now + (1 * oneDay),
                status = "Pending",
                downloadUrl = "https://lms.bahria.edu.pk/files/OS_Lab10_Tasks.pdf",
                fileName = "OS_Lab10_Multithreading.pdf",
                updatedAt = now
            ),
            Assignment(
                id = "cs411_ass2",
                title = "Ass#2: A* Search & Minimax Alpha-Beta",
                courseName = "Artificial Intelligence",
                dueDateString = "25 Nov 2026 - 03:00 PM",
                dueTimestamp = now + (4 * oneDay),
                status = "Pending",
                downloadUrl = "https://lms.bahria.edu.pk/files/AI_Assignment_2.pdf",
                fileName = "AI_Assignment_2_Search.pdf",
                updatedAt = now
            ),
            Assignment(
                id = "cs302l_task7",
                title = "Lab Task 7: Memory Hierarchy & Cache Mapping",
                courseName = "Computer Architecture Lab",
                dueDateString = "10 Nov 2026 - 11:55 PM",
                dueTimestamp = now - (11 * oneDay),
                status = "Submitted",
                downloadUrl = "https://lms.bahria.edu.pk/files/CA_Lab7_Manual.pdf",
                fileName = "CA_Lab7_MemoryHierarchy.pdf",
                updatedAt = now
            ),
            Assignment(
                id = "cs314_quiz3",
                title = "Grammar & Pushdown Automata Practice",
                courseName = "Theory of Automata",
                dueDateString = "05 Nov 2026 - 10:00 AM",
                dueTimestamp = now - (16 * oneDay),
                status = "Submitted",
                downloadUrl = "https://lms.bahria.edu.pk/files/TOA_Grammar_Tasks.pdf",
                fileName = "TOA_PDA_Grammar_Tasks.pdf",
                updatedAt = now
            )
        )
    }

    fun getMockNotifications(): List<NotificationItem> {
        val now = System.currentTimeMillis()
        val oneHour = 60 * 60 * 1000L
        return listOf(
            NotificationItem(
                id = 1,
                title = "📝 New Assignment: Complex Computing Problem",
                message = "Design & Analysis of Algorithms • Due: 15 Dec 2026 - 11:55 PM",
                type = "ASSIGNMENT",
                timestamp = now - (10 * 60 * 1000L),
                isRead = false
            ),
            NotificationItem(
                id = 2,
                title = "⚠️ Attendance Alert: Computer Architecture",
                message = "You were marked absent. Your attendance is now 60.00% (Critical).",
                type = "ATTENDANCE",
                timestamp = now - (2 * oneHour),
                isRead = false
            ),
            NotificationItem(
                id = 3,
                title = "📝 New Assignment: Assignment 10",
                message = "Operating Systems Lab • Due: 22 Nov 2026 - 11:55 PM",
                type = "ASSIGNMENT",
                timestamp = now - (5 * oneHour),
                isRead = true
            )
        )
    }
}
