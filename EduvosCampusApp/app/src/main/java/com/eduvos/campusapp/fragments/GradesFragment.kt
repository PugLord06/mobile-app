package com.eduvos.campusapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eduvos.campusapp.adapters.GradeAdapter
import com.eduvos.campusapp.database.AppDatabase
import com.eduvos.campusapp.databinding.FragmentGradesBinding
import com.eduvos.campusapp.utils.SharedPreferencesManager
import kotlinx.coroutines.launch

class GradesFragment : Fragment() {
    
    private var _binding: FragmentGradesBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var database: AppDatabase
    private lateinit var prefsManager: SharedPreferencesManager
    private lateinit var gradeAdapter: GradeAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGradesBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        database = AppDatabase.getDatabase(requireContext())
        prefsManager = SharedPreferencesManager(requireContext())
        
        setupRecyclerView()
        loadGrades()
    }
    
    private fun setupRecyclerView() {
        gradeAdapter = GradeAdapter()
        
        binding.rvGrades.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = gradeAdapter
        }
    }
    
    private fun loadGrades() {
        val studentNumber = prefsManager.getStudentNumber() ?: return
        
        database.gradeDao().getGradesByStudent(studentNumber).observe(viewLifecycleOwner) { grades ->
            gradeAdapter.submitList(grades)
            binding.tvEmpty.visibility = if (grades.isEmpty()) View.VISIBLE else View.GONE
            
            // Calculate GPA
            lifecycleScope.launch {
                val averageGrade = database.gradeDao().getAverageGrade(studentNumber)
                averageGrade?.let {
                    binding.tvGpa.text = String.format("Overall Average: %.1f%%", it)
                    binding.cardGpa.visibility = View.VISIBLE
                }
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}