package com.bahria.portal.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bahria.portal.R
import com.bahria.portal.data.local.PreferencesManager
import com.bahria.portal.data.repository.PortalRepository
import com.bahria.portal.databinding.FragmentSettingsBinding
import com.bahria.portal.ui.login.LoginActivity
import com.bahria.portal.worker.PortalSyncWorker
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefs: PreferencesManager
    private lateinit var repository: PortalRepository

    private val intervals = listOf(
        Pair("15 minutes (Frequent)", 15),
        Pair("30 minutes (Recommended)", 30),
        Pair("1 hour", 60),
        Pair("3 hours", 180),
        Pair("6 hours", 360)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = PreferencesManager(requireContext())
        repository = PortalRepository.getInstance(requireContext())

        setupProfile()
        setupIntervalSpinner()
        setupSwitches()
        setupButtons()
    }

    private fun setupProfile() {
        val enrollment = if (prefs.isDemoMode) "DEMO-STUDENT-01" else prefs.enrollment
        val campus = if (prefs.isDemoMode) "Karachi Campus (Demo Mode)" else prefs.campus

        binding.tvSettingsEnrollment.text = enrollment.ifBlank { "Not Logged In" }
        binding.tvSettingsCampus.text = campus
    }

    private fun setupIntervalSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            intervals.map { it.first }
        )
        binding.spinnerInterval.adapter = adapter

        val currentInterval = prefs.checkIntervalMinutes
        val selectedIdx = intervals.indexOfFirst { it.second == currentInterval }.coerceAtLeast(1)
        binding.spinnerInterval.setSelection(selectedIdx)

        binding.spinnerInterval.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val newInterval = intervals[position].second
                if (prefs.checkIntervalMinutes != newInterval) {
                    prefs.checkIntervalMinutes = newInterval
                    if (prefs.isAutoCheckEnabled) {
                        PortalSyncWorker.schedule(requireContext(), newInterval.toLong())
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupSwitches() {
        binding.switchAutoCheck.isChecked = prefs.isAutoCheckEnabled
        binding.switchNotifications.isChecked = prefs.isNotificationsEnabled

        binding.switchAutoCheck.setOnCheckedChangeListener { _, isChecked ->
            prefs.isAutoCheckEnabled = isChecked
            if (isChecked) {
                PortalSyncWorker.schedule(requireContext(), prefs.checkIntervalMinutes.toLong())
                Snackbar.make(binding.root, "Background checking activated", Snackbar.LENGTH_SHORT).show()
            } else {
                PortalSyncWorker.cancel(requireContext())
                Snackbar.make(binding.root, "Background checking disabled", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            prefs.isNotificationsEnabled = isChecked
        }
    }

    @SuppressLint("BatteryLife")
    private fun setupButtons() {
        binding.btnBatterySettings.setOnClickListener {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                        data = Uri.parse("package:${requireContext().packageName}")
                    }
                    startActivity(intent)
                } else {
                    val intent = Intent(Settings.ACTION_SETTINGS)
                    startActivity(intent)
                }
            } catch (_: Exception) {
                val intent = Intent(Settings.ACTION_SETTINGS)
                startActivity(intent)
            }
        }

        binding.btnForceSync.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                Snackbar.make(binding.root, "Syncing portal data...", Snackbar.LENGTH_SHORT).show()
                val res = repository.refreshAll(isForce = true)
                if (res.isSuccess) {
                    Snackbar.make(binding.root, "Data refreshed successfully!", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(binding.root, "Sync error: ${res.exceptionOrNull()?.message}", Snackbar.LENGTH_LONG).show()
                }
            }
        }

        binding.btnClearCache.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Clear Local Data")
                .setMessage("Are you sure? This will remove cached attendance and assignments from your phone.")
                .setPositiveButton("Clear") { _, _ ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        repository.clearCache()
                        Snackbar.make(binding.root, "Cache cleared.", Snackbar.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Log Out")
                .setMessage("Do you want to log out of your Bahria Portal account?")
                .setPositiveButton("Log Out") { _, _ ->
                    prefs.logout()
                    PortalSyncWorker.cancel(requireContext())
                    val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    requireActivity().finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
