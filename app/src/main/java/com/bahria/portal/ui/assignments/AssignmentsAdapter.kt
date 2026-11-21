package com.bahria.portal.ui.assignments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bahria.portal.R
import com.bahria.portal.data.model.Assignment
import com.bahria.portal.databinding.ItemAssignmentBinding

class AssignmentsAdapter : ListAdapter<Assignment, AssignmentsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAssignmentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemAssignmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(assignment: Assignment) {
            val context = binding.root.context
            binding.tvAssignmentTitle.text = assignment.title
            binding.tvAssignmentCourse.text = assignment.courseName
            binding.tvDueDate.text = "Due: ${assignment.dueDateString}"
            binding.tvStatusTag.text = assignment.status

            if (assignment.isSubmitted) {
                binding.tvCountdownBadge.text = "Submitted"
                binding.tvCountdownBadge.setBackgroundResource(R.drawable.bg_status_safe)
                binding.tvCountdownBadge.setTextColor(ContextCompat.getColor(context, R.color.status_safe))
                binding.tvStatusTag.setTextColor(ContextCompat.getColor(context, R.color.status_safe))
            } else if (assignment.isOverdue) {
                binding.tvCountdownBadge.text = "Overdue"
                binding.tvCountdownBadge.setBackgroundResource(R.drawable.bg_status_critical)
                binding.tvCountdownBadge.setTextColor(ContextCompat.getColor(context, R.color.status_critical))
                binding.tvStatusTag.setTextColor(ContextCompat.getColor(context, R.color.status_critical))
            } else {
                val days = assignment.calculatedDaysRemaining
                val hours = assignment.calculatedHoursRemaining

                when {
                    hours <= 24 -> {
                        val txt = if (hours <= 1) "Due soon!" else "$hours hours left"
                        binding.tvCountdownBadge.text = "⏰ $txt"
                        binding.tvCountdownBadge.setBackgroundResource(R.drawable.bg_status_critical)
                        binding.tvCountdownBadge.setTextColor(ContextCompat.getColor(context, R.color.status_critical))
                    }
                    days <= 2 -> {
                        binding.tvCountdownBadge.text = "⏰ $days day remaining"
                        binding.tvCountdownBadge.setBackgroundResource(R.drawable.bg_status_warning)
                        binding.tvCountdownBadge.setTextColor(ContextCompat.getColor(context, R.color.status_warning))
                    }
                    else -> {
                        binding.tvCountdownBadge.text = "⏰ $days days remaining"
                        binding.tvCountdownBadge.setBackgroundResource(R.drawable.bg_status_safe)
                        binding.tvCountdownBadge.setTextColor(ContextCompat.getColor(context, R.color.status_safe))
                    }
                }
                binding.tvStatusTag.setTextColor(ContextCompat.getColor(context, R.color.status_warning))
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Assignment>() {
        override fun areItemsTheSame(oldItem: Assignment, newItem: Assignment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Assignment, newItem: Assignment): Boolean {
            return oldItem == newItem
        }
    }
}
