package com.beemer.unofficial.fromis_9.view.diff

import androidx.recyclerview.widget.DiffUtil
import com.beemer.unofficial.fromis_9.model.dto.Member

class MemberListDiffUtil(private val oldList: List<Member>, private val newList: List<Member>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}