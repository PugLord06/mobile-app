package com.eduvos.campusapp.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.eduvos.campusapp.database.AppDatabase
import com.eduvos.campusapp.databinding.ActivityCourseDetailBinding
import com.eduvos.campusapp.models.Course
import kotlinx.coroutines.launch

class CourseDetailActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityCourseDetailBinding
    private lateinit var database: AppDatabase
    private var courseCode: String? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        database = AppDatabase.getDatabase(this)
        courseCode = intent.getStringExtra("courseCode")
        
        setupActionBar()
        loadCourseDetails()
    }
    
    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    
    private fun loadCourseDetails() {
        courseCode?.let { code ->
            lifecycleScope.launch {
                val course = database.courseDao().getCourseByCode(code)
                course?.let {
                    displayCourseInfo(it)
                }
            }
        }
    }
    
    private fun displayCourseInfo(course: Course) {
        binding.apply {
            collapsingToolbar.title = course.courseName
            
            tvCourseCode.text = course.courseCode
            tvCourseName.text = course.courseName
            tvLecturer.text = "Lecturer: ${course.lecturer}"
            tvCredits.text = "${course.credits} Credits"
            tvSemester.text = "${course.semester} Semester ${course.year}"
            
            course.description?.let {
                tvDescription.text = it
            }
            
            course.time?.let { time ->
                course.dayOfWeek?.let { day ->
                    tvSchedule.text = "Schedule: $day at $time"
                }
            }
            
            course.venue?.let {
                tvVenue.text = "Venue: $it"
            }
        }
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