package com.eduvos.campusapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.eduvos.campusapp.adapters.TimetableAdapter
import com.eduvos.campusapp.database.AppDatabase
import com.eduvos.campusapp.databinding.FragmentDayTimetableBinding
import com.eduvos.campusapp.utils.SharedPreferencesManager

class DayTimetableFragment : Fragment() {
    
    private var _binding: FragmentDayTimetableBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var database: AppDatabase
    private lateinit var prefsManager: SharedPreferencesManager
    private lateinit var timetableAdapter: TimetableAdapter
    private var dayOfWeek: String = ""
    
    companion object {
        private const val ARG_DAY = "day_of_week"
        
        fun newInstance(day: String): DayTimetableFragment {
            return DayTimetableFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DAY, day)
                }
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dayOfWeek = arguments?.getString(ARG_DAY) ?: ""
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDayTimetableBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        database = AppDatabase.getDatabase(requireContext())
        prefsManager = SharedPreferencesManager(requireContext())
        
        setupRecyclerView()
        loadTimetable()
    }
    
    private fun setupRecyclerView() {
        timetableAdapter = TimetableAdapter()
        
        binding.rvTimetable.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = timetableAdapter
        }
    }
    
    private fun loadTimetable() {
        val studentNumber = prefsManager.getStudentNumber() ?: return
        
        database.timetableDao().getTimetableByDay(studentNumber, dayOfWeek)
            .observe(viewLifecycleOwner) { entries ->
                timetableAdapter.submitList(entries)
                binding.tvEmpty.visibility = if (entries.isEmpty()) View.VISIBLE else View.GONE
            }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}