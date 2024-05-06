package com.beemer.unofficial.fromis_9.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.databinding.RowHomeBinding
import com.beemer.unofficial.fromis_9.model.dto.HomeDto
import com.beemer.unofficial.fromis_9.view.diff.HomeDiffUtil
import com.bumptech.glide.Glide

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private var itemList = mutableListOf<HomeDto>()
    private var onItemClickListener: ((HomeDto, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowHomeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: HomeDto) {
            Glide.with(binding.root).load(item.image).into(binding.image)
            binding.text.text = item.text
        }
    }

    fun setOnItemClickListener(listener: (HomeDto, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(newHome: List<HomeDto>) {
        val diffCallback = HomeDiffUtil(itemList, newHome)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(newHome)
        diffResult.dispatchUpdatesTo(this)
    }
}