package com.bahria.portal.ui.calculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.bahria.portal.R
import com.bahria.portal.data.model.CourseAttendance
import com.bahria.portal.databinding.BottomSheetCalculatorBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max

class BunkCalculatorBottomSheet(
    private val courses: List<CourseAttendance>,
    private val preselectedCourseName: String? = null
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetCalculatorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetCalculatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCourseSpinner()
        setupTextWatchers()
    }

    private fun setupCourseSpinner() {
        val courseNames = mutableListOf("Custom / Manual Entry")
        courseNames.addAll(courses.map { it.courseName })

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, courseNames)
        binding.spinnerCalcCourse.adapter = adapter

        val initialIndex = if (!preselectedCourseName.isNullOrBlank()) {
            val idx = courseNames.indexOfFirst { it.equals(preselectedCourseName, ignoreCase = true) }
            if (idx >= 0) idx else 0
        } else {
            if (courses.isNotEmpty()) 1 else 0
        }

        binding.spinnerCalcCourse.setSelection(initialIndex)

        binding.spinnerCalcCourse.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0 && position - 1 < courses.size) {
                    val c = courses[position - 1]
                    binding.etCalcPresent.setText(c.present.toString())
                    binding.etCalcAbsent.setText(c.absent.toString())
                }
                recalculate()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupTextWatchers() {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                recalculate()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etCalcPresent.addTextChangedListener(watcher)
        binding.etCalcAbsent.addTextChangedListener(watcher)
    }

    private fun recalculate() {
        val present = binding.etCalcPresent.text?.toString()?.toDoubleOrNull() ?: 0.0
        val absent = binding.etCalcAbsent.text?.toString()?.toDoubleOrNull() ?: 0.0
        val total = present + absent

        if (total <= 0) {
            binding.tvCalcCurrentPct.text = "0.00%"
            binding.tvCalcAdvice.text = "Enter attended and missed classes to see calculation."
            return
        }

        val pct = Math.round((present / total) * 10000.0) / 100.0
        binding.tvCalcCurrentPct.text = "Current: $pct%"

        if (pct >= 75.0) {
            val safeBunks = max(0, floor((present / 0.75) - total).toInt())
            binding.cardCalcResult.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.status_safe_bg))
            binding.cardCalcResult.strokeColor = ContextCompat.getColor(requireContext(), R.color.status_safe_stroke)
            binding.tvCalcCurrentPct.setTextColor(ContextCompat.getColor(requireContext(), R.color.status_safe))

            if (safeBunks > 0) {
                binding.tvCalcAdvice.text = "✅ Safe to bunk $safeBunks class(es) while staying above 75% eligibility."
            } else {
                binding.tvCalcAdvice.text = "⚠️ On the edge! Missing even 1 more class will drop you below 75%."
            }
        } else {
            val needed = max(1, ceil(3.0 * total - 4.0 * present).toInt())
            binding.cardCalcResult.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.status_critical_bg))
            binding.cardCalcResult.strokeColor = ContextCompat.getColor(requireContext(), R.color.status_critical_stroke)
            binding.tvCalcCurrentPct.setTextColor(ContextCompat.getColor(requireContext(), R.color.status_critical))

            binding.tvCalcAdvice.text = "🚨 Critical! You must attend the next $needed consecutive class(es) without absence to reach 75% eligibility."
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
