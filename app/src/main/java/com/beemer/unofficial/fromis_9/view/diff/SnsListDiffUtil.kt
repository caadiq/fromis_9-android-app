package com.beemer.unofficial.fromis_9.view.diff

import androidx.recyclerview.widget.DiffUtil
import com.beemer.unofficial.fromis_9.model.dto.Social

class SnsListDiffUtil(private val oldList: List<Social>, private val newList: List<Social>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].sns == newList[newItemPosition].sns
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}