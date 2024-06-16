package com.beemer.unofficial.fromis_9.view.diff

import androidx.recyclerview.widget.DiffUtil
import com.beemer.unofficial.fromis_9.view.adapter.NewsItem

class NewsListDiffUtil(private val oldList: List<NewsItem>, private val newList: List<NewsItem>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]::class == newList[newItemPosition]::class
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}