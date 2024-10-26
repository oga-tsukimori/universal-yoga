package com.example.universalyogalondon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.universalyogalondon.R
import com.example.universalyogalondon.model.ClassInfo
import java.text.SimpleDateFormat
import java.util.*

class ClassAdapter(private val classes: List<ClassInfo>) : RecyclerView.Adapter<ClassAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val classNameTextView: TextView = view.findViewById(R.id.classNameTextView)
        val teacherNameTextView: TextView = view.findViewById(R.id.teacherNameTextView)
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_class, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val classInfo = classes[position]
        holder.classNameTextView.text = classInfo.className
        holder.teacherNameTextView.text = classInfo.teacherName
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        holder.dateTextView.text = dateFormat.format(Date(classInfo.date))
    }

    override fun getItemCount() = classes.size
}
