package com.bahria.portal.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000x\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u0000 32\u00020\u0001:\u00013B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010#\u001a\u00020$H\u0086@\u00a2\u0006\u0002\u0010%J\u000e\u0010&\u001a\u00020$H\u0086@\u00a2\u0006\u0002\u0010%J*\u0010\'\u001a\u00020$2\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00100\t2\f\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00100\tH\u0082@\u00a2\u0006\u0002\u0010*J*\u0010+\u001a\u00020$2\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\n0\t2\f\u0010)\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u0082@\u00a2\u0006\u0002\u0010*J\u000e\u0010,\u001a\u00020$H\u0086@\u00a2\u0006\u0002\u0010%J&\u0010-\u001a\b\u0012\u0004\u0012\u00020$0.2\b\b\u0002\u0010/\u001a\u000200H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b1\u00102R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\fR\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001b0\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\fR\u001d\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\fR\u000e\u0010\u001f\u001a\u00020 X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u00064"}, d2 = {"Lcom/bahria/portal/data/repository/PortalRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "assignmentDao", "Lcom/bahria/portal/data/local/AssignmentDao;", "assignmentsFlow", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/bahria/portal/data/model/Assignment;", "getAssignmentsFlow", "()Lkotlinx/coroutines/flow/Flow;", "attendanceDao", "Lcom/bahria/portal/data/local/AttendanceDao;", "attendanceFlow", "Lcom/bahria/portal/data/model/CourseAttendance;", "getAttendanceFlow", "automator", "Lcom/bahria/portal/data/network/PortalAutomatorWebView;", "db", "Lcom/bahria/portal/data/local/AppDatabase;", "notificationDao", "Lcom/bahria/portal/data/local/NotificationDao;", "notificationHelper", "Lcom/bahria/portal/notification/NotificationHelper;", "notificationsFlow", "Lcom/bahria/portal/data/model/NotificationItem;", "getNotificationsFlow", "pendingAssignmentsFlow", "getPendingAssignmentsFlow", "prefs", "Lcom/bahria/portal/data/local/PreferencesManager;", "scraper", "Lcom/bahria/portal/data/network/PortalScraper;", "clearAllNotifications", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearCache", "detectAttendanceChanges", "oldList", "newList", "(Ljava/util/List;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "detectNewAssignments", "loadDemoData", "refreshAll", "Lkotlin/Result;", "isForce", "", "refreshAll-gIAlu-s", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class PortalRepository {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.bahria.portal.data.local.AppDatabase db = null;
    @org.jetbrains.annotations.NotNull()
    private final com.bahria.portal.data.local.AttendanceDao attendanceDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.bahria.portal.data.local.AssignmentDao assignmentDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.bahria.portal.data.local.NotificationDao notificationDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.bahria.portal.data.local.PreferencesManager prefs = null;
    @org.jetbrains.annotations.NotNull()
    private final com.bahria.portal.data.network.PortalScraper scraper = null;
    @org.jetbrains.annotations.NotNull()
    private final com.bahria.portal.data.network.PortalAutomatorWebView automator = null;
    @org.jetbrains.annotations.NotNull()
    private final com.bahria.portal.notification.NotificationHelper notificationHelper = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "PortalRepository";
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.bahria.portal.data.repository.PortalRepository INSTANCE;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.bahria.portal.data.model.CourseAttendance>> attendanceFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.bahria.portal.data.model.Assignment>> assignmentsFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.bahria.portal.data.model.Assignment>> pendingAssignmentsFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.bahria.portal.data.model.NotificationItem>> notificationsFlow = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.bahria.portal.data.repository.PortalRepository.Companion Companion = null;
    
    public PortalRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.bahria.portal.data.model.CourseAttendance>> getAttendanceFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.bahria.portal.data.model.Assignment>> getAssignmentsFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.bahria.portal.data.model.Assignment>> getPendingAssignmentsFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.bahria.portal.data.model.NotificationItem>> getNotificationsFlow() {
        return null;
    }
    
    private final java.lang.Object detectAttendanceChanges(java.util.List<com.bahria.portal.data.model.CourseAttendance> oldList, java.util.List<com.bahria.portal.data.model.CourseAttendance> newList, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object detectNewAssignments(java.util.List<com.bahria.portal.data.model.Assignment> oldList, java.util.List<com.bahria.portal.data.model.Assignment> newList, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object clearAllNotifications(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object clearCache(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object loadDemoData(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/bahria/portal/data/repository/PortalRepository$Companion;", "", "()V", "INSTANCE", "Lcom/bahria/portal/data/repository/PortalRepository;", "TAG", "", "getInstance", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bahria.portal.data.repository.PortalRepository getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}