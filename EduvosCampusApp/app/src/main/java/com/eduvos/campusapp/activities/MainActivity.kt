package com.eduvos.campusapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.eduvos.campusapp.R
import com.eduvos.campusapp.databinding.ActivityMainBinding
import com.eduvos.campusapp.fragments.*
import com.eduvos.campusapp.utils.SharedPreferencesManager

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefsManager: SharedPreferencesManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        prefsManager = SharedPreferencesManager(this)
        
        setupBottomNavigation()
        
        // Load home fragment by default
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }
    }
    
    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_courses -> {
                    loadFragment(CoursesFragment())
                    true
                }
                R.id.nav_timetable -> {
                    loadFragment(TimetableFragment())
                    true
                }
                R.id.nav_grades -> {
                    loadFragment(GradesFragment())
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }
    
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
    
    fun logout() {
        prefsManager.clearUserData()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}