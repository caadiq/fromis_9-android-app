package com.beemer.unofficial.fromis_9.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.RowScheduleCategoryListBinding
import com.beemer.unofficial.fromis_9.view.diff.ScheduleCategoryListDiffUtil

data class Category(
    val category: String,
    var isSelected: Boolean
)

class ScheduleCategoryListAdapter : RecyclerView.Adapter<ScheduleCategoryListAdapter.ViewHolder>() {
    private var itemList = mutableListOf<Category>()
    private var onItemClickListener: ((Category, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowScheduleCategoryListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowScheduleCategoryListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: Category) {
            binding.txtCategory.apply {
                text = item.category
                setTextColor(ResourcesCompat.getColor(resources, if (item.isSelected) R.color.white else R.color.dark_gray, null))
                setBackgroundResource(if (item.isSelected) R.drawable.rectangle_primary_rounded_20dp_ripple else R.drawable.rectangle_lightgray_rounded_20dp_ripple)
            }
        }
    }

    fun setOnItemClickListener(listener: (Category, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<Category>) {
        val diffCallback = ScheduleCategoryListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getItemList(): List<Category> {
        return itemList
    }

    fun setItemSelected(position: Int) {
        itemList[position] = itemList[position].copy(isSelected = !itemList[position].isSelected)
        notifyItemChanged(position)
    }
}