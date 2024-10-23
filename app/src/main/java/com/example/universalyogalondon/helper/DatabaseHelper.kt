package com.example.universalyogalondon.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.universalyogalondon.model.YogaClass

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "Courses.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "courses"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_DURATION = "duration"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_TYPE = "type"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_DURATION INTEGER,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_TYPE TEXT
            )
        """
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertCourse(name: String, duration: Int, description: String, type: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_DURATION, duration)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_TYPE, type)
        }
        return db.insert(TABLE_NAME, null, contentValues)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllCourses(): List<YogaClass> {
        val courses = mutableListOf<YogaClass>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val courseName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val duration = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val course = YogaClass(
                    id = id.toString(),
                    name = courseName,
                    instructor = "", // Pass an empty string for now
                    time = "",
                    startDate = "2024-12-12",
                    endDate = "2024-12-13",
                    dayOfWeek = "",
                    duration = duration,
                    level = "",
                    description = description
                )
                courses.add(course)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return courses
    }
}
