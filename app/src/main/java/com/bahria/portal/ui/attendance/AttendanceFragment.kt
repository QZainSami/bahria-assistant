package com.bahria.portal.ui.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bahria.portal.R
import com.bahria.portal.data.local.PreferencesManager
import com.bahria.portal.data.model.CourseAttendance
import com.bahria.portal.data.repository.PortalRepository
import com.bahria.portal.databinding.FragmentAttendanceBinding
import com.bahria.portal.ui.calculator.BunkCalculatorBottomSheet
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AttendanceFragment : Fragment() {

    private var _binding: FragmentAttendanceBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: PortalRepository
    private lateinit var prefs: PreferencesManager
    private lateinit var adapter: AttendanceAdapter

    private var allCourses: List<CourseAttendance> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = PortalRepository.getInstance(requireContext())
        prefs = PreferencesManager(requireContext())

        setupRecyclerView()
        setupListeners()
        observeData()
    }

    private fun setupRecyclerView() {
        adapter = AttendanceAdapter { course ->
            val sheet = BunkCalculatorBottomSheet(allCourses, course.courseName)
            sheet.show(parentFragmentManager, "BunkCalcSheet")
        }
        binding.rvAttendance.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAttendance.adapter = adapter
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            refreshData()
        }

        binding.btnSyncAttendance.setOnClickListener {
            binding.swipeRefresh.isRefreshing = true
            viewLifecycleOwner.lifecycleScope.launch {
                val res = repository.refreshAttendance()
                binding.swipeRefresh.isRefreshing = false
                if (res.isSuccess) {
                    Snackbar.make(binding.root, "Attendance synced with CMS!", Snackbar.LENGTH_SHORT).show()
                } else {
                    val msg = res.exceptionOrNull()?.message ?: "Attendance sync failed."
                    Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnOpenPlanner.setOnClickListener {
            val sheet = BunkCalculatorBottomSheet(allCourses)
            sheet.show(parentFragmentManager, "BunkCalcSheet")
        }

        binding.chipGroupAttendance.setOnCheckedStateChangeListener { _, checkedIds ->
            filterCourses(checkedIds.firstOrNull())
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                repository.attendanceFlow.collect { courses ->
                    allCourses = courses
                    updateUI(courses)
                }
            }
        }
    }

    private fun updateUI(courses: List<CourseAttendance>) {
        if (courses.isEmpty()) {
            binding.emptyAttendanceView.visibility = View.VISIBLE
            binding.rvAttendance.visibility = View.GONE
            binding.tvHeroPercentage.text = "0.0%"
            binding.tvStatSafeCount.text = "0 Safe"
            binding.tvStatCriticalCount.text = "0 Critical"
            return
        }

        binding.emptyAttendanceView.visibility = View.GONE
        binding.rvAttendance.visibility = View.VISIBLE

        // Overall Weighted Attendance Calculation
        val totalAttended = courses.sumOf { it.present }
        val totalLectures = courses.sumOf { it.total }
        val overallPct = if (totalLectures > 0) (totalAttended / totalLectures) * 100.0 else 0.0

        binding.tvHeroPercentage.text = String.format("%.2f%%", overallPct)

        val criticalCount = courses.count { it.isCritical }
        val safeCount = courses.count { it.isSafe }

        binding.tvStatSafeCount.text = "$safeCount Safe (≥80%)"
        binding.tvStatCriticalCount.text = "$criticalCount Critical (<75%)"

        if (overallPct >= 80.0) {
            binding.tvHeroPercentage.setTextColor(ContextCompat.getColor(requireContext(), R.color.status_safe))
        } else if (overallPct >= 75.0) {
            binding.tvHeroPercentage.setTextColor(ContextCompat.getColor(requireContext(), R.color.status_warning))
        } else {
            binding.tvHeroPercentage.setTextColor(ContextCompat.getColor(requireContext(), R.color.status_critical))
        }

        val lastSync = prefs.lastSyncTimestamp
        if (lastSync > 0) {
            val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
            binding.tvLastSyncAttendance.text = "Last updated: ${sdf.format(Date(lastSync))}"
        } else {
            binding.tvLastSyncAttendance.text = "Cached on device"
        }

        filterCourses(binding.chipGroupAttendance.checkedChipId)
    }

    private fun filterCourses(chipId: Int?) {
        val filtered = when (chipId) {
            R.id.chipCritical -> allCourses.filter { it.isCritical }
            R.id.chipSafe -> allCourses.filter { it.isSafe }
            else -> allCourses
        }
        adapter.submitList(filtered)
    }

    private fun refreshData() {
        binding.swipeRefresh.isRefreshing = true
        viewLifecycleOwner.lifecycleScope.launch {
            val res = repository.refreshAll(isForce = true)
            binding.swipeRefresh.isRefreshing = false
            if (!res.isSuccess) {
                val msg = res.exceptionOrNull()?.message ?: "Sync failed."
                Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
