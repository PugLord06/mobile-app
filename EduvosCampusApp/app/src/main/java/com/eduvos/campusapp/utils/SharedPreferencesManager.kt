package com.eduvos.campusapp.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("EduvosCampusPrefs", Context.MODE_PRIVATE)
    
    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_STUDENT_NUMBER = "student_number"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_REMEMBER_ME = "remember_me"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_NOTIFICATION_ENABLED = "notification_enabled"
    }
    
    fun setLoggedIn(isLoggedIn: Boolean) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }
    
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    fun setStudentNumber(studentNumber: String) {
        prefs.edit().putString(KEY_STUDENT_NUMBER, studentNumber).apply()
    }
    
    fun getStudentNumber(): String? {
        return prefs.getString(KEY_STUDENT_NUMBER, null)
    }
    
    fun setUserEmail(email: String) {
        prefs.edit().putString(KEY_USER_EMAIL, email).apply()
    }
    
    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }
    
    fun setUserName(name: String) {
        prefs.edit().putString(KEY_USER_NAME, name).apply()
    }
    
    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, null)
    }
    
    fun setRememberMe(remember: Boolean) {
        prefs.edit().putBoolean(KEY_REMEMBER_ME, remember).apply()
    }
    
    fun getRememberMe(): Boolean {
        return prefs.getBoolean(KEY_REMEMBER_ME, false)
    }
    
    fun setThemeMode(mode: String) {
        prefs.edit().putString(KEY_THEME_MODE, mode).apply()
    }
    
    fun getThemeMode(): String {
        return prefs.getString(KEY_THEME_MODE, "light") ?: "light"
    }
    
    fun setNotificationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIFICATION_ENABLED, enabled).apply()
    }
    
    fun isNotificationEnabled(): Boolean {
        return prefs.getBoolean(KEY_NOTIFICATION_ENABLED, true)
    }
    
    fun clearUserData() {
        prefs.edit()
            .remove(KEY_IS_LOGGED_IN)
            .remove(KEY_STUDENT_NUMBER)
            .remove(KEY_USER_EMAIL)
            .remove(KEY_USER_NAME)
            .apply()
    }
    
    fun clearAll() {
        prefs.edit().clear().apply()
    }
}