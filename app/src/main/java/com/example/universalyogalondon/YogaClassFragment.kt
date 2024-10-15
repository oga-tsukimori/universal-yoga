package com.example.universalyogalondon

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

class YogaClassFragment : Fragment() {

    private var _binding: FragmentYogaClassBinding? = null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYogaClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()

        binding.buttonDateTime.setOnClickListener {
            showDateTimePicker()
        }

        binding.buttonSave.setOnClickListener {
            if (validateInput()) {
                // TODO: Save the yoga class details
                Toast.makeText(context, "Yoga class saved successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSpinner() {
        val classTypes = resources.getStringArray(R.array.class_types)
        val typesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, classTypes)
        binding.spinnerClassType.adapter = typesAdapter
    }

    private fun validateInput(): Boolean {
        var isValid = true

        if (binding.buttonDateTime.text == getString(R.string.select_date_time)) {
            Toast.makeText(context, "Please select a date and time", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (binding.editTextCapacity.text.isNullOrBlank()) {
            binding.editTextCapacity.error = "Required"
            isValid = false
        }

        if (binding.editTextDuration.text.isNullOrBlank()) {
            binding.editTextDuration.error = "Required"
            isValid = false
        }

        if (binding.editTextPrice.text.isNullOrBlank()) {
            binding.editTextPrice.error = "Required"
            isValid = false
        }

        if (binding.spinnerClassType.selectedItemPosition == 0) {
            (binding.spinnerClassType.selectedView as? android.widget.TextView)?.error = "Required"
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
