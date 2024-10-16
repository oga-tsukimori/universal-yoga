package com.example.universalyogalondon

import YogaClass
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.universalyogalondon.databinding.FragmentClassCalendarBinding
import android.graphics.Typeface
import android.view.Gravity
import java.util.*
import java.text.SimpleDateFormat


class ClassCalendarFragment : Fragment() {

    private var _binding: FragmentClassCalendarBinding? = null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()

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
        setupMonthNavigation()
        updateCalendarTitle()
        populateCalendar()
    }

    fun updateCalendar() {
        populateCalendar()
    }

    private fun setupMonthNavigation() {
        binding.buttonPreviousMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            updateCalendarTitle()
            populateCalendar()
        }

        binding.buttonNextMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            updateCalendarTitle()
            populateCalendar()
        }
    }

    private fun updateCalendarTitle() {
        val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val year = calendar.get(Calendar.YEAR)
        binding.textViewMonthYear.text = "$monthName $year"
    }

    private fun populateCalendar() {
        binding.calendarTable.removeAllViews()
        addHeaderRow()
        
        val timeSlots = listOf("07:00-08:30", "08:30-09:45", "09:45-10:30", "10:30-11:30", "11:30-13:00")
        val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri")
        
        timeSlots.forEach { timeSlot ->
            val row = TableRow(context)
            row.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)

            // Add time slot
            row.addView(createTextView(timeSlot, isHeader = true, isTimeSlot = true))

            // Add cells for each day
            for (day in daysOfWeek) {
                val cell = createTextView("", isHeader = false)
                val yogaClass = findYogaClassForDayAndTime(day, timeSlot)
                if (yogaClass != null) {
                    cell.text = "${yogaClass.classType}\n${yogaClass.duration}min"
                    cell.setBackgroundResource(R.drawable.cell_background)
                }
                row.addView(cell)
            }

            binding.calendarTable.addView(row)
        }
    }

    private fun addHeaderRow() {
        val headerRow = TableRow(context)
        val daysOfWeek = listOf("Time", "Mon", "Tue", "Wed", "Thu", "Fri")
        for (day in daysOfWeek) {
            headerRow.addView(createTextView(day, isHeader = true))
        }
        binding.calendarTable.addView(headerRow)
    }

    private fun createTextView(text: String, isHeader: Boolean = false, isTimeSlot: Boolean = false): TextView {
        return TextView(context).apply {
            this.text = text
            setPadding(16, 16, 16, 16)
            gravity = Gravity.CENTER
            textSize = if (isHeader) 16f else 14f
            if (isHeader) {
                setTypeface(null, Typeface.BOLD)
            }
            layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT).apply {
                if (isTimeSlot) {
                    width = resources.getDimensionPixelSize(R.dimen.time_slot_width)
                } else {
                    width = resources.getDimensionPixelSize(R.dimen.day_cell_width)
                }
                height = resources.getDimensionPixelSize(R.dimen.cell_height)
            }
        }
    }

    private fun findYogaClassForDayAndTime(day: String, timeSlot: String): YogaClass? {
        val (startTime, _) = timeSlot.split("-")
        return YogaClassStorage.getAllClasses().find { yogaClass ->
            yogaClass.dayOfWeek.startsWith(day, ignoreCase = true) &&
            yogaClass.time.startsWith(startTime)
        }
    }

    private fun isClassInCurrentMonth(yogaClass: YogaClass): Boolean {
        // This is a simplified check. You might need to adjust this based on how you store the date in YogaClass
        val classDate = Calendar.getInstance()
        classDate.time = SimpleDateFormat("EEEE, HH:mm", Locale.getDefault()).parse("${yogaClass.dayOfWeek}, ${yogaClass.time}")!!
        return classDate.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
               classDate.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
