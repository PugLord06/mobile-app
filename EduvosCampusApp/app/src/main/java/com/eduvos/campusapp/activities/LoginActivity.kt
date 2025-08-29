package com.eduvos.campusapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.eduvos.campusapp.R
import com.eduvos.campusapp.database.AppDatabase
import com.eduvos.campusapp.databinding.ActivityLoginBinding
import com.eduvos.campusapp.utils.*
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private lateinit var database: AppDatabase
    private lateinit var prefsManager: SharedPreferencesManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        database = AppDatabase.getDatabase(this)
        prefsManager = SharedPreferencesManager(this)
        
        setupViews()
        loadSavedCredentials()
    }
    
    private fun setupViews() {
        binding.btnLogin.setOnClickListener {
            attemptLogin()
        }
        
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        
        binding.tvForgotPassword.setOnClickListener {
            toast("Password reset functionality coming soon!")
        }
    }
    
    private fun loadSavedCredentials() {
        if (prefsManager.getRememberMe()) {
            binding.etEmail.setText(prefsManager.getUserEmail())
            binding.checkboxRemember.isChecked = true
        }
    }
    
    private fun attemptLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        
        // Validation
        when {
            email.isEmpty() -> {
                binding.tilEmail.error = getString(R.string.error_empty_email)
                return
            }
            !email.isValidEmail() -> {
                binding.tilEmail.error = getString(R.string.error_invalid_email)
                return
            }
            password.isEmpty() -> {
                binding.tilPassword.error = getString(R.string.error_empty_password)
                return
            }
        }
        
        // Clear errors
        binding.tilEmail.error = null
        binding.tilPassword.error = null
        
        // Show progress
        showLoading(true)
        
        // Perform login
        lifecycleScope.launch {
            try {
                val user = database.userDao().login(email, password)
                
                if (user != null) {
                    // Save login state
                    prefsManager.setLoggedIn(true)
                    prefsManager.setStudentNumber(user.studentNumber)
                    prefsManager.setUserEmail(user.email)
                    prefsManager.setUserName(user.fullName)
                    
                    // Save remember me preference
                    prefsManager.setRememberMe(binding.checkboxRemember.isChecked)
                    
                    // Update last login
                    database.userDao().updateLastLogin(user.studentNumber, System.currentTimeMillis())
                    
                    // Navigate to main
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    // For demo purposes, create a default user if login fails
                    if (email == "demo@eduvos.com" && password == "demo123") {
                        createDemoUser()
                    } else {
                        showLoading(false)
                        binding.root.snackbar(getString(R.string.error_login_failed))
                    }
                }
            } catch (e: Exception) {
                showLoading(false)
                binding.root.snackbar(getString(R.string.error_network))
            }
        }
    }
    
    private fun createDemoUser() {
        lifecycleScope.launch {
            val demoUser = com.eduvos.campusapp.models.User(
                studentNumber = "2024001",
                fullName = "Demo Student",
                email = "demo@eduvos.com",
                password = "demo123",
                phone = "+27 123 456 789",
                faculty = "Computer Science",
                program = "Bachelor of Computer Science",
                yearOfStudy = 2
            )
            
            database.userDao().insert(demoUser)
            
            // Create demo grades
            val demoGrades = listOf(
                com.eduvos.campusapp.models.Grade(
                    studentNumber = "2024001",
                    courseCode = "CS101",
                    grade = "A",
                    percentage = 85f,
                    status = "Passed",
                    semester = "First",
                    year = 2024
                ),
                com.eduvos.campusapp.models.Grade(
                    studentNumber = "2024001",
                    courseCode = "MATH201",
                    grade = "B+",
                    percentage = 78f,
                    status = "Passed",
                    semester = "First",
                    year = 2024
                ),
                com.eduvos.campusapp.models.Grade(
                    studentNumber = "2024001",
                    courseCode = "ENG102",
                    grade = "A-",
                    percentage = 82f,
                    status = "Passed",
                    semester = "First",
                    year = 2024
                )
            )
            database.gradeDao().insertAll(demoGrades)
            
            // Create demo timetable
            val demoTimetable = listOf(
                com.eduvos.campusapp.models.TimetableEntry(
                    studentNumber = "2024001",
                    courseCode = "CS101",
                    dayOfWeek = "Monday",
                    startTime = "09:00",
                    endTime = "11:00",
                    venue = "Room A101",
                    classType = "Lecture",
                    lecturer = "Dr. Smith"
                ),
                com.eduvos.campusapp.models.TimetableEntry(
                    studentNumber = "2024001",
                    courseCode = "MATH201",
                    dayOfWeek = "Tuesday",
                    startTime = "11:00",
                    endTime = "13:00",
                    venue = "Room B202",
                    classType = "Lecture",
                    lecturer = "Prof. Johnson"
                ),
                com.eduvos.campusapp.models.TimetableEntry(
                    studentNumber = "2024001",
                    courseCode = "ENG102",
                    dayOfWeek = "Wednesday",
                    startTime = "14:00",
                    endTime = "16:00",
                    venue = "Room C303",
                    classType = "Tutorial",
                    lecturer = "Ms. Davis"
                ),
                com.eduvos.campusapp.models.TimetableEntry(
                    studentNumber = "2024001",
                    courseCode = "CS101",
                    dayOfWeek = "Thursday",
                    startTime = "09:00",
                    endTime = "11:00",
                    venue = "Lab D101",
                    classType = "Lab",
                    lecturer = "Dr. Smith"
                )
            )
            database.timetableDao().insertAll(demoTimetable)
            
            // Now login with the demo user
            attemptLogin()
        }
    }
    
    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) android.view.View.VISIBLE else android.view.View.GONE
        binding.btnLogin.isEnabled = !show
    }
}