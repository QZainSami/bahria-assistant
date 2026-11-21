package com.bahria.portal.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.bahria.portal.R
import com.bahria.portal.data.local.PreferencesManager
import com.bahria.portal.data.repository.PortalRepository
import com.bahria.portal.databinding.ActivityLoginBinding
import com.bahria.portal.ui.MainActivity
import com.bahria.portal.worker.PortalSyncWorker
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefs: PreferencesManager
    private lateinit var repository: PortalRepository

    private val campusList = listOf(
        "Karachi Campus",
        "Islamabad Campus (E-8)",
        "Islamabad Campus (H-11)",
        "Lahore Campus",
        "Bahria University Medical & Dental College"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.loginRoot) { _, windowInsets ->
            val insets = windowInsets.getInsets(
                WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars() or WindowInsetsCompat.Type.displayCutout()
            )
            binding.loginRoot.updatePadding(top = insets.top, bottom = insets.bottom)
            windowInsets
        }

        prefs = PreferencesManager(this)
        repository = PortalRepository.getInstance(this)

        setupCampusSpinner()
        populateExistingCredentials()
        setupListeners()
    }

    private fun setupCampusSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, campusList)
        binding.spinnerCampus.adapter = adapter

        val defaultIndex = campusList.indexOfFirst { it.contains(prefs.campus, ignoreCase = true) }
        if (defaultIndex >= 0) {
            binding.spinnerCampus.setSelection(defaultIndex)
        }
    }

    private fun populateExistingCredentials() {
        if (prefs.enrollment.isNotBlank()) {
            binding.etEnrollment.setText(prefs.enrollment)
        }
        if (prefs.password.isNotBlank() && prefs.rememberMe) {
            binding.etPassword.setText(prefs.password)
        }
        binding.cbRememberMe.isChecked = prefs.rememberMe
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val enrollment = binding.etEnrollment.text?.toString()?.trim() ?: ""
            val password = binding.etPassword.text?.toString()?.trim() ?: ""
            val campus = binding.spinnerCampus.selectedItem?.toString() ?: campusList[0]
            val remember = binding.cbRememberMe.isChecked

            if (enrollment.isBlank()) {
                binding.tilEnrollment.error = "Please enter your Enrollment ID"
                return@setOnClickListener
            }
            binding.tilEnrollment.error = null

            if (password.isBlank()) {
                binding.tilPassword.error = "Please enter your portal password"
                return@setOnClickListener
            }
            binding.tilPassword.error = null

            performLogin(enrollment, password, campus, remember)
        }

        binding.btnDemoMode.setOnClickListener {
            lifecycleScope.launch {
                binding.loginProgressBar.visibility = View.VISIBLE
                binding.btnLogin.isEnabled = false
                repository.loadDemoData()
                navigateToMain()
            }
        }
    }

    private fun performLogin(enrollment: String, pass: String, campus: String, remember: Boolean) {
        binding.loginProgressBar.visibility = View.VISIBLE
        binding.btnLogin.isEnabled = false
        binding.btnDemoMode.isEnabled = false

        lifecycleScope.launch {
            prefs.saveCredentials(enrollment, pass, campus, remember)
            val result = repository.refreshAll(isForce = true)

            binding.loginProgressBar.visibility = View.GONE
            binding.btnLogin.isEnabled = true
            binding.btnDemoMode.isEnabled = true

            if (result.isSuccess) {
                if (prefs.isAutoCheckEnabled) {
                    PortalSyncWorker.schedule(this@LoginActivity, prefs.checkIntervalMinutes.toLong())
                }
                navigateToMain()
            } else {
                val error = result.exceptionOrNull()?.message ?: "Login failed. Check your credentials or internet."
                Snackbar.make(binding.loginRoot, error, Snackbar.LENGTH_LONG)
                    .setAction("Use Demo") {
                        lifecycleScope.launch {
                            repository.loadDemoData()
                            navigateToMain()
                        }
                    }
                    .show()
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
