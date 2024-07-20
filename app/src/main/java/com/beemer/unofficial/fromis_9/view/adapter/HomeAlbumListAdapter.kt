package com.beemer.unofficial.fromis_9.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.RowHomeAlbumBinding
import com.beemer.unofficial.fromis_9.databinding.RowHomeAlbumMoreBinding
import com.beemer.unofficial.fromis_9.model.dto.AlbumListDto
import com.beemer.unofficial.fromis_9.view.diff.HomeAlbumListDiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

sealed class HomeAlbumItem {
    data class Album(val item: AlbumListDto) : HomeAlbumItem()
    data object More : HomeAlbumItem()
}

class HomeAlbumListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemList = mutableListOf<HomeAlbumItem>()
    private var onAlbumClickListener: ((HomeAlbumItem.Album, Int) -> Unit)? = null
    private var onMoreClickListener: (() -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int {
        return if (position == itemList.size - 1 && itemList[position] is HomeAlbumItem.More) 2 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            1 -> {
                val binding = RowHomeAlbumBinding.inflate(inflater, parent, false)
                AlbumViewHolder(binding)
            }
            2 -> {
                val binding = RowHomeAlbumMoreBinding.inflate(inflater, parent, false)
                MoreViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = itemList[position]) {
            is HomeAlbumItem.Album -> (holder as AlbumViewHolder).bind(item)
            is HomeAlbumItem.More -> Unit
        }
    }

    inner class AlbumViewHolder(private val binding: RowHomeAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onAlbumClickListener?.invoke(itemList[position] as HomeAlbumItem.Album, position)
                }
            }
        }

        fun bind(item: HomeAlbumItem.Album) {
            Glide.with(binding.root)
                .load(item.item.cover)
                .error(R.drawable.icon_fromis9_gray)
                .fallback(R.drawable.icon_fromis9_gray)
                .placeholder(android.R.color.transparent)
                .transition(DrawableTransitionOptions.withCrossFade())
                .sizeMultiplier(0.7f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imgAlbum)
            binding.txtAlbum.text = item.item.albumName
            binding.txtYear.text = item.item.release
        }
    }

    inner class MoreViewHolder(binding: RowHomeAlbumMoreBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                onMoreClickListener?.invoke()
            }
        }
    }

    fun setOnAlbumClickListener(listener: (HomeAlbumItem.Album, Int) -> Unit) {
        onAlbumClickListener = listener
    }

    fun setOnMoreClickListener(listener: () -> Unit) {
        onMoreClickListener = listener
    }

    fun setItemList(list: List<HomeAlbumItem>) {
        val diffCallback = HomeAlbumListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        itemList.add(HomeAlbumItem.More)
        diffResult.dispatchUpdatesTo(this)
    }
}