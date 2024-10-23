package com.example.universalyogalondon.model

data class YogaClass(
    val id: String,
    val name: String,
    val instructor: String,
    val time: String,
    val startDate: String,
    val endDate: String,
    val dayOfWeek: String,
    val duration: Int,
    val level: String,
    val description: String
)
