package com.eduvos.campusapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.eduvos.campusapp.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        User::class,
        Course::class,
        Grade::class,
        Announcement::class,
        TimetableEntry::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun courseDao(): CourseDao
    abstract fun gradeDao(): GradeDao
    abstract fun announcementDao(): AnnouncementDao
    abstract fun timetableDao(): TimetableDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "eduvos_campus_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
        
        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database)
                    }
                }
            }
        }
        
        private suspend fun populateDatabase(database: AppDatabase) {
            // Add sample data
            val courseDao = database.courseDao()
            val userDao = database.userDao()
            val gradeDao = database.gradeDao()
            val announcementDao = database.announcementDao()
            val timetableDao = database.timetableDao()
            
            // Sample courses
            val courses = listOf(
                Course("CS101", "Introduction to Computer Science", "Dr. Smith", 4, "First", 2024, 
                    "Fundamentals of programming", "Room A101", "09:00", "Monday", "#2196F3"),
                Course("MATH201", "Calculus II", "Prof. Johnson", 4, "First", 2024,
                    "Advanced calculus concepts", "Room B202", "11:00", "Tuesday", "#4CAF50"),
                Course("ENG102", "Academic Writing", "Ms. Davis", 3, "First", 2024,
                    "Academic writing skills", "Room C303", "14:00", "Wednesday", "#FF9800"),
                Course("PHY101", "Physics I", "Dr. Brown", 4, "First", 2024,
                    "Classical mechanics", "Lab D404", "10:00", "Thursday", "#9C27B0"),
                Course("BUS201", "Business Management", "Prof. Wilson", 3, "First", 2024,
                    "Introduction to business principles", "Room E505", "15:00", "Friday", "#F44336")
            )
            courseDao.insertAll(courses)
            
            // Sample announcements
            val announcements = listOf(
                Announcement(
                    title = "Welcome to the new semester!",
                    content = "We hope you have a productive semester ahead. Make sure to check your timetable and course materials.",
                    courseCode = null,
                    author = "Admin",
                    priority = "High",
                    category = "General"
                ),
                Announcement(
                    title = "Assignment 1 Due",
                    content = "Please submit your first assignment by Friday 5 PM.",
                    courseCode = "CS101",
                    author = "Dr. Smith",
                    priority = "High",
                    category = "Assignment"
                ),
                Announcement(
                    title = "Mid-term Exam Schedule",
                    content = "The mid-term exam will be held on March 15th at 10 AM.",
                    courseCode = "MATH201",
                    author = "Prof. Johnson",
                    priority = "High",
                    category = "Exam"
                )
            )
            announcementDao.insertAll(announcements)
        }
    }
}