package com.bahria.portal.data.network;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 #2\u00020\u0001:\u0001#B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u000e\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u000f\u001a\u00020\u000bH\u0002J\"\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00120\u0011H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0014\u0010\u0015J\"\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u00120\u0011H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0018\u0010\u0015J4\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00112\u0006\u0010\u001b\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u000b2\u0006\u0010\u001d\u001a\u00020\u000bH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001e\u0010\u001fJ\u0018\u0010 \u001a\u00020\u000b2\u0006\u0010\u001d\u001a\u00020\u000b2\u0006\u0010!\u001a\u00020\"H\u0002R\u001b\u0010\u0003\u001a\u00020\u00048BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R \u0010\t\u001a\u0014\u0012\u0004\u0012\u00020\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006$"}, d2 = {"Lcom/bahria/portal/data/network/PortalScraper;", "", "()V", "client", "Lokhttp3/OkHttpClient;", "getClient", "()Lokhttp3/OkHttpClient;", "client$delegate", "Lkotlin/Lazy;", "cookieStore", "Ljava/util/HashMap;", "", "", "Lokhttp3/Cookie;", "extractErrorMessage", "html", "fetchAssignments", "Lkotlin/Result;", "", "Lcom/bahria/portal/data/model/Assignment;", "fetchAssignments-IoAF18A", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchAttendance", "Lcom/bahria/portal/data/model/CourseAttendance;", "fetchAttendance-IoAF18A", "login", "", "enrollment", "pass", "campus", "login-BWLJW6A", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "resolveInstituteCode", "doc", "Lorg/jsoup/nodes/Document;", "Companion", "app_debug"})
public final class PortalScraper {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "PortalScraper";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CMS_LOGIN_URL = "https://cms.bahria.edu.pk/Logins/Student/Login.aspx";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CMS_ATTENDANCE_URL = "https://cms.bahria.edu.pk/Sys/Student/ClassAttendance/StudentWiseAttendance.aspx";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CMS_GOTO_LMS_URL = "https://cms.bahria.edu.pk/Sys/Common/GoToLMS.aspx";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LMS_ASSIGNMENTS_URL = "https://lms.bahria.edu.pk/Student/Assignments.php";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36";
    @org.jetbrains.annotations.NotNull()
    private final java.util.HashMap<java.lang.String, java.util.List<okhttp3.Cookie>> cookieStore = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy client$delegate = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.bahria.portal.data.network.PortalScraper.Companion Companion = null;
    
    public PortalScraper() {
        super();
    }
    
    private final okhttp3.OkHttpClient getClient() {
        return null;
    }
    
    private final java.lang.String resolveInstituteCode(java.lang.String campus, org.jsoup.nodes.Document doc) {
        return null;
    }
    
    private final java.lang.String extractErrorMessage(java.lang.String html) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/bahria/portal/data/network/PortalScraper$Companion;", "", "()V", "CMS_ATTENDANCE_URL", "", "CMS_GOTO_LMS_URL", "CMS_LOGIN_URL", "LMS_ASSIGNMENTS_URL", "TAG", "USER_AGENT", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}