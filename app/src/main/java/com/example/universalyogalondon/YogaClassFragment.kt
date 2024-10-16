package com.example.universalyogalondon

import YogaClass
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.universalyogalondon.databinding.FragmentYogaClassBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.google.android.material.button.MaterialButton
import android.widget.NumberPicker
import android.text.InputType
import android.widget.Spinner
import com.google.android.material.button.MaterialButtonToggleGroup
import android.widget.AutoCompleteTextView

class YogaClassFragment : Fragment() {

    private var _binding: FragmentYogaClassBinding? = null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()

    private var capacity = 1
    private var duration = 15

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYogaClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDateTimePicker()
        setupCapacityButtons()
        setupDurationButtons()
        setupPriceField()
        setupCurrencySelector()
        setupClassTypeToggle()

        binding.buttonSave.setOnClickListener {
            if (validateInput()) {
                saveYogaClass()
                Toast.makeText(context, "Yoga class saved successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupDateTimePicker() {
        binding.buttonDateTime.setOnClickListener {
            showDateTimePicker()
        }
    }

    private fun setupCapacityButtons() {
        updateCapacityDisplay()
        binding.buttonDecreaseCapacity.setOnClickListener {
            if (capacity > 1) {
                capacity--
                updateCapacityDisplay()
            }
        }
        binding.buttonIncreaseCapacity.setOnClickListener {
            if (capacity < 50) {
                capacity++
                updateCapacityDisplay()
            }
        }
    }

    private fun updateCapacityDisplay() {
        binding.textViewCapacity.text = capacity.toString()
    }

    private fun setupDurationButtons() {
        updateDurationDisplay()
        binding.buttonDecreaseDuration.setOnClickListener {
            if (duration > 15) {
                duration -= 15
                updateDurationDisplay()
            }
        }
        binding.buttonIncreaseDuration.setOnClickListener {
            if (duration < 180) {
                duration += 15
                updateDurationDisplay()
            }
        }
    }

    private fun updateDurationDisplay() {
        binding.textViewDuration.text = duration.toString()
    }

    private fun setupPriceField() {
        binding.editTextPrice.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
    }

    private fun setupCurrencySelector() {
        val currencies = listOf("£", "$", "€")
        val adapter = ArrayAdapter(requireContext(), R.layout.item_currency, currencies)
        (binding.autoCompleteCurrency as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun setupClassTypeToggle() {
        val classTypes = resources.getStringArray(R.array.class_types)
        binding.toggleClassType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.button_flow -> { /* Handle Flow selection */ }
                    R.id.button_aerial -> { /* Handle Aerial selection */ }
                    R.id.button_family -> { /* Handle Family selection */ }
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true

        if (binding.buttonDateTime.text == getString(R.string.select_date_time)) {
            Toast.makeText(context, "Please select a date and time", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (capacity < 1) {
            Toast.makeText(context, "Please set a valid capacity", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (duration < 15) {
            Toast.makeText(context, "Please set a valid duration", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (binding.editTextPrice.text.isNullOrBlank()) {
            binding.editTextPrice.error = "Required"
            isValid = false
        }

        if (binding.toggleClassType.checkedButtonId == View.NO_ID) {
            Toast.makeText(context, "Please select a class type", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    private fun showDateTimePicker() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                showTimePicker()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                updateDateTimeDisplay()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        timePickerDialog.show()
    }

    private fun updateDateTimeDisplay() {
        val dateFormat = SimpleDateFormat("EEE, MMM d, yyyy HH:mm", Locale.getDefault())
        binding.buttonDateTime.text = dateFormat.format(calendar.time)
    }

    private fun saveYogaClass() {
        val dayOfWeek = getDayOfWeek(calendar)
        val time = getFormattedTime(calendar)
        val classType = getSelectedClassType()
        val price = binding.editTextPrice.text.toString().toFloatOrNull() ?: 0f
        val currency = (binding.autoCompleteCurrency as? AutoCompleteTextView)?.text.toString()

        val yogaClass = YogaClass(
            dayOfWeek = dayOfWeek,
            time = time,
            duration = duration,
            classType = classType,
            capacity = capacity,
            price = price,
            currency = currency
        )
        YogaClassStorage.addClass(yogaClass)
        
        // Notify the ClassCalendarFragment to update
        (parentFragmentManager.findFragmentByTag("ClassCalendarFragment") as? ClassCalendarFragment)?.updateCalendar()
        
        // Navigate back to the ClassCalendarFragment
        parentFragmentManager.popBackStack()
    }

    private fun getDayOfWeek(calendar: Calendar): String {
        val daysOfWeek = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        return daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1]
    }

    private fun getFormattedTime(calendar: Calendar): String {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return String.format("%02d:%02d", hour, minute)
    }

    private fun getSelectedClassType(): String {
        return when (binding.toggleClassType.checkedButtonId) {
            R.id.button_flow -> "Flow"
            R.id.button_aerial -> "Aerial"
            R.id.button_family -> "Family"
            else -> "Unknown"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
