package com.eduvos.campusapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.eduvos.campusapp.R
import com.eduvos.campusapp.database.AppDatabase
import com.eduvos.campusapp.databinding.ActivityRegisterBinding
import com.eduvos.campusapp.models.User
import com.eduvos.campusapp.utils.*
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var database: AppDatabase
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        database = AppDatabase.getDatabase(this)
        
        setupViews()
    }
    
    private fun setupViews() {
        binding.btnRegister.setOnClickListener {
            attemptRegistration()
        }
        
        binding.tvLogin.setOnClickListener {
            finish()
        }
        
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
    
    private fun attemptRegistration() {
        val studentNumber = binding.etStudentNumber.text.toString().trim()
        val fullName = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()
        
        // Validation
        when {
            studentNumber.isEmpty() -> {
                binding.tilStudentNumber.error = "Please enter your student number"
                return
            }
            !studentNumber.isValidStudentNumber() -> {
                binding.tilStudentNumber.error = "Please enter a valid student number"
                return
            }
            fullName.isEmpty() -> {
                binding.tilFullName.error = "Please enter your full name"
                return
            }
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
            password.length < 6 -> {
                binding.tilPassword.error = "Password must be at least 6 characters"
                return
            }
            confirmPassword != password -> {
                binding.tilConfirmPassword.error = getString(R.string.error_password_mismatch)
                return
            }
        }
        
        // Clear errors
        binding.tilStudentNumber.error = null
        binding.tilFullName.error = null
        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tilConfirmPassword.error = null
        
        // Show progress
        showLoading(true)
        
        // Create user
        lifecycleScope.launch {
            try {
                // Check if user exists
                val existingUser = database.userDao().getUserByStudentNumber(studentNumber)
                
                if (existingUser != null) {
                    showLoading(false)
                    binding.root.snackbar("Student number already registered")
                    return@launch
                }
                
                // Create new user
                val newUser = User(
                    studentNumber = studentNumber,
                    fullName = fullName,
                    email = email,
                    password = password
                )
                
                database.userDao().insert(newUser)
                
                showLoading(false)
                toast(getString(R.string.success_registration))
                finish()
                
            } catch (e: Exception) {
                showLoading(false)
                binding.root.snackbar(getString(R.string.error_registration_failed))
            }
        }
    }
    
    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) android.view.View.VISIBLE else android.view.View.GONE
        binding.btnRegister.isEnabled = !show
    }
}