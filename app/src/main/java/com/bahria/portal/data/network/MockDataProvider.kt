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
                courseName = "Operating Systems Lab",
                courseCode = "CSC 312-L",
                present = 22.0,
                absent = 2.0,
                total = 24.0,
                percentage = 91.67,
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
                id = "AI_Ass2",
                title = "Ass#2",
                courseName = "Artificial Intelligence",
                dueDateString = "25 November 2026-03:00 pm",
                dueTimestamp = now + (4 * oneDay),
                status = "Pending",
                downloadUrl = "https://lms.bahria.edu.pk/Student/Download.php?k=AfcofxoMqNJUs0pog4Gbj-Zt1VLtMFJnQ4jfh4mNmcSzby0HtDBzdRwvTsGr5dgkjqEDDEL9rmaq3simNzDhP9hdkHsOOANi_1gTkRX4qbuNwzEGBcX2GvnW9e0vqUOm8V2CSaRQVhn2IqTyuoAD18qOer_gqeH6drRGM5g78tpOogdh5f9lLoresaZHDoZAipJMABdzGf48kPUHsQFHF7O7hlC-X9e95-W0NjIae0ZR_T4YymJiokN9ZB-fHhcOoEJDMbhrQI5-5-tkAODpHMqUR7WmSuqLOXITYsRlPtufOkLL1cZkz2gduAdcNbNmKk0yVCasHTp2_QaK6ui9Z0G5qSUrdYY9at0uQcsF_6SsZQS3DrGMq9-eJbZl4zhVOUHmsIHHI6Jpcf6NP_R-iAdZbb6g1wYfDoowL_jQeDaYfk1Ijcr_rnirM8xJijAh7OxpalYWeYzetuH3xzbVlaQ",
                fileName = "Artificial_Intelligence_Ass2.docx",
                updatedAt = now
            ),
            Assignment(
                id = "cs310_ccp",
                title = "Complex Computing Problem",
                courseName = "Design & Analysis of Algorithms",
                dueDateString = "15 December 2026-11:55 pm",
                dueTimestamp = now + (24 * oneDay),
                status = "Pending",
                downloadUrl = "https://lms.bahria.edu.pk/Student/Download.php?k=AfpXY2cdVMlePppKZt24df3_WfqG3bMaJl2U4-s_9EdJ3JraC2v3Pr4gJiUOTGEno7jRDsFMyo4yyek-tzBXmCzQkKLi6_EDtE2brOG1h-09GPCaHuMJa-PZsm7u5SgBYk436kKEZOhadEFFUXLmf_R3etMhPCo7GuOzQ-q_NA3lnBdD0HpqH2K9m6R_yBelNoeZs_gFQ3g9tFLMrLk18xZ91ZgY553Z4v5kk5CFD8jlI5KnkaymzWE7rDeX1mVy9NgcgIyB06wnQPMhF6vYWPQiSEpybFGEXh9wa4Zt1xSjLSxhPt5jQjwELfv97umwWNyE8E6RcPNFu3On8cdnomfRCU-wmchmM7wS5vWOeJTNhkKRDdLszNyq6d9JtB1U9oC9_AH_sjnEAhWGmzBfyE4JiU4l3YlB57ltVt913wg2jl0Her-IzzsJLrpfo7ofZag_b5ePjQkpBXmqu3XGV8U",
                fileName = "DAA_Complex_Computing_Problem.docx",
                updatedAt = now
            ),
            Assignment(
                id = "AI_Ass1",
                title = "Ass#1",
                courseName = "Artificial Intelligence",
                dueDateString = "24 October 2026-02:30 pm",
                dueTimestamp = now - (28 * oneDay),
                status = "Submitted",
                downloadUrl = "https://lms.bahria.edu.pk/Student/Download.php?k=AWQC5PfCtbJYYfY7hDLYt98z0vVGbQk4OwrXrNlXUsg1Xs2Md1EkxULhsFj2ULnso34_3kXqbublv5z-gZt22FK3CEt2_XExf-b-CFd3eQuK3PZMHcz3KylkhqYbo05ddZKPqfEQ_wLapYirCUVHvZHDblw9yQSNMrxv197eSKmAthORGxZow7dW5fEXykSJyMjKsgqIzhd9Lq_Thh26nmOD0z-76-ZdJjQuNEL-d92Pl1ZbcTjGVAZiaS_5LU0RCrPWBb3R6P2SX1ssSwHQwmjtRqT2jFwbOBgjVyqdUs3nvIVctp0i-ksqPvPBNUfbJ4sHII5NvHB1WGyiYHXXkZoFYOKjbWakMifmjeJC7g9HwtqN3Li2-L7tl4b-biaETJhJjZGK1_sUgWfYQ9cKhoSIiq-BRwZreEwrSgdFk_nEpzw0GD6U7DCBvm87G4sKRoCystJrbgm85YuOQFtoELI",
                fileName = "Artificial_Intelligence_Ass1.docx",
                updatedAt = now
            ),
            Assignment(
                id = "cs312l_ass10",
                title = "Assignment 10: Multithreading & POSIX",
                courseName = "Operating Systems Lab",
                dueDateString = "22 November 2026-11:55 pm",
                dueTimestamp = now + (1 * oneDay),
                status = "Pending",
                downloadUrl = "https://lms.bahria.edu.pk/Student/Download.php?k=mock_os_10",
                fileName = "OS_Lab10_Multithreading.docx",
                updatedAt = now
            ),
            Assignment(
                id = "AI_CCP_Past",
                title = "CCP",
                courseName = "Artificial Intelligence",
                dueDateString = "12 December 2025-03:00 pm",
                dueTimestamp = now - (340 * oneDay),
                status = "Overdue",
                downloadUrl = "https://lms.bahria.edu.pk/Student/Download.php?k=mock_ccp_past",
                fileName = "Artificial_Intelligence_CCP.docx",
                updatedAt = now
            ),
            Assignment(
                id = "cs302l_task7",
                title = "Lab Task 7: Memory Hierarchy & Cache Mapping",
                courseName = "Computer Architecture Lab",
                dueDateString = "10 November 2026-11:55 pm",
                dueTimestamp = now - (11 * oneDay),
                status = "Submitted",
                downloadUrl = "https://lms.bahria.edu.pk/Student/Download.php?k=mock_ca_7",
                fileName = "CA_Lab7_MemoryHierarchy.pdf",
                updatedAt = now
            ),
            Assignment(
                id = "cs314_quiz3",
                title = "Grammar & Pushdown Automata Practice",
                courseName = "Theory of Automata",
                dueDateString = "05 November 2026-10:00 am",
                dueTimestamp = now - (16 * oneDay),
                status = "Submitted",
                downloadUrl = "https://lms.bahria.edu.pk/Student/Download.php?k=mock_toa_quiz3",
                fileName = "TOA_PDA_Grammar_Tasks.docx",
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
