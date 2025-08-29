package com.eduvos.campusapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey
    val courseCode: String,
    val courseName: String,
    val lecturer: String,
    val credits: Int,
    val semester: String,
    val year: Int,
    val description: String? = null,
    val venue: String? = null,
    val time: String? = null,
    val dayOfWeek: String? = null,
    val color: String = "#1976D2" // Default blue color
)