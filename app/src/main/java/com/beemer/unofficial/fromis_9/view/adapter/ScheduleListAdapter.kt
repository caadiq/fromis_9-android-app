package com.beemer.unofficial.fromis_9.view.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.databinding.RowScheduleListBinding
import com.beemer.unofficial.fromis_9.model.dto.ScheduleListDto
import com.beemer.unofficial.fromis_9.view.diff.ScheduleListDiffUtil
import com.beemer.unofficial.fromis_9.view.utils.DateTimeConverter.dateTimeToString
import com.beemer.unofficial.fromis_9.view.utils.DensityConverter.dpToPx
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.util.Locale

class ScheduleListAdapter : RecyclerView.Adapter<ScheduleListAdapter.ViewHolder>() {
    private var itemList = mutableListOf<ScheduleListDto>()
    private var onItemClickListener: ((ScheduleListDto, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowScheduleListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowScheduleListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: ScheduleListDto) {
            Glide.with(binding.root)
                .load(item.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imgPlatform)

            val color = Color.parseColor("#${item.color}")
            val drawable = binding.viewIndicator.background as GradientDrawable
            drawable.setStroke(dpToPx(binding.root.context, 2f), color)

            binding.txtTime.apply {
                text = if (item.allDay) "종일" else dateTimeToString(item.dateTime, "yyyy-MM-dd'T'HH:mm:ss", "a h:mm", Locale.KOREA)
                setTextColor(color)
            }
            binding.txtSchedule.text = item.schedule
            binding.txtDescription.apply {
                text = item.description
                visibility = if (item.description == null) View.GONE else View.VISIBLE
            }
        }
    }

    fun setOnItemClickListener(listener: (ScheduleListDto, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<ScheduleListDto>) {
        val diffCallback = ScheduleListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}