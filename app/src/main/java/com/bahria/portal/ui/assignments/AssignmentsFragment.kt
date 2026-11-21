package com.bahria.portal.ui.assignments

import android.content.Intent
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bahria.portal.R
import com.bahria.portal.data.model.Assignment
import com.bahria.portal.data.repository.PortalRepository
import com.bahria.portal.databinding.FragmentAssignmentsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.io.File

class AssignmentsFragment : Fragment() {

    private var _binding: FragmentAssignmentsBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: PortalRepository
    private lateinit var adapter: AssignmentsAdapter

    private var allAssignments: List<Assignment> = emptyList()
    private var searchQuery = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssignmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = PortalRepository.getInstance(requireContext())

        setupRecyclerView()
        setupListeners()
        observeData()
    }

    private fun setupRecyclerView() {
        adapter = AssignmentsAdapter { assignment ->
            downloadAssignment(assignment)
        }
        binding.rvAssignments.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAssignments.adapter = adapter
    }

    private fun setupListeners() {
        binding.swipeRefreshAssignments.setOnRefreshListener {
            refreshData()
        }

        binding.btnSyncAssignments.setOnClickListener {
            binding.swipeRefreshAssignments.isRefreshing = true
            viewLifecycleOwner.lifecycleScope.launch {
                val res = repository.refreshAssignments()
                binding.swipeRefreshAssignments.isRefreshing = false
                if (res.isSuccess) {
                    Snackbar.make(binding.root, "Assignments synced with LMS!", Snackbar.LENGTH_SHORT).show()
                } else {
                    val msg = res.exceptionOrNull()?.message ?: "Assignments sync failed."
                    Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        binding.chipGroupAssignments.setOnCheckedStateChangeListener { _, checkedIds ->
            filterAssignments(checkedIds.firstOrNull())
        }

        binding.etSearchAssignments.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                searchQuery = s?.toString()?.trim() ?: ""
                filterAssignments(binding.chipGroupAssignments.checkedChipId)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                repository.assignmentsFlow.collect { list ->
                    allAssignments = list
                    updateUI(list)
                }
            }
        }
    }

    private fun updateUI(list: List<Assignment>) {
        val pendingCount = list.count { it.isPending }

        if (pendingCount > 0) {
            binding.tvAssignmentsSubtitle.text = "$pendingCount pending assignment(s) · Action needed"
        } else if (list.isNotEmpty()) {
            binding.tvAssignmentsSubtitle.text = "All assignments submitted! 🎉"
        } else {
            binding.tvAssignmentsSubtitle.text = "No assignments recorded on LMS"
        }

        filterAssignments(binding.chipGroupAssignments.checkedChipId)
    }

    private fun filterAssignments(chipId: Int?) {
        var filtered = when (chipId) {
            R.id.chipSubmitted -> allAssignments.filter { it.isSubmitted }
            R.id.chipAllAssignments -> allAssignments
            else -> allAssignments.filter { it.isPending }
        }

        if (searchQuery.isNotBlank()) {
            filtered = filtered.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                        it.courseName.contains(searchQuery, ignoreCase = true)
            }
        }

        adapter.submitList(filtered)

        if (filtered.isEmpty()) {
            binding.emptyAssignmentsView.visibility = View.VISIBLE
            binding.rvAssignments.visibility = View.GONE
        } else {
            binding.emptyAssignmentsView.visibility = View.GONE
            binding.rvAssignments.visibility = View.VISIBLE
        }
    }

    private fun downloadAssignment(assignment: Assignment) {
        val suggestedName = when {
            assignment.fileName.isNotBlank() -> assignment.fileName
            assignment.downloadUrl.isNotBlank() && assignment.downloadUrl.contains(".") -> {
                assignment.downloadUrl.substringAfterLast("/").substringBefore("?")
            }
            else -> "${assignment.title.replace("[^a-zA-Z0-9.-]".toRegex(), "_")}.docx"
        }

        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            ?: requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            ?: requireContext().filesDir

        val progressSnackbar = Snackbar.make(
            binding.root,
            "Downloading $suggestedName...",
            Snackbar.LENGTH_INDEFINITE
        )
        progressSnackbar.show()

        viewLifecycleOwner.lifecycleScope.launch {
            val res = repository.downloadAssignmentFile(assignment, downloadsDir, suggestedName)
            progressSnackbar.dismiss()

            if (res.isSuccess) {
                val downloadedFile = res.getOrThrow()
                // Register in device media store
                MediaScannerConnection.scanFile(
                    requireContext().applicationContext,
                    arrayOf(downloadedFile.absolutePath),
                    null,
                    null
                )

                Snackbar.make(
                    binding.root,
                    "Saved to Downloads: ${downloadedFile.name}",
                    Snackbar.LENGTH_LONG
                ).setAction("OPEN") {
                    openDownloadedFile(downloadedFile)
                }.show()
            } else {
                val err = res.exceptionOrNull()?.message ?: "Download failed"
                Snackbar.make(binding.root, "Error: $err", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun openDownloadedFile(file: File) {
        try {
            val context = requireContext()
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val ext = file.extension.lowercase()
            val mimeType = when (ext) {
                "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                "doc" -> "application/msword"
                "pdf" -> "application/pdf"
                "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation"
                "ppt" -> "application/vnd.ms-powerpoint"
                "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                "xls" -> "application/vnd.ms-excel"
                "zip" -> "application/zip"
                "rar" -> "application/x-rar-compressed"
                "txt" -> "text/plain"
                else -> {
                    val mapType = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
                    mapType ?: "*/*"
                }
            }

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, mimeType)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(Intent.createChooser(intent, "Open Assignment"))
        } catch (e: Exception) {
            Snackbar.make(binding.root, "Saved in Downloads: ${file.name}", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun refreshData() {
        binding.swipeRefreshAssignments.isRefreshing = true
        viewLifecycleOwner.lifecycleScope.launch {
            val res = repository.refreshAssignments()
            binding.swipeRefreshAssignments.isRefreshing = false
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
