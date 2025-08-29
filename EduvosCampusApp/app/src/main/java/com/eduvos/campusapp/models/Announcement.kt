package com.eduvos.campusapp.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "announcements",
    foreignKeys = [
        ForeignKey(
            entity = Course::class,
            parentColumns = ["courseCode"],
            childColumns = ["courseCode"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["courseCode"])]
)
data class Announcement(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val courseCode: String? = null, // null for general announcements
    val author: String,
    val timestamp: Long = System.currentTimeMillis(),
    val priority: String = "Normal", // High, Normal, Low
    val category: String = "General" // General, Assignment, Exam, Event
)