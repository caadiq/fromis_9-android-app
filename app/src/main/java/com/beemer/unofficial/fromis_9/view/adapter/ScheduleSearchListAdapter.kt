package com.beemer.unofficial.fromis_9.view.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.databinding.RowProgressBinding
import com.beemer.unofficial.fromis_9.databinding.RowScheduleSearchListBinding
import com.beemer.unofficial.fromis_9.model.dto.ScheduleDto
import com.beemer.unofficial.fromis_9.view.diff.ScheduleListDiffUtil
import com.beemer.unofficial.fromis_9.view.utils.DateTimeConverter.dateTimeToString
import com.beemer.unofficial.fromis_9.view.utils.DensityConverter.dpToPx
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.util.Locale

class ScheduleSearchListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemList = mutableListOf<ScheduleDto>()
    private var onItemClickListener: ((ScheduleDto, Int) -> Unit)? = null
    private var isLoading = false

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    override fun getItemCount(): Int = if (isLoading) itemList.size + 1 else itemList.size

    override fun getItemViewType(position: Int): Int = if (isLoading && position == itemList.size) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding = RowScheduleSearchListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolder(binding)
        } else {
            val binding = RowProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(itemList[position])
        }
    }

    inner class ViewHolder(private val binding: RowScheduleSearchListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: ScheduleDto) {
            Glide.with(binding.root)
                .load(item.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imgPlatform)

            val color = Color.parseColor("#${item.color}")
            val drawable = binding.viewIndicator.background as GradientDrawable
            drawable.setStroke(dpToPx(binding.root.context, 2f), color)

            binding.txtTime.apply {
                text = if (item.allDay) "종일" else dateTimeToString(item.dateTime, "yyyy-MM-dd'T'HH:mm:ss", "yyyy.MM.dd  a h:mm", Locale.KOREA)
                setTextColor(color)
            }
            binding.txtSchedule.text = item.schedule
            binding.txtDescription.apply {
                text = item.description
                visibility = if (item.description == null) View.GONE else View.VISIBLE
            }
        }
    }

    inner class LoadingViewHolder(binding: RowProgressBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickListener(listener: (ScheduleDto, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<ScheduleDto>) {
        val diffCallback = ScheduleListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun showProgress() {
        isLoading = true
        notifyItemInserted(itemList.size)
    }

    fun hideProgress() {
        isLoading = false
        notifyItemRemoved(itemList.size)
    }
}