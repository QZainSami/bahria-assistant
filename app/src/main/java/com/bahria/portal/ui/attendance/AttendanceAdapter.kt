package com.bahria.portal.ui.attendance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bahria.portal.R
import com.bahria.portal.data.model.CourseAttendance
import com.bahria.portal.databinding.ItemCourseAttendanceBinding
import java.util.Locale

class AttendanceAdapter(
    private val onCourseClicked: ((CourseAttendance) -> Unit)? = null
) : ListAdapter<CourseAttendance, AttendanceAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCourseAttendanceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemCourseAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(course: CourseAttendance) {
            val context = binding.root.context
            binding.tvCourseName.text = course.courseName

            // Subline: CSC 412 · 3 Cr Hr · 19/27 hrs
            val presentInt = course.present.toInt()
            val totalInt = course.total.toInt()
            binding.tvCourseSubline.text = "${course.displayCode} · ${course.creditHours} Cr Hr · $presentInt/$totalInt hrs"

            // Current rate formatting
            binding.tvCurrentRate.text = String.format(Locale.ENGLISH, "%.1f%%", course.percentage)

            // Max allowed and Used / left
            binding.tvMaxAllowed.text = "${course.maxAllowedAbsentHours}h"
            binding.tvUsedLeft.text = "${course.usedAbsentHours}h / ${course.leftAbsentHours}h"

            // Level Bar and Top Badge Styling
            if (course.isCritical) {
                // Critical (< 75%)
                val needed = course.hoursNeededFor75
                binding.tvBadgeStatus.text = "${needed}h Required"
                binding.tvBadgeStatus.setBackgroundResource(R.drawable.bg_badge_required)
                binding.tvBadgeStatus.setTextColor(ContextCompat.getColor(context, R.color.status_critical))

                binding.pbLevelBar.progressDrawable = ContextCompat.getDrawable(context, R.drawable.level_bar_progress_red)
                binding.tvCurrentRate.setTextColor(ContextCompat.getColor(context, R.color.status_critical))
            } else {
                // Safe or Warning (>= 75%)
                val left = course.leftAbsentHours
                binding.tvBadgeStatus.text = "${left}h Left"
                binding.tvBadgeStatus.setBackgroundResource(R.drawable.bg_badge_left)
                binding.tvBadgeStatus.setTextColor(ContextCompat.getColor(context, R.color.status_safe))

                binding.pbLevelBar.progressDrawable = ContextCompat.getDrawable(context, R.drawable.level_bar_progress_green)
                binding.tvCurrentRate.setTextColor(ContextCompat.getColor(context, R.color.brand_secondary))
            }

            binding.pbLevelBar.progress = course.percentage.toInt().coerceIn(0, 100)

            binding.root.setOnClickListener {
                onCourseClicked?.invoke(course)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CourseAttendance>() {
        override fun areItemsTheSame(oldItem: CourseAttendance, newItem: CourseAttendance): Boolean {
            return oldItem.courseName == newItem.courseName
        }

        override fun areContentsTheSame(oldItem: CourseAttendance, newItem: CourseAttendance): Boolean {
            return oldItem == newItem
        }
    }
}
