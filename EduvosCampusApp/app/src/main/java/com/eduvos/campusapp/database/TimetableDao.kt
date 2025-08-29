package com.eduvos.campusapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.eduvos.campusapp.models.TimetableEntry

@Dao
interface TimetableDao {
    @Query("""
        SELECT t.*, c.courseName, c.color 
        FROM timetable_entries t 
        INNER JOIN courses c ON t.courseCode = c.courseCode 
        WHERE t.studentNumber = :studentNumber 
        ORDER BY 
            CASE t.dayOfWeek 
                WHEN 'Monday' THEN 1 
                WHEN 'Tuesday' THEN 2 
                WHEN 'Wednesday' THEN 3 
                WHEN 'Thursday' THEN 4 
                WHEN 'Friday' THEN 5 
                ELSE 6 
            END, 
            t.startTime ASC
    """)
    fun getTimetableByStudent(studentNumber: String): LiveData<List<TimetableWithCourse>>
    
    @Query("""
        SELECT t.*, c.courseName, c.color 
        FROM timetable_entries t 
        INNER JOIN courses c ON t.courseCode = c.courseCode 
        WHERE t.studentNumber = :studentNumber AND t.dayOfWeek = :dayOfWeek
        ORDER BY t.startTime ASC
    """)
    fun getTimetableByDay(studentNumber: String, dayOfWeek: String): LiveData<List<TimetableWithCourse>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: TimetableEntry)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<TimetableEntry>)
    
    @Update
    suspend fun update(entry: TimetableEntry)
    
    @Delete
    suspend fun delete(entry: TimetableEntry)
    
    @Query("DELETE FROM timetable_entries WHERE studentNumber = :studentNumber")
    suspend fun deleteAllForStudent(studentNumber: String)
}

data class TimetableWithCourse(
    val id: Long,
    val studentNumber: String,
    val courseCode: String,
    val courseName: String,
    val color: String,
    val dayOfWeek: String,
    val startTime: String,
    val endTime: String,
    val venue: String,
    val classType: String,
    val lecturer: String?
)