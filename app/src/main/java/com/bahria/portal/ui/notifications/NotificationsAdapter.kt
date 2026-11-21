package com.bahria.portal.ui.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bahria.portal.R
import com.bahria.portal.data.model.NotificationItem
import com.bahria.portal.databinding.ItemNotificationBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationsAdapter : ListAdapter<NotificationItem, NotificationsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NotificationItem) {
            val context = binding.root.context
            binding.tvNotifTitle.text = item.title
            binding.tvNotifMessage.text = item.message

            val sdf = SimpleDateFormat("dd MMM yyyy • hh:mm a", Locale.getDefault())
            binding.tvNotifTime.text = sdf.format(Date(item.timestamp))

            when (item.type) {
                "ATTENDANCE" -> {
                    binding.ivNotifType.setImageResource(R.drawable.ic_warning)
                    binding.ivNotifType.setColorFilter(ContextCompat.getColor(context, R.color.status_warning))
                }
                "ASSIGNMENT" -> {
                    binding.ivNotifType.setImageResource(R.drawable.ic_assignment)
                    binding.ivNotifType.setColorFilter(ContextCompat.getColor(context, R.color.brand_primary_variant))
                }
                else -> {
                    binding.ivNotifType.setImageResource(R.drawable.ic_notifications)
                    binding.ivNotifType.setColorFilter(ContextCompat.getColor(context, R.color.brand_secondary))
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<NotificationItem>() {
        override fun areItemsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
            return oldItem == newItem
        }
    }
}
