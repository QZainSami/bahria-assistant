package com.bahria.portal.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 -2\u00020\u0001:\u0001-B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020#H\u0002J\u0012\u0010$\u001a\u00020!2\b\u0010%\u001a\u0004\u0018\u00010&H\u0014J\u0010\u0010\'\u001a\u00020!2\u0006\u0010\"\u001a\u00020#H\u0014J\b\u0010(\u001a\u00020!H\u0002J\u0012\u0010)\u001a\u00020!2\b\u0010%\u001a\u0004\u0018\u00010&H\u0002J\b\u0010*\u001a\u00020!H\u0002J\u0010\u0010+\u001a\u00020!2\u0006\u0010,\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u001b\u0010\u000b\u001a\u00020\f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000f\u0010\n\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0012\u001a\u00020\u00138BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0016\u0010\n\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u001b\u001a\u00020\u001c8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001f\u0010\n\u001a\u0004\b\u001d\u0010\u001e\u00a8\u0006."}, d2 = {"Lcom/bahria/portal/ui/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "activeFragment", "Landroidx/fragment/app/Fragment;", "assignmentsFragment", "Lcom/bahria/portal/ui/assignments/AssignmentsFragment;", "getAssignmentsFragment", "()Lcom/bahria/portal/ui/assignments/AssignmentsFragment;", "assignmentsFragment$delegate", "Lkotlin/Lazy;", "attendanceFragment", "Lcom/bahria/portal/ui/attendance/AttendanceFragment;", "getAttendanceFragment", "()Lcom/bahria/portal/ui/attendance/AttendanceFragment;", "attendanceFragment$delegate", "binding", "Lcom/bahria/portal/databinding/ActivityMainBinding;", "notificationsFragment", "Lcom/bahria/portal/ui/notifications/NotificationsFragment;", "getNotificationsFragment", "()Lcom/bahria/portal/ui/notifications/NotificationsFragment;", "notificationsFragment$delegate", "prefs", "Lcom/bahria/portal/data/local/PreferencesManager;", "repository", "Lcom/bahria/portal/data/repository/PortalRepository;", "settingsFragment", "Lcom/bahria/portal/ui/settings/SettingsFragment;", "getSettingsFragment", "()Lcom/bahria/portal/ui/settings/SettingsFragment;", "settingsFragment$delegate", "handleIntent", "", "intent", "Landroid/content/Intent;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onNewIntent", "performQuickSync", "setupFragments", "setupNavigation", "switchFragment", "target", "Companion", "app_debug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.bahria.portal.databinding.ActivityMainBinding binding;
    private com.bahria.portal.data.local.PreferencesManager prefs;
    private com.bahria.portal.data.repository.PortalRepository repository;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy attendanceFragment$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy assignmentsFragment$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy notificationsFragment$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy settingsFragment$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private androidx.fragment.app.Fragment activeFragment;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_OPEN_TAB = "extra_open_tab";
    @org.jetbrains.annotations.NotNull()
    public static final com.bahria.portal.ui.MainActivity.Companion Companion = null;
    
    public MainActivity() {
        super();
    }
    
    private final com.bahria.portal.ui.attendance.AttendanceFragment getAttendanceFragment() {
        return null;
    }
    
    private final com.bahria.portal.ui.assignments.AssignmentsFragment getAssignmentsFragment() {
        return null;
    }
    
    private final com.bahria.portal.ui.notifications.NotificationsFragment getNotificationsFragment() {
        return null;
    }
    
    private final com.bahria.portal.ui.settings.SettingsFragment getSettingsFragment() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onNewIntent(@org.jetbrains.annotations.NotNull()
    android.content.Intent intent) {
    }
    
    private final void handleIntent(android.content.Intent intent) {
    }
    
    private final void setupFragments(android.os.Bundle savedInstanceState) {
    }
    
    private final void setupNavigation() {
    }
    
    private final void switchFragment(androidx.fragment.app.Fragment target) {
    }
    
    private final void performQuickSync() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/bahria/portal/ui/MainActivity$Companion;", "", "()V", "EXTRA_OPEN_TAB", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}