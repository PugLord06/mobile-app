package com.eduvos.campusapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eduvos.campusapp.R
import com.eduvos.campusapp.databinding.ItemAnnouncementBinding
import com.eduvos.campusapp.models.Announcement
import com.eduvos.campusapp.utils.toTimeAgo

class AnnouncementAdapter : ListAdapter<Announcement, AnnouncementAdapter.AnnouncementViewHolder>(
    AnnouncementDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val binding = ItemAnnouncementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AnnouncementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AnnouncementViewHolder(
        private val binding: ItemAnnouncementBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(announcement: Announcement) {
            binding.apply {
                tvTitle.text = announcement.title
                tvContent.text = announcement.content
                tvAuthor.text = "By ${announcement.author}"
                tvTime.text = announcement.timestamp.toTimeAgo()
                
                // Set priority color
                when (announcement.priority) {
                    "High" -> {
                        viewPriorityIndicator.setBackgroundColor(
                            ContextCompat.getColor(root.context, R.color.error)
                        )
                    }
                    "Normal" -> {
                        viewPriorityIndicator.setBackgroundColor(
                            ContextCompat.getColor(root.context, R.color.primary)
                        )
                    }
                    "Low" -> {
                        viewPriorityIndicator.setBackgroundColor(
                            ContextCompat.getColor(root.context, R.color.text_secondary)
                        )
                    }
                }
                
                // Set category chip
                chipCategory.text = announcement.category
                
                // Show course if available
                announcement.courseCode?.let {
                    tvCourse.text = it
                } ?: run {
                    tvCourse.text = "General"
                }
            }
        }
    }

    class AnnouncementDiffCallback : DiffUtil.ItemCallback<Announcement>() {
        override fun areItemsTheSame(oldItem: Announcement, newItem: Announcement): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Announcement, newItem: Announcement): Boolean {
            return oldItem == newItem
        }
    }
}