object YogaClassStorage {
    private val classes = mutableListOf<YogaClass>()

    fun addClass(yogaClass: YogaClass) {
        classes.add(yogaClass)
    }

    fun getAllClasses(): List<YogaClass> = classes.toList()

    fun getClassesForDay(day: String): List<YogaClass> {
        return classes.filter { it.dayOfWeek.equals(day, ignoreCase = true) }
    }
}
