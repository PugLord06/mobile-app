package com.eduvos.campusapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eduvos.campusapp.activities.CourseDetailActivity
import com.eduvos.campusapp.activities.GradesActivity
import com.eduvos.campusapp.activities.TimetableActivity
import com.eduvos.campusapp.adapters.AnnouncementAdapter
import com.eduvos.campusapp.adapters.CourseAdapter
import com.eduvos.campusapp.database.AppDatabase
import com.eduvos.campusapp.databinding.FragmentHomeBinding
import com.eduvos.campusapp.models.Course
import com.eduvos.campusapp.utils.SharedPreferencesManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var database: AppDatabase
    private lateinit var prefsManager: SharedPreferencesManager
    private lateinit var courseAdapter: CourseAdapter
    private lateinit var announcementAdapter: AnnouncementAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        database = AppDatabase.getDatabase(requireContext())
        prefsManager = SharedPreferencesManager(requireContext())
        
        setupViews()
        loadData()
    }
    
    private fun setupViews() {
        // Set greeting
        val greeting = getGreeting()
        val userName = prefsManager.getUserName() ?: "Student"
        binding.tvGreeting.text = "$greeting, $userName!"
        
        // Set current date
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
        binding.tvDate.text = dateFormat.format(Date())
        
        // Quick access cards
        binding.cardTimetable.setOnClickListener {
            startActivity(Intent(requireContext(), TimetableActivity::class.java))
        }
        
        binding.cardGrades.setOnClickListener {
            startActivity(Intent(requireContext(), GradesActivity::class.java))
        }
        
        binding.cardAssignments.setOnClickListener {
            // TODO: Implement assignments activity
        }
        
        binding.cardLibrary.setOnClickListener {
            // TODO: Implement library activity
        }
        
        // Setup RecyclerViews
        setupCoursesRecyclerView()
        setupAnnouncementsRecyclerView()
    }
    
    private fun setupCoursesRecyclerView() {
        courseAdapter = CourseAdapter { course ->
            val intent = Intent(requireContext(), CourseDetailActivity::class.java)
            intent.putExtra("courseCode", course.courseCode)
            startActivity(intent)
        }
        
        binding.rvCourses.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = courseAdapter
        }
    }
    
    private fun setupAnnouncementsRecyclerView() {
        announcementAdapter = AnnouncementAdapter()
        
        binding.rvAnnouncements.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = announcementAdapter
            isNestedScrollingEnabled = false
        }
    }
    
    private fun loadData() {
        val studentNumber = prefsManager.getStudentNumber() ?: return
        
        // Load courses
        database.courseDao().getAllCourses().observe(viewLifecycleOwner) { courses ->
            courseAdapter.submitList(courses.take(5)) // Show only first 5
            binding.tvEmptyCourses.visibility = if (courses.isEmpty()) View.VISIBLE else View.GONE
        }
        
        // Load announcements
        database.announcementDao().getRecentAnnouncements(5).observe(viewLifecycleOwner) { announcements ->
            announcementAdapter.submitList(announcements)
            binding.tvEmptyAnnouncements.visibility = if (announcements.isEmpty()) View.VISIBLE else View.GONE
        }
        
        // Load today's classes
        loadTodaysClasses(studentNumber)
    }
    
    private fun loadTodaysClasses(studentNumber: String) {
        val today = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
        
        database.timetableDao().getTimetableByDay(studentNumber, today).observe(viewLifecycleOwner) { classes ->
            if (classes.isNotEmpty()) {
                val nextClass = classes.firstOrNull { 
                    val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                    it.startTime > currentTime
                }
                
                nextClass?.let {
                    binding.tvNextClass.text = "${it.courseName} at ${it.startTime}"
                    binding.tvNextClassVenue.text = "Venue: ${it.venue}"
                    binding.cardNextClass.visibility = View.VISIBLE
                }
            } else {
                binding.cardNextClass.visibility = View.GONE
            }
        }
    }
    
    private fun getGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when {
            hour < 12 -> "Good morning"
            hour < 17 -> "Good afternoon"
            else -> "Good evening"
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}