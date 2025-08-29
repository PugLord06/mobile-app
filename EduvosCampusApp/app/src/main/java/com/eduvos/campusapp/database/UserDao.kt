package com.eduvos.campusapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.eduvos.campusapp.models.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): User?
    
    @Query("SELECT * FROM users WHERE studentNumber = :studentNumber LIMIT 1")
    suspend fun getUserByStudentNumber(studentNumber: String): User?
    
    @Query("SELECT * FROM users WHERE studentNumber = :studentNumber LIMIT 1")
    fun getUserLiveData(studentNumber: String): LiveData<User?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)
    
    @Update
    suspend fun update(user: User)
    
    @Delete
    suspend fun delete(user: User)
    
    @Query("UPDATE users SET lastLogin = :timestamp WHERE studentNumber = :studentNumber")
    suspend fun updateLastLogin(studentNumber: String, timestamp: Long)
}