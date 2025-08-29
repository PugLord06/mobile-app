package com.eduvos.campusapp.utils

import android.content.Context
import android.text.format.DateFormat
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

// View Extensions
fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

// Context Extensions
fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun View.snackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, message, duration).show()
}

// Date Extensions
fun Long.toFormattedDate(): String {
    val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return format.format(Date(this))
}

fun Long.toFormattedDateTime(): String {
    val format = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
    return format.format(Date(this))
}

fun Long.toTimeAgo(): String {
    val now = System.currentTimeMillis()
    val diff = now - this
    
    return when {
        diff < 60_000 -> "Just now"
        diff < 3_600_000 -> "${diff / 60_000} minutes ago"
        diff < 86_400_000 -> "${diff / 3_600_000} hours ago"
        diff < 604_800_000 -> "${diff / 86_400_000} days ago"
        else -> toFormattedDate()
    }
}

// String Extensions
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidStudentNumber(): Boolean {
    return this.length >= 6 && this.all { it.isDigit() }
}

// Grade Extensions
fun String.toGradeColor(): String {
    return when (this.uppercase()) {
        "A", "A+" -> "#4CAF50"
        "B", "B+" -> "#8BC34A"
        "C", "C+" -> "#FFC107"
        "D", "D+" -> "#FF9800"
        "F" -> "#F44336"
        else -> "#757575"
    }
}

fun Float.toGradeLetter(): String {
    return when {
        this >= 90 -> "A+"
        this >= 80 -> "A"
        this >= 75 -> "B+"
        this >= 70 -> "B"
        this >= 65 -> "C+"
        this >= 60 -> "C"
        this >= 55 -> "D+"
        this >= 50 -> "D"
        else -> "F"
    }
}

// Time Extensions
fun String.to24HourFormat(): String {
    return try {
        val inputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = inputFormat.parse(this)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        this
    }
}

fun String.to12HourFormat(): String {
    return try {
        val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = inputFormat.parse(this)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        this
    }
}