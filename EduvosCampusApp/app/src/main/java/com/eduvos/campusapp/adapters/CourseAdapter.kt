package com.eduvos.campusapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eduvos.campusapp.databinding.ItemCourseBinding
import com.eduvos.campusapp.models.Course

class CourseAdapter(
    private val onCourseClick: (Course) -> Unit
) : ListAdapter<Course, CourseAdapter.CourseViewHolder>(CourseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseBinding.inflate(
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
        private val binding: ItemCourseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(course: Course) {
            binding.apply {
                tvCourseCode.text = course.courseCode
                tvCourseName.text = course.courseName
                tvLecturer.text = course.lecturer
                tvCredits.text = "${course.credits} Credits"
                
                // Set card background color
                try {
                    cardCourse.setCardBackgroundColor(Color.parseColor(course.color))
                } catch (e: Exception) {
                    // Default color if parsing fails
                    cardCourse.setCardBackgroundColor(Color.parseColor("#1976D2"))
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