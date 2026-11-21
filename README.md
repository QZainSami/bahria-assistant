# 🎓 Bahria Portal Companion (BU Portal Pro)

A modern, high-performance, and beautifully crafted Android companion application for **Bahria University** students to track **CMS Attendance** and **LMS Assignments** with live background updates, zero data loss, and an interactive bunk planner.

---

## 🚀 Key Improvements Over the Senior's App

| Feature | The Old App | Bahria Portal Companion (This App) |
| :--- | :--- | :--- |
| **Look & Feel** | Amateurish, harsh solid colors, no animations, cramped tables | Sleek Material 3 Midnight OLED UI, custom card strokes, fluid transitions, and status-coded rings |
| **Persistence & State** | ❌ Switching tabs wiped attendance data completely | ✅ **Room SQLite Local Database**: Attendance & assignments persist permanently across tabs and app restarts |
| **Offline Experience** | ❌ Blank screens when offline or switching tabs | ✅ Instant offline loading with clear "Last updated: X mins ago" timestamp |
| **Background Sync & Alerts** | Prone to being killed by OS battery savers | ✅ **Android WorkManager** + OEM battery optimization helper + rich Android notification channels |
| **Credential Security** | Plain text storage | ✅ Encrypted local storage with Remember Me & Auto-Login |
| **Scraping Engine** | Fragile direct scraper that breaks on ViewState/redirect changes | ✅ **Dual-Engine**: High-speed OkHttp/Jsoup scraper + automated headless in-app WebView fallback |
| **Bunk & Goal Planner** | ❌ Not available | ✅ **Interactive Bunk Calculator**: Tells you exactly how many classes you can skip or need to attend to keep ≥ 75% |
| **Demo Mode** | ❌ Not available | ✅ One-tap demo mode with realistic semester courses to test without portal downtime |

---

## 📱 Core Features

### 1. 📊 Smart Attendance Dashboard
- **Overall Attendance Gauge**: Instant high-level percentage indicator.
- **Course Cards**: Breakdown of **Present**, **Absent**, and **Total** lectures with visual progress bars.
- **Threshold Coding**:
  - 🟢 **Safe (≥ 80%)**: Attendance is in the clear.
  - 🟡 **Warning (75% - 79.9%)**: Close to the threshold.
  - 🔴 **Critical (< 75%)**: Debarred from exams warning badge.
- **Bunk Advisor**: "Safe to bunk 2 classes" or "🚨 Attend next 5 classes to reach 75%".
- **Filter Chips**: Filter by *All Courses*, *Critical (< 75%)*, or *Safe (≥ 80%)*.

### 2. 📝 LMS Assignments Hub
- **Status & Urgency Badges**:
  - 🚨 `< 24 Hours`: Red countdown badge.
  - 🟡 `1 - 2 Days`: Amber alert badge.
  - 🟢 `3+ Days`: Green calm badge.
  - ✅ `Submitted` / `Graded`: Clean state indicator.
- **Live Search & Filter**: Instant search by course name or assignment title; filter by *Pending*, *Submitted*, or *All*.

### 3. 🔔 Live Notifications & Alerts
- Real-time diff detection engine runs in the background.
- Whenever an absence is recorded on CMS:
  > *⚠️ Attendance Alert: You were marked absent in Computer Architecture! Current: 60.00%*
- Whenever an instructor uploads a new assignment on LMS:
  > *📝 New Assignment: Complex Computing Problem (Design & Analysis of Algorithms) • Due: 15 Dec*
- Notification Center in-app tab with a timestamped history and "Clear All" action.

### 4. 🧮 Interactive Bunk & Attendance Goal Planner
- Bottom sheet tool accessible from the top toolbar or any course card.
- Select any course or type custom values.
- Real-time mathematics:
  $$\text{Safe Bunks} = \left\lfloor \frac{\text{Present}}{0.75} - \text{Total} \right\rfloor$$
  $$\text{Classes Needed for 75\%} = \left\lceil 3 \times \text{Total} - 4 \times \text{Present} \right\rceil$$

### 5. ⚙️ Settings & Device Optimization
- Configurable auto-check frequency: 15 min, 30 min, 1 hour, 3 hours.
- Direct deep-link button to disable Android battery optimization so notifications are never delayed.
- Safe logout and local cache cleaner.

---

## 🛠️ Architecture & Tech Stack

- **Language**: Kotlin 2.0+
- **Architecture**: MVVM + Repository Pattern + Single Source of Truth
- **Local Persistence**: Android Jetpack Room Database (SQLite)
- **Background Automation**: Android Jetpack WorkManager
- **Networking & Scraping**: OkHttp 4, JavaNetCookieJar, Jsoup 1.17+, Android Headless WebView Bridge
- **UI Framework**: Material Design 3, ViewBinding, SwipeRefreshLayout, CoordinatorLayout

---

## 📂 Project Structure

```text
f:\Projects\Uni App
├── app
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
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
│           │   ├── ui
│           │   │   ├── MainActivity.kt
│           │   │   ├── assignments
│           │   │   │   ├── AssignmentsAdapter.kt
│           │   │   │   └── AssignmentsFragment.kt
│           │   │   ├── attendance
│           │   │   │   ├── AttendanceAdapter.kt
│           │   │   │   └── AttendanceFragment.kt
│           │   │   ├── calculator
│           │   │   │   └── BunkCalculatorBottomSheet.kt
│           │   │   ├── login
│           │   │   │   └── LoginActivity.kt
│           │   │   ├── notifications
│           │   │   │   ├── NotificationsAdapter.kt
│           │   │   │   └── NotificationsFragment.kt
│           │   │   └── settings
│           │   │       └── SettingsFragment.kt
│           │   └── worker
│           │       ├── BootReceiver.kt
│           │       └── PortalSyncWorker.kt
│           └── res
│               ├── drawable/       # Custom icons, cards & status badges
│               ├── layout/         # Material 3 XML layouts
│               ├── menu/           # Bottom navigation menu
│               ├── values/         # Midnight theme colors, strings, themes
│               └── xml/            # Network security & backup configs
├── build.gradle.kts
├── gradle.properties
├── gradlew.bat
├── local.properties
└── settings.gradle.kts
```

---

## 🏃‍♂️ How to Run

1. Open **Android Studio** (or IntelliJ with Android plugin).
2. Choose **File > Open** and select `f:\Projects\Uni App`.
3. Allow Gradle to sync.
4. Click **Run > Run 'app'** (or press `Shift + F10`) with an Android phone or emulator connected.
5. On the login screen:
   - Enter your **Enrollment ID** (e.g. `02-134242-111`), your **Password**, and choose your **Campus**.
   - Or tap **"Explore Demo Mode"** to immediately tour the full app with sample courses!
