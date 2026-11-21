package com.bahria.portal.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006H\'J\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u000e\u0010\r\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004\u00a8\u0006\u000e"}, d2 = {"Lcom/bahria/portal/data/local/NotificationDao;", "", "deleteAll", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllNotifications", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/bahria/portal/data/model/NotificationItem;", "insert", "", "item", "(Lcom/bahria/portal/data/model/NotificationItem;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markAllAsRead", "app_debug"})
@androidx.room.Dao()
public abstract interface NotificationDao {
    
    @androidx.room.Query(value = "SELECT * FROM notifications ORDER BY timestamp DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.bahria.portal.data.model.NotificationItem>> getAllNotifications();
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.bahria.portal.data.model.NotificationItem item, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "DELETE FROM notifications")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE notifications SET isRead = 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object markAllAsRead(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}