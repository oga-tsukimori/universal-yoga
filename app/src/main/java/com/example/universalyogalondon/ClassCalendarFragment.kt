package com.example.universalyogalondon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.universalyogalondon.databinding.FragmentClassCalendarBinding

class ClassCalendarFragment : Fragment() {

    private var _binding: FragmentClassCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateCalendar()
    }

    private fun populateCalendar() {
        val timeSlots = listOf("07:00-08:30", "08:30-09:45", "09:45-10:30", "10:30-11:30", "11:30-13:00")
        
        timeSlots.forEach { timeSlot ->
            val row = TableRow(context)
            row.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
            row.setPadding(8, 8, 8, 8)

            // Add time slot
            row.addView(createTextView(timeSlot))

            // Add cells for each day
            for (i in 1..5) {
                row.addView(createTextView(""))
            }

            binding.calendarTable.addView(row)
        }

        // TODO: Fetch added classes and populate the calendar
        // This is where you'd add logic to retrieve saved classes and add them to the appropriate cells
    }

    private fun createTextView(text: String): TextView {
        return TextView(context).apply {
            this.text = text
            setPadding(8, 8, 8, 8)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
