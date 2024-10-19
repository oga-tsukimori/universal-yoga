package com.example.universalyogalondon

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.universalyogalondon.databinding.FragmentYogaClassBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.material.chip.Chip
import android.text.Editable
import android.text.TextWatcher
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.universalyogalondon.ClassInfo

class YogaClassFragment : Fragment() {

    private var _binding: FragmentYogaClassBinding? = null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()
    private var startDate: Long = 0
    private var endDate: Long = 0

    private val classes = mutableListOf<ClassInfo>()
    private lateinit var classAdapter: ClassAdapter

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
        setupDateRangePicker()
        setupClassTypeChips()
        setupSaveButton()
        setupInputValidation()
        setupAddClassButton()
        setupClassesRecyclerView()
    }

    private fun setupInputValidation() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateInputs()
            }
        }

        binding.classNameEditText.addTextChangedListener(textWatcher)
        binding.durationEditText.addTextChangedListener(textWatcher)
        binding.dateRangePickerButton.addTextChangedListener(textWatcher)
    }

    private fun validateInputs() {
        val isValid = binding.classNameEditText.text.isNotBlank() &&
                binding.durationEditText.text.isNotBlank() &&
                binding.dateRangePickerButton.text != "Select Course Duration" &&
                binding.classTypeChipGroup.checkedChipIds.isNotEmpty()

        binding.saveButton.isEnabled = isValid
    }

    private fun setupDateRangePicker() {
        binding.dateRangePickerButton.setOnClickListener {
            val constraintsBuilder = CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())

            val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select Course Duration")
                .setSelection(androidx.core.util.Pair(MaterialDatePicker.todayInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds()))
                .setCalendarConstraints(constraintsBuilder.build())
                .setTheme(R.style.ThemeMaterialCalendar)
                .build()

            dateRangePicker.addOnPositiveButtonClickListener { selection ->
                startDate = selection.first
                endDate = selection.second
                updateDateRangeButtonText()
                validateInputs()
            }

            dateRangePicker.show(childFragmentManager, "DATE_RANGE_PICKER")
        }
    }

    private fun updateDateRangeButtonText() {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val startDateStr = dateFormat.format(Date(startDate))
        val endDateStr = dateFormat.format(Date(endDate))
        binding.dateRangePickerButton.text = "$startDateStr - $endDateStr"
    }

    private fun setupClassTypeChips() {
        val classTypes = resources.getStringArray(R.array.class_types)
        classTypes.forEach { type ->
            val chip = Chip(context).apply {
                text = type
                isCheckable = true
                setChipBackgroundColorResource(R.color.chip_background_color)
                setTextColor(resources.getColorStateList(R.color.chip_text_color, null))
                setOnCheckedChangeListener { _, _ -> validateInputs() }
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
                    setOnCheckedChangeListener { _, _ -> validateInputs() }
                }
                binding.classTypeChipGroup.addView(chip)
                binding.customTypeEditText.text?.clear()
                validateInputs()
            }
        }
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {
            val className = binding.classNameEditText.text.toString()
            if (className.isBlank()) {
                Toast.makeText(context, "Please enter a course name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Remove the instructor variable
            val duration = binding.durationEditText.text.toString().toIntOrNull() ?: 0
            val selectedTypes = binding.classTypeChipGroup.checkedChipIds.map { chipId ->
                (binding.classTypeChipGroup.findViewById<Chip>(chipId)).text.toString()
            }

            val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
            val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date(startDate))

            val yogaClass = YogaClass(
                id = UUID.randomUUID().toString(),
                name = className,
                instructor = "", // Pass an empty string for now
                time = time,
                startDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(startDate)),
                endDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(endDate)),
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
        binding.durationEditText.text?.clear()
        binding.descriptionEditText.text?.clear()
        binding.classTypeChipGroup.clearCheck()
        binding.customTypeEditText.text?.clear()
        calendar.time = Date() // Reset to current date and time
        startDate = 0
        endDate = 0
        binding.dateRangePickerButton.text = "Select Course Duration"
    }

    private fun setupAddClassButton() {
        binding.button.setOnClickListener {
            showAddClassDialog()
        }
    }

    private fun showAddClassDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_class, null)
        val classNameEditText = dialogView.findViewById<EditText>(R.id.classNameEditText)
        val teacherNameEditText = dialogView.findViewById<EditText>(R.id.teacherNameEditText)
        val datePickerButton = dialogView.findViewById<Button>(R.id.datePickerButton)

        var selectedDate: Long = 0

        datePickerButton.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Class Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                selectedDate = selection
                val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                datePickerButton.text = dateFormat.format(Date(selectedDate))
            }

            datePicker.show(childFragmentManager, "DATE_PICKER")
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Add New Class")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val className = classNameEditText.text.toString()
                val teacherName = teacherNameEditText.text.toString()
                if (className.isNotBlank() && teacherName.isNotBlank() && selectedDate != 0L) {
                    val newClass = ClassInfo(className, teacherName, selectedDate)
                    classes.add(newClass)
                    classAdapter.notifyItemInserted(classes.size - 1)
                    Toast.makeText(context, "New class added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupClassesRecyclerView() {
        classAdapter = ClassAdapter(classes)
        binding.classesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = classAdapter
        }
    }
}
