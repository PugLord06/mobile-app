package com.eduvos.campusapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.eduvos.campusapp.models.Course

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses ORDER BY courseName ASC")
    fun getAllCourses(): LiveData<List<Course>>
    
    @Query("SELECT * FROM courses WHERE courseCode = :courseCode LIMIT 1")
    suspend fun getCourseByCode(courseCode: String): Course?
    
    @Query("SELECT * FROM courses WHERE semester = :semester AND year = :year")
    fun getCoursesBySemester(semester: String, year: Int): LiveData<List<Course>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(course: Course)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(courses: List<Course>)
    
    @Update
    suspend fun update(course: Course)
    
    @Delete
    suspend fun delete(course: Course)
    
    @Query("DELETE FROM courses")
    suspend fun deleteAll()
}