package com.eduvos.campusapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.eduvos.campusapp.adapters.TimetablePagerAdapter
import com.eduvos.campusapp.databinding.FragmentTimetableBinding
import com.google.android.material.tabs.TabLayoutMediator

class TimetableFragment : Fragment() {
    
    private var _binding: FragmentTimetableBinding? = null
    private val binding get() = _binding!!
    
    private val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimetableBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViewPager()
    }
    
    private fun setupViewPager() {
        val adapter = TimetablePagerAdapter(this, daysOfWeek)
        binding.viewPager.adapter = adapter
        
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = daysOfWeek[position]
        }.attach()
        
        // Set to current day
        val currentDayIndex = getCurrentDayIndex()
        if (currentDayIndex != -1) {
            binding.viewPager.setCurrentItem(currentDayIndex, false)
        }
    }
    
    private fun getCurrentDayIndex(): Int {
        val currentDay = java.text.SimpleDateFormat("EEEE", java.util.Locale.getDefault())
            .format(java.util.Date())
        return daysOfWeek.indexOf(currentDay)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}