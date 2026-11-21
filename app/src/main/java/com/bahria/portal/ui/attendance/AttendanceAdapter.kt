package com.bahria.portal.ui.attendance

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bahria.portal.R
import com.bahria.portal.data.model.CourseAttendance
import com.bahria.portal.databinding.ItemCourseAttendanceBinding

class AttendanceAdapter(
    private val onCalculateClicked: (CourseAttendance) -> Unit
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
            binding.tvCourseCode.text = if (course.courseCode.isNotBlank()) course.courseCode else "Course Unit"

            binding.tvPresentCount.text = String.format("%.1f", course.present)
            binding.tvAbsentCount.text = String.format("%.1f", course.absent)
            binding.tvTotalCount.text = String.format("%.1f", course.total)

            val pct = course.percentage
            binding.tvPercentage.text = String.format("%.2f%%", pct)
            binding.pbAttendance.progress = pct.toInt()

            // Dynamic Styling based on Threshold
            when {
                course.isCritical -> {
                    // < 75%
                    binding.pillPercentage.setBackgroundResource(R.drawable.bg_status_critical)
                    binding.ivStatusIcon.setImageResource(R.drawable.ic_warning)
                    binding.ivStatusIcon.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.status_critical))
                    binding.tvPercentage.setTextColor(ContextCompat.getColor(context, R.color.status_critical))
                    binding.pbAttendance.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.status_critical))

                    val needed = course.classesNeededFor75
                    binding.tvBunkAdvice.text = "🚨 Critical! Attend next $needed classes to reach 75%"
                    binding.tvBunkAdvice.setTextColor(ContextCompat.getColor(context, R.color.status_critical))
                }
                course.isWarning -> {
                    // 75% - 79.9%
                    binding.pillPercentage.setBackgroundResource(R.drawable.bg_status_warning)
                    binding.ivStatusIcon.setImageResource(R.drawable.ic_warning)
                    binding.ivStatusIcon.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.status_warning))
                    binding.tvPercentage.setTextColor(ContextCompat.getColor(context, R.color.status_warning))
                    binding.pbAttendance.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.status_warning))

                    val safe = course.safeBunksAvailable
                    binding.tvBunkAdvice.text = if (safe > 0) "⚠️ Safe to miss $safe class(es)" else "⚠️ Warning: Don't miss next class"
                    binding.tvBunkAdvice.setTextColor(ContextCompat.getColor(context, R.color.status_warning))
                }
                else -> {
                    // >= 80%
                    binding.pillPercentage.setBackgroundResource(R.drawable.bg_status_safe)
                    binding.ivStatusIcon.setImageResource(R.drawable.ic_check_circle)
                    binding.ivStatusIcon.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.status_safe))
                    binding.tvPercentage.setTextColor(ContextCompat.getColor(context, R.color.status_safe))
                    binding.pbAttendance.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.status_safe))

                    val safe = course.safeBunksAvailable
                    binding.tvBunkAdvice.text = "✅ Safe to miss $safe class(es)"
                    binding.tvBunkAdvice.setTextColor(ContextCompat.getColor(context, R.color.status_safe))
                }
            }

            binding.btnCardCalc.setOnClickListener {
                onCalculateClicked(course)
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
