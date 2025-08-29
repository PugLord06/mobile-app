package com.eduvos.campusapp.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.eduvos.campusapp.fragments.DayTimetableFragment

class TimetablePagerAdapter(
    fragment: Fragment,
    private val days: List<String>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = days.size

    override fun createFragment(position: Int): Fragment {
        return DayTimetableFragment.newInstance(days[position])
    }
}