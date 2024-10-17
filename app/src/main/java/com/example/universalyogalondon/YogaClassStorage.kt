package com.example.universalyogalondon

object YogaClassStorage {
    private val classes = mutableListOf<YogaClass>()

    fun addClass(yogaClass: YogaClass) {
        classes.add(yogaClass)
    }

    fun getAllClasses(): List<YogaClass> = classes.toList()

    fun getClassesForDay(dayOfWeek: String): List<YogaClass> {
        return classes.filter { it.dayOfWeek.equals(dayOfWeek, ignoreCase = true) }
    }
}
