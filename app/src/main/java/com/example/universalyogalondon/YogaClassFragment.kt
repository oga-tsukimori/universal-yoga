package com.example.universalyogalondon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.universalyogalondon.databinding.FragmentYogaClassBinding

class YogaClassFragment : Fragment() {

    private var _binding: FragmentYogaClassBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYogaClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinners()

        binding.buttonSave.setOnClickListener {
            if (validateInput()) {
                // TODO: Save the yoga class details
                Toast.makeText(context, "Yoga class saved successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSpinners() {
        val daysOfWeek = resources.getStringArray(R.array.days_of_week)
        val daysAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, daysOfWeek)
        binding.spinnerDayOfWeek.adapter = daysAdapter

        val classTypes = resources.getStringArray(R.array.class_types)
        val typesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, classTypes)
        binding.spinnerClassType.adapter = typesAdapter
    }

    private fun validateInput(): Boolean {
        var isValid = true

        if (binding.spinnerDayOfWeek.selectedItemPosition == 0) {
            (binding.spinnerDayOfWeek.selectedView as? android.widget.TextView)?.error = "Required"
            isValid = false
        }

        if (binding.editTextTime.text.isNullOrBlank()) {
            binding.editTextTime.error = "Required"
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
