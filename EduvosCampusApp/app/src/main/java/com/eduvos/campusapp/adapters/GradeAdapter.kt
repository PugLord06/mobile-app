package com.eduvos.campusapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eduvos.campusapp.database.GradeWithCourse
import com.eduvos.campusapp.databinding.ItemGradeBinding
import com.eduvos.campusapp.utils.toGradeColor

class GradeAdapter : ListAdapter<GradeWithCourse, GradeAdapter.GradeViewHolder>(GradeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeViewHolder {
        val binding = ItemGradeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GradeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GradeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class GradeViewHolder(
        private val binding: ItemGradeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(gradeWithCourse: GradeWithCourse) {
            binding.apply {
                tvCourseCode.text = gradeWithCourse.courseCode
                tvCourseName.text = gradeWithCourse.courseName
                tvGrade.text = gradeWithCourse.grade
                tvPercentage.text = "${gradeWithCourse.percentage.toInt()}%"
                tvCredits.text = "${gradeWithCourse.credits} Credits"
                tvSemester.text = "${gradeWithCourse.semester} ${gradeWithCourse.year}"
                
                // Set grade color
                try {
                    val gradeColor = Color.parseColor(gradeWithCourse.grade.toGradeColor())
                    cardGradeCircle.setCardBackgroundColor(gradeColor)
                } catch (e: Exception) {
                    cardGradeCircle.setCardBackgroundColor(Color.parseColor("#757575"))
                }
                
                // Set status
                tvStatus.text = gradeWithCourse.status
                when (gradeWithCourse.status) {
                    "Passed" -> tvStatus.setTextColor(Color.parseColor("#4CAF50"))
                    "Failed" -> tvStatus.setTextColor(Color.parseColor("#F44336"))
                    else -> tvStatus.setTextColor(Color.parseColor("#FFC107"))
                }
            }
        }
    }

    class GradeDiffCallback : DiffUtil.ItemCallback<GradeWithCourse>() {
        override fun areItemsTheSame(oldItem: GradeWithCourse, newItem: GradeWithCourse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GradeWithCourse, newItem: GradeWithCourse): Boolean {
            return oldItem == newItem
        }
    }
}