package com.example.universalyogalondon

data class YogaClass(
    val id: String,
    val name: String,
    val instructor: String,
    val time: String,
    val date: String,
    val dayOfWeek: String,
    val duration: Int,
    val level: String, // This will now contain comma-separated class types
    val description: String
)
