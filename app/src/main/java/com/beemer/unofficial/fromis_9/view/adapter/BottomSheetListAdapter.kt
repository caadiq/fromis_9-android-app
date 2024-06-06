package com.beemer.unofficial.fromis_9.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.databinding.RowBottomsheetBodyBinding
import com.beemer.unofficial.fromis_9.databinding.RowBottomsheetHeaderBinding
import com.beemer.unofficial.fromis_9.view.diff.StringListDiffUtil

class BottomSheetListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemList = mutableListOf<String>()
    private var onItemClickListener: ((String, Int) -> Unit)? = null

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_BODY = 1
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int = if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_BODY


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val binding = RowBottomsheetHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            HeaderViewHolder(binding)
        } else {
            val binding = RowBottomsheetBodyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            BodyViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.bind(itemList[position])
        } else if (holder is BodyViewHolder) {
            holder.bind(itemList[position])
        }
    }

    inner class HeaderViewHolder(private val binding: RowBottomsheetHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: String) {
            binding.txtHeader.text = item
        }
    }

    inner class BodyViewHolder(private val binding: RowBottomsheetBodyBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: String) {
            binding.txtText.text = item
        }
    }

    fun setOnItemClickListener(listener: (String, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<String>) {
        val diffCallback = StringListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}