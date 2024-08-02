package com.beemer.unofficial.fromis_9.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.databinding.RowHomeNewsBinding
import com.beemer.unofficial.fromis_9.databinding.RowHomeNewsMoreBinding
import com.beemer.unofficial.fromis_9.model.dto.LatestNews
import com.beemer.unofficial.fromis_9.view.diff.HomeNewsListDiffUtil
import com.beemer.unofficial.fromis_9.view.utils.DateTimeConverter.dateTimeToString
import com.beemer.unofficial.fromis_9.view.utils.DateTimeConverter.stringToDateTime
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.time.LocalDateTime
import java.util.Locale

sealed class HomeLatestNewsItem {
    data class News(val item: LatestNews) : HomeLatestNewsItem()
    data object More : HomeLatestNewsItem()
}

class HomeNewsListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemList = mutableListOf<HomeLatestNewsItem>()
    private var onNewsClickListener: ((HomeLatestNewsItem.News, Int) -> Unit)? = null
    private var onMoreClickListener: (() -> Unit)? = null

    private val today = LocalDateTime.now()

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int {
        return if (position == itemList.size - 1 && itemList[position] is HomeLatestNewsItem.More) 2 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            1 -> {
                val binding = RowHomeNewsBinding.inflate(inflater, parent, false)
                NewsViewHolder(binding)
            }
            2 -> {
                val binding = RowHomeNewsMoreBinding.inflate(inflater, parent, false)
                MoreViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = itemList[position]) {
            is HomeLatestNewsItem.News -> (holder as NewsViewHolder).bind(item)
            is HomeLatestNewsItem.More -> Unit
        }
    }

    inner class NewsViewHolder(private val binding: RowHomeNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onNewsClickListener?.invoke(itemList[position] as HomeLatestNewsItem.News, position)
                }
            }
        }

        fun bind(item: HomeLatestNewsItem.News) {
            Glide.with(binding.root)
                .load(item.item.portalImage)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imgPortal)
            binding.txtPortal.text = item.item.portal
            binding.txtTitle.text = item.item.title
            binding.txtDate.text = if (stringToDateTime(item.item.date, "yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA).toLocalDate() == today.toLocalDate()) {
                if (item.item.portal == "위버스 공지사항")
                    dateTimeToString(item.item.date, "yyyy-MM-dd'T'HH:mm:ss", "yyyy.MM.dd", Locale.KOREA)
                else
                    dateTimeToString(item.item.date, "yyyy-MM-dd'T'HH:mm:ss", "HH:mm", Locale.KOREA)
            } else {
                dateTimeToString(item.item.date, "yyyy-MM-dd'T'HH:mm:ss", "yyyy.MM.dd", Locale.KOREA)
            }
        }
    }

    inner class MoreViewHolder(binding: RowHomeNewsMoreBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                onMoreClickListener?.invoke()
            }
        }
    }

    fun setOnNewsClickListener(listener: (HomeLatestNewsItem.News, Int) -> Unit) {
        onNewsClickListener = listener
    }

    fun setOnMoreClickListener(listener: () -> Unit) {
        onMoreClickListener = listener
    }

    fun setItemList(list: List<HomeLatestNewsItem>) {
        val diffCallback = HomeNewsListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        itemList.add(HomeLatestNewsItem.More)
        diffResult.dispatchUpdatesTo(this)
    }
}