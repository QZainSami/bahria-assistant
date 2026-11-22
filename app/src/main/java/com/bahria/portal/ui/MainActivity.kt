package com.bahria.portal.ui

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bahria.portal.R
import com.bahria.portal.data.local.PreferencesManager
import com.bahria.portal.data.repository.PortalRepository
import com.bahria.portal.databinding.ActivityMainBinding
import com.bahria.portal.ui.assignments.AssignmentsFragment
import com.bahria.portal.ui.attendance.AttendanceFragment
import com.bahria.portal.ui.login.LoginActivity
import com.bahria.portal.ui.notifications.NotificationsFragment
import com.bahria.portal.ui.settings.SettingsFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: PreferencesManager
    private lateinit var repository: PortalRepository

    private val attendanceFragment by lazy { AttendanceFragment() }
    private val assignmentsFragment by lazy { AssignmentsFragment() }
    private val notificationsFragment by lazy { NotificationsFragment() }
    private val settingsFragment by lazy { SettingsFragment() }

    private var activeFragment: Fragment = attendanceFragment

    companion object {
        const val EXTRA_OPEN_TAB = "extra_open_tab"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = PreferencesManager(this)
        repository = PortalRepository.getInstance(this)

        if (!prefs.isLoggedIn && !prefs.isDemoMode) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.mainCoordinator) { _, windowInsets ->
            val statusBarInsets = windowInsets.getInsets(
                WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.displayCutout()
            )
            val navBarInsets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())

            binding.topAppBar.updatePadding(top = statusBarInsets.top)
            binding.bottomNavigation.updatePadding(bottom = navBarInsets.bottom)

            windowInsets
        }

        setupNavigation()
        setupFragments(savedInstanceState)
        handleIntent(intent)

        binding.btnQuickSync.setOnClickListener {
            performQuickSync()
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            } else if (prefs.isAutoCheckEnabled) {
                com.bahria.portal.service.BahriaBackgroundService.start(this)
            }
        } else if (prefs.isAutoCheckEnabled) {
            com.bahria.portal.service.BahriaBackgroundService.start(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            if (prefs.isAutoCheckEnabled) {
                com.bahria.portal.service.BahriaBackgroundService.start(this)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val targetTab = intent.getStringExtra(EXTRA_OPEN_TAB)
        when (targetTab) {
            "ASSIGNMENTS" -> {
                binding.bottomNavigation.selectedItemId = R.id.nav_assignments
            }
            "ATTENDANCE" -> {
                binding.bottomNavigation.selectedItemId = R.id.nav_attendance
            }
            "NOTIFICATIONS" -> {
                binding.bottomNavigation.selectedItemId = R.id.nav_notifications
            }
        }
    }

    private fun setupFragments(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, settingsFragment, "SETTINGS").hide(settingsFragment)
                .add(R.id.fragmentContainer, notificationsFragment, "NOTIFICATIONS").hide(notificationsFragment)
                .add(R.id.fragmentContainer, assignmentsFragment, "ASSIGNMENTS").hide(assignmentsFragment)
                .add(R.id.fragmentContainer, attendanceFragment, "ATTENDANCE")
                .commit()
            activeFragment = attendanceFragment
        }
    }

    private fun setupNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_attendance -> {
                    switchFragment(attendanceFragment)
                    true
                }
                R.id.nav_assignments -> {
                    switchFragment(assignmentsFragment)
                    true
                }
                R.id.nav_notifications -> {
                    switchFragment(notificationsFragment)
                    true
                }
                R.id.nav_settings -> {
                    switchFragment(settingsFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun switchFragment(target: Fragment) {
        if (activeFragment != target) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .hide(activeFragment)
                .show(target)
                .commit()
            activeFragment = target
        }
    }

    private fun performQuickSync() {
        val rotate = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 800
            repeatCount = 1
        }
        binding.btnQuickSync.startAnimation(rotate)

        lifecycleScope.launch {
            val res = repository.refreshAll(isForce = true)
            if (res.isSuccess) {
                Snackbar.make(binding.root, "Updated from portal!", Snackbar.LENGTH_SHORT).show()
            } else {
                val err = res.exceptionOrNull()?.message ?: "Sync failed"
                Snackbar.make(binding.root, err, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
