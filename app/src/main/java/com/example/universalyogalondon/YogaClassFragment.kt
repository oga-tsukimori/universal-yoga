package com.example.universalyogalondon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.universalyogalondon.databinding.FragmentYogaClassBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class YogaClassFragment : Fragment() {

    private var _binding: FragmentYogaClassBinding? = null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()

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
        setupDatePicker()
        setupTimePicker()
        setupClassTypeChips()
        setupSaveButton()
    }

    private fun setupDatePicker() {
        binding.datePickerButton.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTheme(R.style.ThemeMaterialCalendar)
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                calendar.timeInMillis = selection
                updateDateButtonText()
            }

            datePicker.show(childFragmentManager, "DATE_PICKER")
        }
        updateDateButtonText()
    }

    private fun setupTimePicker() {
        binding.timePickerButton.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE))
                .setTitleText("Select time")
                .setTheme(R.style.ThemeMaterialTimePicker)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
                calendar.set(Calendar.MINUTE, timePicker.minute)
                updateTimeButtonText()
            }

            timePicker.show(childFragmentManager, "TIME_PICKER")
        }
        updateTimeButtonText()
    }

    private fun updateDateButtonText() {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        binding.datePickerButton.text = dateFormat.format(calendar.time)
    }

    private fun updateTimeButtonText() {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        binding.timePickerButton.text = timeFormat.format(calendar.time)
    }

    private fun setupClassTypeChips() {
        val classTypes = resources.getStringArray(R.array.class_types)
        classTypes.forEach { type ->
            val chip = Chip(context).apply {
                text = type
                isCheckable = true
                setChipBackgroundColorResource(R.color.chip_background_color)
                setTextColor(resources.getColorStateList(R.color.chip_text_color, null))
            }
            binding.classTypeChipGroup.addView(chip)
        }

        binding.addCustomTypeButton.setOnClickListener {
            val customType = binding.customTypeEditText.text.toString().trim()
            if (customType.isNotEmpty()) {
                val chip = Chip(context).apply {
                    text = customType
                    isCheckable = true
                    isChecked = true
                    setChipBackgroundColorResource(R.color.chip_background_color)
                    setTextColor(resources.getColorStateList(R.color.chip_text_color, null))
                }
                binding.classTypeChipGroup.addView(chip)
                binding.customTypeEditText.text?.clear()
            }
        }
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {
            val className = binding.classNameEditText.text.toString()
            if (className.isBlank()) {
                Toast.makeText(context, "Please enter a class name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val instructor = binding.instructorEditText.text.toString()
            val duration = binding.durationEditText.text.toString().toIntOrNull() ?: 0
            val selectedTypes = binding.classTypeChipGroup.checkedChipIds.map { chipId ->
                (binding.classTypeChipGroup.findViewById<Chip>(chipId)).text.toString()
            }

            val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)

            val yogaClass = YogaClass(
                id = UUID.randomUUID().toString(),
                name = className,
                instructor = instructor,
                time = time,
                date = date,
                dayOfWeek = dayOfWeek,
                duration = duration,
                level = selectedTypes.joinToString(", "),
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
        binding.classTypeChipGroup.clearCheck()
        binding.customTypeEditText.text?.clear()
        calendar.time = Date() // Reset to current date and time
        updateDateButtonText()
        updateTimeButtonText()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
