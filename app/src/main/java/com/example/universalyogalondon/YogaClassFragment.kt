package com.example.universalyogalondon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.universalyogalondon.databinding.FragmentYogaClassBinding
import java.util.*


class YogaClassFragment : Fragment() {

    private var _binding: FragmentYogaClassBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYogaClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()
        setupSaveButton()
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.days_of_week,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.dayOfWeekSpinner.adapter = adapter
        }
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {
            val className = binding.classNameEditText.text.toString()
            if (className.isBlank()) {
                Toast.makeText(context, "Please enter a class name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dayOfWeek = binding.dayOfWeekSpinner.selectedItem.toString()
            val instructor = binding.instructorEditText.text.toString()
            val duration = binding.durationEditText.text.toString().toIntOrNull() ?: 0
            val level = when (binding.classTypeToggleGroup.checkedButtonId) {
                R.id.flowButton -> "Flow"
                R.id.aerialButton -> "Aerial"
                R.id.familyButton -> "Family"
                else -> "Unknown"
            }

            val time = String.format("%02d:%02d", binding.timePicker.hour, binding.timePicker.minute)

            val yogaClass = YogaClass(
                id = UUID.randomUUID().toString(),
                name = className,
                instructor = instructor,
                time = time,
                dayOfWeek = dayOfWeek,
                duration = duration,
                level = level,
                description = binding.descriptionEditText.text.toString()
            )

            YogaClassStorage.addClass(yogaClass)
            Toast.makeText(context, "Class saved successfully", Toast.LENGTH_SHORT).show()
            clearInputs()
        }
    }

    private fun clearInputs() {
        binding.classNameEditText.text?.clear()
        binding.instructorEditText.text?.clear()
        binding.durationEditText.text?.clear()
        binding.descriptionEditText.text?.clear()
        binding.dayOfWeekSpinner.setSelection(0)
        binding.classTypeToggleGroup.check(R.id.flowButton)
        binding.timePicker.hour = 0
        binding.timePicker.minute = 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
