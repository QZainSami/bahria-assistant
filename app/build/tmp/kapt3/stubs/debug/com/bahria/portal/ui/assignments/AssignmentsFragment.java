package com.bahria.portal.ui.assignments;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0017\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0002\u00a2\u0006\u0002\u0010\u0015J\b\u0010\u0016\u001a\u00020\u0012H\u0002J$\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J\b\u0010\u001f\u001a\u00020\u0012H\u0016J\u001a\u0010 \u001a\u00020\u00122\u0006\u0010!\u001a\u00020\u00182\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J\b\u0010\"\u001a\u00020\u0012H\u0002J\b\u0010#\u001a\u00020\u0012H\u0002J\b\u0010$\u001a\u00020\u0012H\u0002J\u0016\u0010%\u001a\u00020\u00122\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\'"}, d2 = {"Lcom/bahria/portal/ui/assignments/AssignmentsFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/bahria/portal/databinding/FragmentAssignmentsBinding;", "adapter", "Lcom/bahria/portal/ui/assignments/AssignmentsAdapter;", "allAssignments", "", "Lcom/bahria/portal/data/model/Assignment;", "binding", "getBinding", "()Lcom/bahria/portal/databinding/FragmentAssignmentsBinding;", "repository", "Lcom/bahria/portal/data/repository/PortalRepository;", "searchQuery", "", "filterAssignments", "", "chipId", "", "(Ljava/lang/Integer;)V", "observeData", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onViewCreated", "view", "refreshData", "setupListeners", "setupRecyclerView", "updateUI", "list", "app_debug"})
public final class AssignmentsFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.bahria.portal.databinding.FragmentAssignmentsBinding _binding;
    private com.bahria.portal.data.repository.PortalRepository repository;
    private com.bahria.portal.ui.assignments.AssignmentsAdapter adapter;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.bahria.portal.data.model.Assignment> allAssignments;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String searchQuery = "";
    
    public AssignmentsFragment() {
        super();
    }
    
    private final com.bahria.portal.databinding.FragmentAssignmentsBinding getBinding() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupRecyclerView() {
    }
    
    private final void setupListeners() {
    }
    
    private final void observeData() {
    }
    
    private final void updateUI(java.util.List<com.bahria.portal.data.model.Assignment> list) {
    }
    
    private final void filterAssignments(java.lang.Integer chipId) {
    }
    
    private final void refreshData() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}