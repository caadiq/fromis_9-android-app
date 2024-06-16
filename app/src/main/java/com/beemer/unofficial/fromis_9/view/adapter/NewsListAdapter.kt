package com.beemer.unofficial.fromis_9.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.databinding.RowHomeNewsBinding
import com.beemer.unofficial.fromis_9.databinding.RowNewsFooterBinding
import com.beemer.unofficial.fromis_9.model.dto.LatestNews
import com.beemer.unofficial.fromis_9.view.diff.NewsListDiffUtil
import com.beemer.unofficial.fromis_9.view.utils.DateTimeConverter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.time.LocalDateTime
import java.util.Locale

sealed class NewsItem {
    data class News(val item: LatestNews) : NewsItem()
    data object Footer : NewsItem()
}

class NewsListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemList = mutableListOf<NewsItem>()
    private var onItemClickListener: ((NewsItem.News, Int) -> Unit)? = null

    private val today = LocalDateTime.now()

    companion object {
        private const val VIEW_TYPE_BODY = 0
        private const val VIEW_TYPE_FOOTER = 1
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int) : Int = if (position == itemList.size - 1 && itemList[position] is NewsItem.Footer) VIEW_TYPE_FOOTER else VIEW_TYPE_BODY

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_BODY) {
            val binding = RowHomeNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            BodyViewHolder(binding)
        } else {
            val binding = RowNewsFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            FooterViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = itemList[position]) {
            is NewsItem.News -> (holder as BodyViewHolder).bind(item.item)
            is NewsItem.Footer -> Unit
        }
    }

    inner class BodyViewHolder(private val binding: RowHomeNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position] as NewsItem.News, position)
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

    inner class FooterViewHolder(binding: RowNewsFooterBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickListener(listener: (NewsItem.News, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<NewsItem>) {
        val diffCallback = NewsListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        itemList.add(NewsItem.Footer)
        diffResult.dispatchUpdatesTo(this)
    }
}