package com.eduvos.campusapp.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "timetable_entries",
    foreignKeys = [
        ForeignKey(
            entity = Course::class,
            parentColumns = ["courseCode"],
            childColumns = ["courseCode"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["studentNumber"],
            childColumns = ["studentNumber"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["courseCode"]),
        Index(value = ["studentNumber"])
    ]
)
data class TimetableEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentNumber: String,
    val courseCode: String,
    val dayOfWeek: String, // Monday, Tuesday, etc.
    val startTime: String, // HH:mm format
    val endTime: String, // HH:mm format
    val venue: String,
    val classType: String = "Lecture", // Lecture, Tutorial, Lab, Test
    val lecturer: String? = null
)