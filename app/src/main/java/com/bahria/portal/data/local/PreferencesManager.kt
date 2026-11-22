package com.bahria.portal.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "bahria_portal_prefs"
        private const val KEY_ENROLLMENT = "key_enrollment"
        private const val KEY_PASSWORD = "key_password"
        private const val KEY_CAMPUS = "key_campus"
        private const val KEY_USER_TYPE = "key_user_type"
        private const val KEY_IS_LOGGED_IN = "key_is_logged_in"
        private const val KEY_REMEMBER_ME = "key_remember_me"
        private const val KEY_AUTO_CHECK = "key_auto_check"
        private const val KEY_NOTIFICATIONS = "key_notifications"
        private const val KEY_CHECK_INTERVAL = "key_check_interval"
        private const val KEY_LAST_SYNC = "key_last_sync"
        private const val KEY_IS_DEMO = "key_is_demo"

        const val DEFAULT_CAMPUS = "Karachi Campus"
        const val DEFAULT_USER_TYPE = "Student"
        const val DEFAULT_INTERVAL_MINUTES = 30
    }

    var enrollment: String
        get() = prefs.getString(KEY_ENROLLMENT, "") ?: ""
        set(value) = prefs.edit().putString(KEY_ENROLLMENT, value).apply()

    var password: String
        get() = prefs.getString(KEY_PASSWORD, "") ?: ""
        set(value) = prefs.edit().putString(KEY_PASSWORD, value).apply()

    var campus: String
        get() = prefs.getString(KEY_CAMPUS, DEFAULT_CAMPUS) ?: DEFAULT_CAMPUS
        set(value) = prefs.edit().putString(KEY_CAMPUS, value).apply()

    var userType: String
        get() = prefs.getString(KEY_USER_TYPE, DEFAULT_USER_TYPE) ?: DEFAULT_USER_TYPE
        set(value) = prefs.edit().putString(KEY_USER_TYPE, value).apply()

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()

    var rememberMe: Boolean
        get() = prefs.getBoolean(KEY_REMEMBER_ME, true)
        set(value) = prefs.edit().putBoolean(KEY_REMEMBER_ME, value).apply()

    var isAutoCheckEnabled: Boolean
        get() = prefs.getBoolean(KEY_AUTO_CHECK, true)
        set(value) = prefs.edit().putBoolean(KEY_AUTO_CHECK, value).apply()

    var isNotificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS, true)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATIONS, value).apply()

    var checkIntervalMinutes: Int
        get() = prefs.getInt(KEY_CHECK_INTERVAL, DEFAULT_INTERVAL_MINUTES)
        set(value) = prefs.edit().putInt(KEY_CHECK_INTERVAL, value).apply()

    var lastSyncTimestamp: Long
        get() = prefs.getLong(KEY_LAST_SYNC, 0L)
        set(value) = prefs.edit().putLong(KEY_LAST_SYNC, value).apply()

    var isDemoMode: Boolean
        get() = prefs.getBoolean(KEY_IS_DEMO, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_DEMO, value).apply()

    fun saveCredentials(enrollment: String, pass: String, camp: String, remember: Boolean) {
        prefs.edit()
            .putString(KEY_ENROLLMENT, enrollment)
            .putString(KEY_PASSWORD, pass)
            .putString(KEY_CAMPUS, camp)
            .putBoolean(KEY_REMEMBER_ME, remember)
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putBoolean(KEY_IS_DEMO, false)
            .apply()
    }

    fun hasAlertBeenSent(key: String): Boolean {
        return prefs.getBoolean("alert_sent_$key", false)
    }

    fun markAlertSent(key: String) {
        prefs.edit().putBoolean("alert_sent_$key", true).apply()
    }

    fun logout() {
        val remember = rememberMe
        val savedEnrollment = if (remember) enrollment else ""
        val savedCampus = campus

        prefs.edit()
            .clear()
            .putBoolean(KEY_REMEMBER_ME, remember)
            .putString(KEY_ENROLLMENT, savedEnrollment)
            .putString(KEY_CAMPUS, savedCampus)
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .putBoolean(KEY_IS_DEMO, false)
            .apply()
    }
}
