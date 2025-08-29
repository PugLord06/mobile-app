package com.eduvos.campusapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eduvos.campusapp.databinding.ItemCourseListBinding
import com.eduvos.campusapp.models.Course

class CourseListAdapter(
    private val onCourseClick: (Course) -> Unit
) : ListAdapter<Course, CourseListAdapter.CourseViewHolder>(CourseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CourseViewHolder(
        private val binding: ItemCourseListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(course: Course) {
            binding.apply {
                tvCourseCode.text = course.courseCode
                tvCourseName.text = course.courseName
                tvLecturer.text = "Lecturer: ${course.lecturer}"
                tvCredits.text = "${course.credits} Credits"
                tvSemester.text = "${course.semester} Semester ${course.year}"
                
                course.time?.let { time ->
                    course.dayOfWeek?.let { day ->
                        tvSchedule.text = "$day at $time"
                    }
                } ?: run {
                    tvSchedule.text = "Schedule TBA"
                }
                
                course.venue?.let {
                    tvVenue.text = "Venue: $it"
                } ?: run {
                    tvVenue.text = "Venue TBA"
                }
                
                // Set color indicator
                try {
                    viewColorIndicator.setBackgroundColor(Color.parseColor(course.color))
                } catch (e: Exception) {
                    viewColorIndicator.setBackgroundColor(Color.parseColor("#1976D2"))
                }
                
                root.setOnClickListener {
                    onCourseClick(course)
                }
            }
        }
    }

    class CourseDiffCallback : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.courseCode == newItem.courseCode
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem
        }
    }
}