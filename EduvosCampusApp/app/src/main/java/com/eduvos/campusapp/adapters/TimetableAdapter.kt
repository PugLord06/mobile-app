package com.eduvos.campusapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eduvos.campusapp.database.TimetableWithCourse
import com.eduvos.campusapp.databinding.ItemTimetableBinding

class TimetableAdapter : ListAdapter<TimetableWithCourse, TimetableAdapter.TimetableViewHolder>(
    TimetableDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimetableViewHolder {
        val binding = ItemTimetableBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TimetableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimetableViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TimetableViewHolder(
        private val binding: ItemTimetableBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: TimetableWithCourse) {
            binding.apply {
                tvTime.text = "${entry.startTime} - ${entry.endTime}"
                tvCourseCode.text = entry.courseCode
                tvCourseName.text = entry.courseName
                tvVenue.text = entry.venue
                tvClassType.text = entry.classType
                entry.lecturer?.let {
                    tvLecturer.text = it
                }
                
                // Set color stripe
                try {
                    viewColorStripe.setBackgroundColor(Color.parseColor(entry.color))
                } catch (e: Exception) {
                    viewColorStripe.setBackgroundColor(Color.parseColor("#1976D2"))
                }
            }
        }
    }

    class TimetableDiffCallback : DiffUtil.ItemCallback<TimetableWithCourse>() {
        override fun areItemsTheSame(
            oldItem: TimetableWithCourse, 
            newItem: TimetableWithCourse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: TimetableWithCourse, 
            newItem: TimetableWithCourse
        ): Boolean {
            return oldItem == newItem
        }
    }
}