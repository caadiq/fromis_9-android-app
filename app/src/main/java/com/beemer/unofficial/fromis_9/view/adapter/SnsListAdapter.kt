package com.beemer.unofficial.fromis_9.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.databinding.RowSnsListBinding
import com.beemer.unofficial.fromis_9.model.dto.Social
import com.beemer.unofficial.fromis_9.view.diff.SnsListDiffUtil

class SnsListAdapter : RecyclerView.Adapter<SnsListAdapter.ViewHolder>() {
    private var itemList = mutableListOf<Social>()
    private var onItemClickListener: ((Social, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowSnsListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowSnsListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: Social) {
            binding.txtSns.text = item.sns
                .replace("facebook", "Facebook")
                .replace("insta", "Instagram")
                .replace("twitter", "X")
                .replace("youtube", "YouTube")
        }
    }

    fun setOnItemClickListener(listener: (Social, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<Social>) {
        val diffCallback = SnsListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}