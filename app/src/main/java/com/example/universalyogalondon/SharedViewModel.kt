package com.example.universalyogalondon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.universalyogalondon.model.YogaClass

class SharedViewModel : ViewModel() {
    private val _courses = MutableLiveData<List<YogaClass>>()
    val courses: LiveData<List<YogaClass>> = _courses

    fun addCourse(course: YogaClass) {
        val currentList = _courses.value ?: emptyList()
        _courses.value = currentList + course
    }
}
