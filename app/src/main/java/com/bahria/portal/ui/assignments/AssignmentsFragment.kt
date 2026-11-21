package com.bahria.portal.ui.assignments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.bahria.portal.data.model.Assignment
import com.bahria.portal.data.repository.PortalRepository
import com.bahria.portal.databinding.FragmentAssignmentsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

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
        adapter = AssignmentsAdapter()
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
        binding.cardAssignmentBanner.visibility = View.VISIBLE

        if (pendingCount > 0) {
            binding.tvBannerText.text = "Found $pendingCount pending assignment(s)"
            binding.cardAssignmentBanner.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_card))
            binding.cardAssignmentBanner.strokeColor = ContextCompat.getColor(requireContext(), R.color.status_safe_stroke)
            binding.tvBannerText.setTextColor(ContextCompat.getColor(requireContext(), R.color.status_safe))
            binding.ivBannerIcon.setImageResource(R.drawable.ic_check_circle)
            binding.ivBannerIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.status_safe))
        } else if (list.isNotEmpty()) {
            binding.tvBannerText.text = "No pending assignments! 🎉"
            binding.cardAssignmentBanner.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_card))
            binding.cardAssignmentBanner.strokeColor = ContextCompat.getColor(requireContext(), R.color.brand_primary)
            binding.tvBannerText.setTextColor(ContextCompat.getColor(requireContext(), R.color.brand_primary))
            binding.ivBannerIcon.setImageResource(R.drawable.ic_check_circle)
            binding.ivBannerIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.brand_primary))
        } else {
            binding.tvBannerText.text = "No assignments loaded yet"
            binding.cardAssignmentBanner.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_card))
            binding.cardAssignmentBanner.strokeColor = ContextCompat.getColor(requireContext(), R.color.bg_card_stroke)
            binding.tvBannerText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary))
            binding.ivBannerIcon.setImageResource(R.drawable.ic_assignment)
            binding.ivBannerIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.brand_secondary))
        }

        filterAssignments(binding.chipGroupAssignments.checkedChipId)
    }

    private fun filterAssignments(chipId: Int?) {
        var filtered = when (chipId) {
            R.id.chipSubmitted -> allAssignments.filter { it.isSubmitted }
            R.id.chipAllAssignments -> allAssignments
            else -> allAssignments.filter { it.isPending } // default is pending
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

    private fun refreshData() {
        binding.swipeRefreshAssignments.isRefreshing = true
        viewLifecycleOwner.lifecycleScope.launch {
            val res = repository.refreshAll(isForce = true)
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
