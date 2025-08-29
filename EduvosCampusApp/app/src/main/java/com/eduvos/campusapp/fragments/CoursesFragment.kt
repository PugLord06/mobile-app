package com.eduvos.campusapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.eduvos.campusapp.activities.CourseDetailActivity
import com.eduvos.campusapp.adapters.CourseListAdapter
import com.eduvos.campusapp.database.AppDatabase
import com.eduvos.campusapp.databinding.FragmentCoursesBinding

class CoursesFragment : Fragment() {
    
    private var _binding: FragmentCoursesBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var database: AppDatabase
    private lateinit var courseAdapter: CourseListAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        database = AppDatabase.getDatabase(requireContext())
        
        setupRecyclerView()
        loadCourses()
    }
    
    private fun setupRecyclerView() {
        courseAdapter = CourseListAdapter { course ->
            val intent = Intent(requireContext(), CourseDetailActivity::class.java)
            intent.putExtra("courseCode", course.courseCode)
            startActivity(intent)
        }
        
        binding.rvCourses.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = courseAdapter
        }
    }
    
    private fun loadCourses() {
        database.courseDao().getAllCourses().observe(viewLifecycleOwner) { courses ->
            courseAdapter.submitList(courses)
            binding.tvEmpty.visibility = if (courses.isEmpty()) View.VISIBLE else View.GONE
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}