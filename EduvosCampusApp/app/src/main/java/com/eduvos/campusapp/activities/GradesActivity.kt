package com.eduvos.campusapp.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.eduvos.campusapp.databinding.ActivityGradesBinding

class GradesActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityGradesBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGradesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupActionBar()
    }
    
    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Grades"
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}