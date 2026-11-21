package com.bahria.portal.data.network;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0002\u0015\u0016B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J>\u0010\t\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u000b0\n2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u000fH\u0087@\u00a2\u0006\u0002\u0010\u0012J\b\u0010\u0013\u001a\u00020\u0014H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/bahria/portal/data/network/PortalAutomatorWebView;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "mainHandler", "Landroid/os/Handler;", "webView", "Landroid/webkit/WebView;", "automateFetch", "Lkotlin/Pair;", "", "Lcom/bahria/portal/data/model/CourseAttendance;", "Lcom/bahria/portal/data/model/Assignment;", "enrollment", "", "password", "campus", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "cleanup", "", "JSBridge", "ScrapingCallback", "app_debug"})
public final class PortalAutomatorWebView {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.Nullable()
    private android.webkit.WebView webView;
    @org.jetbrains.annotations.NotNull()
    private final android.os.Handler mainHandler = null;
    
    public PortalAutomatorWebView(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @android.annotation.SuppressLint(value = {"SetJavaScriptEnabled"})
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object automateFetch(@org.jetbrains.annotations.NotNull()
    java.lang.String enrollment, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    java.lang.String campus, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Pair<? extends java.util.List<com.bahria.portal.data.model.CourseAttendance>, ? extends java.util.List<com.bahria.portal.data.model.Assignment>>> $completion) {
        return null;
    }
    
    private final void cleanup() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001BA\u0012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003\u0012\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003\u00a2\u0006\u0002\u0010\bJ\u0010\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u0004H\u0007J\u0010\u0010\u000b\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u0004H\u0007J\u0010\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u0004H\u0007R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/bahria/portal/data/network/PortalAutomatorWebView$JSBridge;", "", "onAttendance", "Lkotlin/Function1;", "", "", "onAssignments", "onError", "(Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "onAssignmentsParsed", "json", "onAttendanceParsed", "onLoginError", "msg", "app_debug"})
    public static final class JSBridge {
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function1<java.lang.String, kotlin.Unit> onAttendance = null;
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function1<java.lang.String, kotlin.Unit> onAssignments = null;
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function1<java.lang.String, kotlin.Unit> onError = null;
        
        public JSBridge(@org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onAttendance, @org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onAssignments, @org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onError) {
            super();
        }
        
        @android.webkit.JavascriptInterface()
        public final void onAttendanceParsed(@org.jetbrains.annotations.NotNull()
        java.lang.String json) {
        }
        
        @android.webkit.JavascriptInterface()
        public final void onAssignmentsParsed(@org.jetbrains.annotations.NotNull()
        java.lang.String json) {
        }
        
        @android.webkit.JavascriptInterface()
        public final void onLoginError(@org.jetbrains.annotations.NotNull()
        java.lang.String msg) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J$\u0010\u0006\u001a\u00020\u00032\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\bH&\u00a8\u0006\f"}, d2 = {"Lcom/bahria/portal/data/network/PortalAutomatorWebView$ScrapingCallback;", "", "onError", "", "error", "", "onSuccess", "attendance", "", "Lcom/bahria/portal/data/model/CourseAttendance;", "assignments", "Lcom/bahria/portal/data/model/Assignment;", "app_debug"})
    public static abstract interface ScrapingCallback {
        
        public abstract void onSuccess(@org.jetbrains.annotations.NotNull()
        java.util.List<com.bahria.portal.data.model.CourseAttendance> attendance, @org.jetbrains.annotations.NotNull()
        java.util.List<com.bahria.portal.data.model.Assignment> assignments);
        
        public abstract void onError(@org.jetbrains.annotations.NotNull()
        java.lang.String error);
    }
}