package com.beemer.unofficial.fromis_9.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.databinding.RowHomeNewsBinding
import com.beemer.unofficial.fromis_9.model.dto.LatestNews
import com.beemer.unofficial.fromis_9.view.diff.NewsListDiffUtil
import com.beemer.unofficial.fromis_9.view.utils.DateTimeConverter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.time.LocalDateTime
import java.util.Locale

class NewsListAdapter : RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {
    private var itemList = mutableListOf<LatestNews>()
    private var onItemClickListener: ((LatestNews, Int) -> Unit)? = null

    private val today = LocalDateTime.now()

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowHomeNewsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowHomeNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: LatestNews) {
            Glide.with(binding.root)
                .load(item.portalImage)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imgPortal)
            binding.txtPortal.text = item.portal
            binding.txtTitle.text = item.title
            binding.txtDate.text = if (DateTimeConverter.stringToDateTime(item.date, "yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA).toLocalDate() == today.toLocalDate()) {
                DateTimeConverter.dateTimeToString(item.date, "yyyy-MM-dd'T'HH:mm:ss", "HH:mm", Locale.KOREA)
            } else {
                DateTimeConverter.dateTimeToString(item.date, "yyyy-MM-dd'T'HH:mm:ss", "yyyy.MM.dd", Locale.KOREA)
            }
        }
    }

    fun setOnItemClickListener(listener: (LatestNews, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<LatestNews>) {
        val diffCallback = NewsListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}