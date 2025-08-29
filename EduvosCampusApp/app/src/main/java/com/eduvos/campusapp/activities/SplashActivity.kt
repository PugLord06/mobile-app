package com.eduvos.campusapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.eduvos.campusapp.R
import com.eduvos.campusapp.databinding.ActivitySplashBinding
import com.eduvos.campusapp.utils.SharedPreferencesManager

class SplashActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySplashBinding
    private lateinit var prefsManager: SharedPreferencesManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        prefsManager = SharedPreferencesManager(this)
        
        // Apply animations
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        val slideUp = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        
        binding.imgLogo.startAnimation(fadeIn)
        binding.tvAppName.startAnimation(slideUp)
        binding.tvTagline.startAnimation(slideUp)
        
        // Delay and navigate
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, 2500)
    }
    
    private fun navigateToNextScreen() {
        val intent = if (prefsManager.isLoggedIn()) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }
        
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}