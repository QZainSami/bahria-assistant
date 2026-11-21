package com.bahria.portal.ui.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            // Optional: tapping opens details sheet
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
                    Snackbar.make(binding.root, "Attendance synced from CMS!", Snackbar.LENGTH_SHORT).show()
                } else {
                    val msg = res.exceptionOrNull()?.message ?: "Attendance sync failed."
                    Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
                }
            }
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
            binding.tvAttendanceSubtitle.text = "No records cached"
            return
        }

        binding.emptyAttendanceView.visibility = View.GONE
        binding.rvAttendance.visibility = View.VISIBLE

        val lastSync = prefs.lastSyncTimestamp
        val timeStr = if (lastSync > 0) {
            val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
            "Synced ${sdf.format(Date(lastSync))}"
        } else {
            "Cached on device"
        }
        binding.tvAttendanceSubtitle.text = "${courses.size} courses enrolled · $timeStr"

        filterCourses(binding.chipGroupAttendance.checkedChipId)
    }

    private fun filterCourses(chipId: Int?) {
        val filtered = when (chipId) {
            R.id.chipCritical -> allCourses.filter { it.isCritical }
            R.id.chipSafe -> allCourses.filter { !it.isCritical }
            else -> allCourses
        }
        adapter.submitList(filtered)
    }

    private fun refreshData() {
        binding.swipeRefresh.isRefreshing = true
        viewLifecycleOwner.lifecycleScope.launch {
            val res = repository.refreshAttendance()
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
