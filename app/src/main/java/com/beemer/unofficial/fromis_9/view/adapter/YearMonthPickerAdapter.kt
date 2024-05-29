package com.beemer.unofficial.fromis_9.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.RowYearmonthPickerBinding
import com.beemer.unofficial.fromis_9.view.diff.YearMonthPickerDiffUtil

data class MonthPicker (
    val month: String,
    var selected: Boolean
)

class YearMonthPickerAdapter : RecyclerView.Adapter<YearMonthPickerAdapter.ViewHolder>() {
    private var itemList = mutableListOf<MonthPicker>()
    private var onItemClickListener: ((MonthPicker, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowYearmonthPickerBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowYearmonthPickerBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: MonthPicker) {
            binding.txtMonth.apply {
                text = item.month
                if (item.selected) {
                    setTextColor(context.getColor(R.color.white))
                    setBackgroundResource(R.drawable.oval_primary_filled)
                } else {
                    setTextColor(context.getColor(R.color.dark_gray))
                    setBackgroundResource(android.R.color.transparent)
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (MonthPicker, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<MonthPicker>) {
        val diffCallback = YearMonthPickerDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setItemSelected(position: Int) {
        val previousSelected = itemList.indexOfFirst { it.selected }
        if (previousSelected != -1) {
            itemList[previousSelected].selected = false
            notifyItemChanged(previousSelected)
        }
        itemList[position].selected = true
        notifyItemChanged(position)
    }

    fun getSelectedItem(): Int {
        return itemList.indexOfFirst { it.selected }
    }
}