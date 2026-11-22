# 🎓 Bahria Portal Companion

A modern, high-performance, and beautifully crafted Android companion application for **Bahria University** students to track **CMS Attendance** and **LMS Assignments** with instant background updates, proactive deadline alerts, offline caching, and direct document downloads.

---

## ✨ Features & Highlights

- **⚡ Instant Parallel Sync**: Concurrently fetches CMS attendance and LMS assignments across all registered courses in ~2–3 seconds using optimized asynchronous requests.
- **🎨 Sleek Dracula OLED Dark Theme**: Implements an ultra-deep dark palette (`#12131A`), refined typography, and a compact, clean interface.
- **🚨 Proactive Deadline Alerts**: Automated alerts for unsubmitted assignments due today (≤ 24 hours), due soon (24–48 hours), or when a deadline was missed.
- **🛡️ Persistent Background Service**: Dedicated Android Foreground Service that runs scheduled checks and displays an ongoing status notification: `"Bahria Assistant - Running in background • Last checked: [time]"`.
- **💾 Full Offline Persistence**: Powered by Android Jetpack Room Database (SQLite) so attendance records, assignment details, and notifications remain instantly accessible offline.
- **📥 Direct Document Downloader**: Download assignment problem files directly from the LMS in authentic `.docx` or `.pdf` formats that open seamlessly on any office suite or reader.
- **📊 Real-Time Attendance Intelligence**: Live calculation of Current Rate, Max Allowed Hours, Used Hours, and Hours Left with visual status bars and exam eligibility warnings.
- **🧪 One-Tap Demo Mode**: Full offline simulation with realistic university courses to test the app without needing portal credentials or active server availability.

---

## 📱 Detailed Capabilities

### 1. 📊 Smart Attendance Dashboard
- **Exact Calculation**: Displays Current Rate, Max Allowed Absence Hours, Used Hours, and Hours Left.
- **Dynamic Level Bar**: Seamless color indication reflecting attendance safety levels (Green for ≥ 75%, Red for < 75%).
- **Attendance Warnings**:
  - 🟢 **Safe (≥ 80%)**: Full exam clearance.
  - 🟡 **Warning (75% - 79.9%)**: Nearing threshold warning.
  - 🔴 **Critical (< 75%)**: Debarred status warning badge.
- **Filter Chips**: Filter by *All Courses*, *Critical (< 75%)*, or *Safe (≥ 80%)*.

### 2. 📝 LMS Assignments Hub & In-App Downloader
- **Direct Download Action**: Tap the download icon on any assignment card to immediately save the problem file to your phone's storage.
- **Document Integrity**: Saves authentic `.docx` and `.pdf` files, preventing corrupt downloads.
- **Intelligent Alert Engine**:
  - 🚨 **Due Today**: High-priority alert when an unsubmitted assignment is due within 24 hours.
  - ⏳ **Due Soon**: Reminder when an unsubmitted assignment is due within 48 hours.
  - ⚠️ **Deadline Passed**: Notification if an assignment deadline passed without submission.
- **Filter Chips**: Quickly switch between *All*, *Pending*, and *Submitted* assignments.

### 3. 🔄 Reliable Background Engine
- **Foreground Service**: Ensures background checks persist reliably across system battery optimizations.
- **Status Notification**: Shows a persistent notification in the status bar:
  > **Bahria Assistant**  
  > *Running in background • Last checked: [time] (Next in [X]m)*
- **Configurable Intervals**: Choose check frequency (15m, 30m, 1h, 3h, 6h) directly from Settings.
- **Boot Persistence**: Automatically restarts after device reboot.

---

## 🛠️ Architecture & Tech Stack

- **Language**: Kotlin 2.0+
- **Architecture**: MVVM + Repository Pattern + Single Source of Truth
- **Local Persistence**: Android Jetpack Room Database (SQLite)
- **Background Automation**: Android Foreground Service + Jetpack WorkManager
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
