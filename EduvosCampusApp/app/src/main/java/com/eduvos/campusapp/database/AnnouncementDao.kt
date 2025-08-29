package com.eduvos.campusapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.eduvos.campusapp.models.Announcement

@Dao
interface AnnouncementDao {
    @Query("SELECT * FROM announcements ORDER BY timestamp DESC")
    fun getAllAnnouncements(): LiveData<List<Announcement>>
    
    @Query("SELECT * FROM announcements WHERE courseCode IS NULL OR courseCode = :courseCode ORDER BY timestamp DESC")
    fun getAnnouncementsByCourse(courseCode: String): LiveData<List<Announcement>>
    
    @Query("SELECT * FROM announcements ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentAnnouncements(limit: Int): LiveData<List<Announcement>>
    
    @Query("SELECT * FROM announcements WHERE priority = 'High' ORDER BY timestamp DESC")
    fun getHighPriorityAnnouncements(): LiveData<List<Announcement>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(announcement: Announcement)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(announcements: List<Announcement>)
    
    @Update
    suspend fun update(announcement: Announcement)
    
    @Delete
    suspend fun delete(announcement: Announcement)
    
    @Query("DELETE FROM announcements WHERE timestamp < :timestamp")
    suspend fun deleteOldAnnouncements(timestamp: Long)
}