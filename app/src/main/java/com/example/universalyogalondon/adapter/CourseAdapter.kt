package com.example.universalyogalondon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.universalyogalondon.R
import com.example.universalyogalondon.model.YogaClass

class CourseAdapter(private var courses: List<YogaClass>) : RecyclerView.Adapter<CourseAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val courseName: TextView = view.findViewById(R.id.courseNameTextView)
        val courseInstructor: TextView = view.findViewById(R.id.courseInstructorTextView)
        val courseDuration: TextView = view.findViewById(R.id.courseDurationTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val course = courses[position]
        holder.courseName.text = course.name
        holder.courseInstructor.text = "Instructor: ${course.description}"
        holder.courseDuration.text = "Duration: ${course.duration} minutes"
    }

    override fun getItemCount() = courses.size

    fun updateCourses(newCourses: List<YogaClass>) {
        courses = newCourses
        notifyDataSetChanged()
    }
}
