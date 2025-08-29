package com.eduvos.campusapp.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "grades",
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
data class Grade(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentNumber: String,
    val courseCode: String,
    val grade: String, // A, B, C, D, F
    val percentage: Float,
    val status: String, // Passed, Failed, In Progress
    val semester: String,
    val year: Int,
    val assessmentType: String? = null, // Exam, Assignment, Test
    val comments: String? = null
)