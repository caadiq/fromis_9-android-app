package com.beemer.unofficial.fromis_9.view.diff

import androidx.recyclerview.widget.DiffUtil
import com.beemer.unofficial.fromis_9.view.adapter.HomeLatestNewsItem

class HomeNewsListDiffUtil(private val oldList: List<HomeLatestNewsItem>, private val newList: List<HomeLatestNewsItem>) : DiffUtil.Callback() {
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