# 🎓 Bahria Portal Companion

A modern, high-performance, and beautifully crafted Android companion application for **Bahria University** students to track **CMS Attendance** and **LMS Assignments** with instant background updates, proactive deadline alerts, offline caching, and direct document downloads.

---

## 🚀 Key Improvements Over the Legacy Portal App

| Feature | Legacy App | Bahria Portal Companion |
| :--- | :--- | :--- |
| **Look & Feel** | Amateurish, harsh solid colors, no animations, cramped tables | Sleek Dracula OLED Dark theme (`#12131A`), refined typography, and compact layout |
| **Sync Speed** | Sequential, slow 30s+ scraping | Concurrently fetches CMS attendance and LMS assignments in parallel in 2-3 seconds |
| **Assignment Alerts** | ❌ No reminder alerts for unsubmitted or due assignments | Proactive alerts for assignments Due Today (≤ 24h), Due Soon (24-48h), and Missed Deadlines |
| **Background Service** | ❌ Easily killed by OS battery optimization; no ongoing indicator | Persistent Android Foreground Service with real-time status: `"Running in background • Last checked: hh:mm"` |
| **Persistence & State** | ❌ Switching tabs wiped attendance data completely | **Room SQLite Local Database**: Attendance & assignments persist permanently across tabs and app restarts |
| **Offline Experience** | ❌ Blank screens when offline or switching tabs | Instant offline loading with clear "Last updated" timestamps |
| **In-App Downloads** | ❌ Opens web portal or corrupt downloads | Clean one-tap assignment downloader directly saving valid `.docx` / `.pdf` files to device storage |
| **Bunk & Goal Planner** | ❌ Clunky or absent | Integrated real-time attendance math showing exact hours allowed, used, and required to maintain 75% |
| **Demo Mode** | ❌ Not available | One-tap demo mode with realistic semester courses to explore without portal downtime |

---

## 📱 Core Features

### 1. 📊 Smart Attendance Dashboard
- **Exact Portal Recreation**: Displays Current Rate, Max Allowed Hours, Used Hours, and Hours Left.
- **Dynamic Progress Bar**: Color transitions dynamically between Green (≥ 75%) and Red (< 75%).
- **Threshold Coding**:
  - 🟢 **Safe (≥ 80%)**: Attendance is in the clear.
  - 🟡 **Warning (75% - 79.9%)**: Close to the threshold.
  - 🔴 **Critical (< 75%)**: Debarred from exams warning badge.
- **Filter Chips**: Filter by *All Courses*, *Critical (< 75%)*, or *Safe (≥ 80%)*.

### 2. 📝 LMS Assignments Hub & Direct Downloader
- **Direct Download Button**: Tap the download icon on any assignment card to immediately save the assignment document to your phone's Download folder.
- **Format Integrity**: Correctly retrieves or structures genuine `.docx` and `.pdf` files that open seamlessly in Microsoft Word, Google Docs, or PDF viewers.
- **Proactive Deadline Alerts**:
  - 🚨 **Due Today**: High-priority alert when an unsubmitted assignment is due within 24 hours.
  - ⏳ **Due Soon**: Reminder when an unsubmitted assignment is due within 48 hours.
  - ⚠️ **Deadline Passed**: Notification if an assignment wasn't submitted before the deadline.
- **Filter Chips**: Quickly switch between *All*, *Pending*, and *Submitted* assignments.

### 3. 🔄 Reliable Background Engine & Sync
- **Foreground Service**: Stays alive reliably in the background with an ongoing status bar notification: `"Bahria Assistant - Running in background • Last checked: [time] (Next in [X]m)"`.
- **Configurable Intervals**: Choose check frequency (15m, 30m, 1h, 3h, 6h) directly in Settings.
- **Boot Persistence**: Automatically restarts after device reboot.

---

## 🛠️ Architecture & Tech Stack

- **Language**: Kotlin 2.0+
- **Architecture**: MVVM + Repository Pattern + Single Source of Truth
- **Local Persistence**: Android Jetpack Room Database (SQLite)
- **Background Automation**: Android Foreground Service + Jetpack WorkManager fallback
- **Networking & Scraping**: OkHttp 4 (with parallel request dispatching), Jsoup, CookieJar session bridging
- **UI Framework**: Material Design 3, ViewBinding, SwipeRefreshLayout, CoordinatorLayout

---

## 📂 Project Structure

```text
├── app
│   ├── build.gradle.kts
│   └── src
│       └── main
│           ├── AndroidManifest.xml
│           ├── java/com/bahria/portal
│           │   ├── BahriaApplication.kt
│           │   ├── data
│           │   │   ├── local
│           │   │   │   ├── AppDatabase.kt
│           │   │   │   ├── AssignmentDao.kt
│           │   │   │   ├── AttendanceDao.kt
│           │   │   │   ├── NotificationDao.kt
│           │   │   │   └── PreferencesManager.kt
│           │   │   ├── model
│           │   │   │   ├── Assignment.kt
│           │   │   │   ├── CourseAttendance.kt
│           │   │   │   └── NotificationItem.kt
│           │   │   ├── network
│           │   │   │   ├── MockDataProvider.kt
│           │   │   │   ├── PortalAutomatorWebView.kt
│           │   │   │   └── PortalScraper.kt
│           │   │   └── repository
│           │   │       └── PortalRepository.kt
│           │   ├── notification
│           │   │   └── NotificationHelper.kt
│           │   ├── service
│           │   │   └── BahriaBackgroundService.kt
│           │   ├── ui
│           │   │   ├── MainActivity.kt
│           │   │   ├── assignments/
│           │   │   ├── attendance/
│           │   │   ├── calculator/
│           │   │   ├── login/
│           │   │   ├── notifications/
│           │   │   └── settings/
│           │   └── worker
│           │       ├── BootReceiver.kt
│           │       └── PortalSyncWorker.kt
│           └── res
│               ├── drawable/       # Custom app logo, status chips, card backgrounds
│               ├── layout/         # Material 3 XML layouts
│               ├── values/         # Dracula dark palette (#12131A), styles, colors
│               └── xml/            # Security network config
```

---

## 🏃‍♂️ How to Run

1. Open **Android Studio** and select the project directory.
2. Allow Gradle to sync.
3. Build and run on an Android device or emulator:
   ```bash
   ./gradlew assembleDebug
   ```
4. Install via ADB:
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

---

## 📄 License

This project is licensed under the permissive terms specified in the [LICENSE](file:///LICENSE) file.
Copyright (c) 2026 Sami.
