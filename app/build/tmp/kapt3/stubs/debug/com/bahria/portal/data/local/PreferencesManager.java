package com.bahria.portal.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u0000 72\u00020\u0001:\u00017B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u00101\u001a\u000202J&\u00103\u001a\u0002022\u0006\u0010\u0012\u001a\u00020\u00062\u0006\u00104\u001a\u00020\u00062\u0006\u00105\u001a\u00020\u00062\u0006\u00106\u001a\u00020\u0015R$\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR$\u0010\r\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R$\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0013\u0010\t\"\u0004\b\u0014\u0010\u000bR$\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0005\u001a\u00020\u00158F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R$\u0010\u001a\u001a\u00020\u00152\u0006\u0010\u0005\u001a\u00020\u00158F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u001a\u0010\u0017\"\u0004\b\u001b\u0010\u0019R$\u0010\u001c\u001a\u00020\u00152\u0006\u0010\u0005\u001a\u00020\u00158F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u001c\u0010\u0017\"\u0004\b\u001d\u0010\u0019R$\u0010\u001e\u001a\u00020\u00152\u0006\u0010\u0005\u001a\u00020\u00158F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u001e\u0010\u0017\"\u0004\b\u001f\u0010\u0019R$\u0010!\u001a\u00020 2\u0006\u0010\u0005\u001a\u00020 8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R$\u0010&\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\'\u0010\t\"\u0004\b(\u0010\u000bR\u000e\u0010)\u001a\u00020*X\u0082\u0004\u00a2\u0006\u0002\n\u0000R$\u0010+\u001a\u00020\u00152\u0006\u0010\u0005\u001a\u00020\u00158F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b,\u0010\u0017\"\u0004\b-\u0010\u0019R$\u0010.\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b/\u0010\t\"\u0004\b0\u0010\u000b\u00a8\u00068"}, d2 = {"Lcom/bahria/portal/data/local/PreferencesManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "value", "", "campus", "getCampus", "()Ljava/lang/String;", "setCampus", "(Ljava/lang/String;)V", "", "checkIntervalMinutes", "getCheckIntervalMinutes", "()I", "setCheckIntervalMinutes", "(I)V", "enrollment", "getEnrollment", "setEnrollment", "", "isAutoCheckEnabled", "()Z", "setAutoCheckEnabled", "(Z)V", "isDemoMode", "setDemoMode", "isLoggedIn", "setLoggedIn", "isNotificationsEnabled", "setNotificationsEnabled", "", "lastSyncTimestamp", "getLastSyncTimestamp", "()J", "setLastSyncTimestamp", "(J)V", "password", "getPassword", "setPassword", "prefs", "Landroid/content/SharedPreferences;", "rememberMe", "getRememberMe", "setRememberMe", "userType", "getUserType", "setUserType", "logout", "", "saveCredentials", "pass", "camp", "remember", "Companion", "app_debug"})
public final class PreferencesManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.SharedPreferences prefs = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREF_NAME = "bahria_portal_prefs";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_ENROLLMENT = "key_enrollment";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_PASSWORD = "key_password";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_CAMPUS = "key_campus";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_USER_TYPE = "key_user_type";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_IS_LOGGED_IN = "key_is_logged_in";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_REMEMBER_ME = "key_remember_me";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_AUTO_CHECK = "key_auto_check";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_NOTIFICATIONS = "key_notifications";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_CHECK_INTERVAL = "key_check_interval";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_LAST_SYNC = "key_last_sync";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_IS_DEMO = "key_is_demo";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DEFAULT_CAMPUS = "Karachi Campus";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String DEFAULT_USER_TYPE = "Student";
    public static final int DEFAULT_INTERVAL_MINUTES = 30;
    @org.jetbrains.annotations.NotNull()
    public static final com.bahria.portal.data.local.PreferencesManager.Companion Companion = null;
    
    public PreferencesManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEnrollment() {
        return null;
    }
    
    public final void setEnrollment(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPassword() {
        return null;
    }
    
    public final void setPassword(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCampus() {
        return null;
    }
    
    public final void setCampus(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUserType() {
        return null;
    }
    
    public final void setUserType(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final boolean isLoggedIn() {
        return false;
    }
    
    public final void setLoggedIn(boolean value) {
    }
    
    public final boolean getRememberMe() {
        return false;
    }
    
    public final void setRememberMe(boolean value) {
    }
    
    public final boolean isAutoCheckEnabled() {
        return false;
    }
    
    public final void setAutoCheckEnabled(boolean value) {
    }
    
    public final boolean isNotificationsEnabled() {
        return false;
    }
    
    public final void setNotificationsEnabled(boolean value) {
    }
    
    public final int getCheckIntervalMinutes() {
        return 0;
    }
    
    public final void setCheckIntervalMinutes(int value) {
    }
    
    public final long getLastSyncTimestamp() {
        return 0L;
    }
    
    public final void setLastSyncTimestamp(long value) {
    }
    
    public final boolean isDemoMode() {
        return false;
    }
    
    public final void setDemoMode(boolean value) {
    }
    
    public final void saveCredentials(@org.jetbrains.annotations.NotNull()
    java.lang.String enrollment, @org.jetbrains.annotations.NotNull()
    java.lang.String pass, @org.jetbrains.annotations.NotNull()
    java.lang.String camp, boolean remember) {
    }
    
    public final void logout() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u000e\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/bahria/portal/data/local/PreferencesManager$Companion;", "", "()V", "DEFAULT_CAMPUS", "", "DEFAULT_INTERVAL_MINUTES", "", "DEFAULT_USER_TYPE", "KEY_AUTO_CHECK", "KEY_CAMPUS", "KEY_CHECK_INTERVAL", "KEY_ENROLLMENT", "KEY_IS_DEMO", "KEY_IS_LOGGED_IN", "KEY_LAST_SYNC", "KEY_NOTIFICATIONS", "KEY_PASSWORD", "KEY_REMEMBER_ME", "KEY_USER_TYPE", "PREF_NAME", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}