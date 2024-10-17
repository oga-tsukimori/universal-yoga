package com.example.universalyogalondon

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
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder


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
        binding.textViewMonthYear.text = "$monthName ${calendar.get(Calendar.YEAR)}"
    }

    private fun populateCalendar() {
        binding.calendarGrid.removeAllViews()
        addHeaderRow()

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfMonth = getFirstDayOfMonth()

        var dayOfMonth = 1
        for (i in 0 until 6) {
            val row = TableRow(context)
            row.layoutParams = TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f)
            for (j in 0 until 7) {
                if (i == 0 && j < firstDayOfMonth || dayOfMonth > daysInMonth) {
                    row.addView(createEmptyCell())
                } else {
                    row.addView(createDayCell(dayOfMonth))
                    dayOfMonth++
                }
            }
            binding.calendarGrid.addView(row)
            if (dayOfMonth > daysInMonth) break
        }
    }

    private fun addHeaderRow() {
        val headerRow = TableRow(context)
        val daysOfWeek = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        for (day in daysOfWeek) {
            headerRow.addView(createTextView(day, isHeader = true))
        }
        binding.calendarGrid.addView(headerRow)
    }

    private fun getFirstDayOfMonth(): Int {
        val temp = calendar.clone() as Calendar
        temp.set(Calendar.DAY_OF_MONTH, 1)
        var firstDay = temp.get(Calendar.DAY_OF_WEEK) - 2
        if (firstDay < 0) firstDay = 6
        return firstDay
    }

    private fun createDayCell(day: Int): FrameLayout {
        val cell = FrameLayout(requireContext())
        cell.layoutParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).apply {
            setMargins(2, 2, 2, 2)
        }
        cell.setPadding(8, 8, 8, 8)

        val dayView = TextView(context).apply {
            text = day.toString()
            setTextColor(ContextCompat.getColor(context, R.color.black))
            textSize = 16f
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.TOP or Gravity.START
            layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        cell.addView(dayView)

        val temp = calendar.clone() as Calendar
        temp.set(Calendar.DAY_OF_MONTH, day)
        val dayOfWeek = temp.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
        val classes = YogaClassStorage.getClassesForDay(dayOfWeek)

        if (classes.isNotEmpty()) {
            // Add pink background for days with classes
            cell.setBackgroundResource(R.drawable.calendar_cell_background)
            
            // Add a small indicator dot
            val indicatorDot = View(context).apply {
                layoutParams = FrameLayout.LayoutParams(8, 8).apply {
                    gravity = Gravity.TOP or Gravity.END
                    setMargins(0, 4, 4, 0)
                }
                background = ContextCompat.getDrawable(context, R.drawable.indicator_dot)
            }
            cell.addView(indicatorDot)
        }

        cell.setOnClickListener {
            showClassesForDay(day)
        }

        return cell
    }

    private fun createEmptyCell(): View {
        return View(context).apply {
            layoutParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
        }
    }

    private fun showClassesForDay(day: Int) {
        val temp = calendar.clone() as Calendar
        temp.set(Calendar.DAY_OF_MONTH, day)
        val dayOfWeek = temp.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        val classes = YogaClassStorage.getClassesForDay(dayOfWeek)

        val message = if (classes.isNotEmpty()) {
            classes.joinToString("\n") { yogaClass ->
                "${yogaClass.time} - ${yogaClass.name} (${yogaClass.duration} min)"
            }
        } else {
            "No classes scheduled for this day"
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Classes for $dayOfWeek, ${temp.get(Calendar.DAY_OF_MONTH)} ${temp.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())}")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun createTextView(text: String, isHeader: Boolean = false): TextView {
        return TextView(context).apply {
            this.text = text
            setPadding(8, 8, 8, 8)
            gravity = Gravity.CENTER
            layoutParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            if (isHeader) {
                setTypeface(null, Typeface.BOLD)
                setTextColor(ContextCompat.getColor(context, R.color.pink_500))
                textSize = 14f
            } else {
                setTextColor(ContextCompat.getColor(context, R.color.black))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
