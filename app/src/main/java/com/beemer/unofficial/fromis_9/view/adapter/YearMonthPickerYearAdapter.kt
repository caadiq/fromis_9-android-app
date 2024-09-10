package com.beemer.unofficial.fromis_9.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.databinding.RowYearmonthPickerYearBinding
import com.beemer.unofficial.fromis_9.view.diff.IntListDiffUtil

class YearMonthPickerYearAdapter() : RecyclerView.Adapter<YearMonthPickerYearAdapter.ViewHolder>() {
    private var itemList = mutableListOf<Int>()

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowYearmonthPickerYearBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowYearmonthPickerYearBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Int) {
            binding.txtYear.text = item.toString()
        }
    }

    fun setItemList(list: List<Int>) {
        val diffCallBack = IntListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}