object YogaClassStorage {
    private val classes = mutableListOf<YogaClass>()

    fun addClass(yogaClass: YogaClass) {
        classes.add(yogaClass)
    }

    fun getAllClasses(): List<YogaClass> = classes.toList()
}
