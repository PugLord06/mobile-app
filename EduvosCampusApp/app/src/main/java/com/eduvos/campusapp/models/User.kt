package com.eduvos.campusapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val studentNumber: String,
    val fullName: String,
    val email: String,
    val password: String,
    val phone: String? = null,
    val faculty: String? = null,
    val program: String? = null,
    val yearOfStudy: Int = 1,
    val profileImageUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastLogin: Long = System.currentTimeMillis()
)