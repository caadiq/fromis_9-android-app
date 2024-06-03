package com.beemer.unofficial.fromis_9.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.databinding.RowHomeMemberBinding
import com.beemer.unofficial.fromis_9.model.dto.Member
import com.beemer.unofficial.fromis_9.view.diff.HomeMemberListDiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class HomeMemberListAdapter : RecyclerView.Adapter<HomeMemberListAdapter.ViewHolder>() {
    private var itemList = mutableListOf<Member>()
    private var onItemClickListener: ((Member, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowHomeMemberBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowHomeMemberBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: Member) {
            Glide.with(binding.root)
                .load(item.profileImage)
                .placeholder(android.R.color.transparent)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.imgProfile)
            binding.txtName.text = item.name
        }
    }

    fun setOnItemClickListener(listener: (Member, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<Member>) {
        val diffCallback = HomeMemberListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}