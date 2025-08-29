package com.eduvos.campusapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.eduvos.campusapp.models.Grade

@Dao
interface GradeDao {
    @Query("""
        SELECT g.*, c.courseName, c.credits 
        FROM grades g 
        INNER JOIN courses c ON g.courseCode = c.courseCode 
        WHERE g.studentNumber = :studentNumber 
        ORDER BY g.year DESC, g.semester DESC
    """)
    fun getGradesByStudent(studentNumber: String): LiveData<List<GradeWithCourse>>
    
    @Query("""
        SELECT g.*, c.courseName, c.credits 
        FROM grades g 
        INNER JOIN courses c ON g.courseCode = c.courseCode 
        WHERE g.studentNumber = :studentNumber AND g.semester = :semester AND g.year = :year
        ORDER BY c.courseName ASC
    """)
    fun getGradesBySemester(studentNumber: String, semester: String, year: Int): LiveData<List<GradeWithCourse>>
    
    @Query("SELECT * FROM grades WHERE studentNumber = :studentNumber AND courseCode = :courseCode LIMIT 1")
    suspend fun getGrade(studentNumber: String, courseCode: String): Grade?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(grade: Grade)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(grades: List<Grade>)
    
    @Update
    suspend fun update(grade: Grade)
    
    @Delete
    suspend fun delete(grade: Grade)
    
    @Query("SELECT AVG(percentage) FROM grades WHERE studentNumber = :studentNumber AND status = 'Passed'")
    suspend fun getAverageGrade(studentNumber: String): Float?
}

data class GradeWithCourse(
    val id: Long,
    val studentNumber: String,
    val courseCode: String,
    val courseName: String,
    val credits: Int,
    val grade: String,
    val percentage: Float,
    val status: String,
    val semester: String,
    val year: Int,
    val assessmentType: String?,
    val comments: String?
)