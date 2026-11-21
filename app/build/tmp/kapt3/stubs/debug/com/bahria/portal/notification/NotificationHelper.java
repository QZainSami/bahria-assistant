package com.bahria.portal.notification;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u0000 \u000e2\u00020\u0001:\u0001\u000eB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0007\u001a\u00020\bH\u0002J\u0016\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bJ\u0016\u0010\r\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/bahria/portal/notification/NotificationHelper;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "notificationManager", "Landroid/app/NotificationManager;", "createNotificationChannels", "", "showAssignmentNotification", "title", "", "message", "showAttendanceNotification", "Companion", "app_debug"})
public final class NotificationHelper {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_ASSIGNMENTS = "channel_assignments";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_ATTENDANCE = "channel_attendance";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_SYNC = "channel_sync";
    private static final int NOTIF_ID_ASSIGNMENT = 1001;
    private static final int NOTIF_ID_ATTENDANCE = 1002;
    @org.jetbrains.annotations.NotNull()
    private final android.app.NotificationManager notificationManager = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.bahria.portal.notification.NotificationHelper.Companion Companion = null;
    
    public NotificationHelper(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    private final void createNotificationChannels() {
    }
    
    public final void showAssignmentNotification(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String message) {
    }
    
    public final void showAttendanceNotification(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String message) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/bahria/portal/notification/NotificationHelper$Companion;", "", "()V", "CHANNEL_ASSIGNMENTS", "", "CHANNEL_ATTENDANCE", "CHANNEL_SYNC", "NOTIF_ID_ASSIGNMENT", "", "NOTIF_ID_ATTENDANCE", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}