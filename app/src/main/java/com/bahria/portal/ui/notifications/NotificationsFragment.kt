package com.bahria.portal.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bahria.portal.data.repository.PortalRepository
import com.bahria.portal.databinding.FragmentNotificationsBinding
import kotlinx.coroutines.launch

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: PortalRepository
    private lateinit var adapter: NotificationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = PortalRepository.getInstance(requireContext())

        adapter = NotificationsAdapter()
        binding.rvNotifications.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotifications.adapter = adapter

        binding.btnClearAllNotifications.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                repository.clearAllNotifications()
            }
        }

        observeData()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                repository.notificationsFlow.collect { list ->
                    adapter.submitList(list)
                    if (list.isEmpty()) {
                        binding.emptyNotificationsView.visibility = View.VISIBLE
                        binding.rvNotifications.visibility = View.GONE
                    } else {
                        binding.emptyNotificationsView.visibility = View.GONE
                        binding.rvNotifications.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
