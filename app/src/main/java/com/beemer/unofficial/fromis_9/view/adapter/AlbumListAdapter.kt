package com.beemer.unofficial.fromis_9.view.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.databinding.RowAlbumListBinding
import com.beemer.unofficial.fromis_9.model.dto.AlbumListDto
import com.beemer.unofficial.fromis_9.view.diff.AlbumListDiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class AlbumListAdapter : RecyclerView.Adapter<AlbumListAdapter.ViewHolder>() {
    private var itemList = mutableListOf<AlbumListDto>()
    private var onItemClickListener: ((AlbumListDto, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowAlbumListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowAlbumListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: AlbumListDto) {
            Glide.with(binding.root)
                .load(item.cover)
                .transition(DrawableTransitionOptions.withCrossFade())
                .sizeMultiplier(0.5f)
                .into(binding.imgCover)
            binding.txtType.apply {
                text = item.type

                val colorPrimary = Color.parseColor("#${item.colorPrimary}")
                val colorSecondary = Color.parseColor("#${item.colorSecondary}")

                setTextColor(colorSecondary)
                backgroundTintList = ColorStateList.valueOf(colorPrimary)
            }
            binding.txtName.text = item.albumName
            binding.txtRelease.text = item.release
        }
    }

    fun setOnItemClickListener(listener: (AlbumListDto, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(newAlbumList: List<AlbumListDto>) {
        val diffCallback = AlbumListDiffUtil(itemList, newAlbumList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(newAlbumList)
        diffResult.dispatchUpdatesTo(this)
    }
}