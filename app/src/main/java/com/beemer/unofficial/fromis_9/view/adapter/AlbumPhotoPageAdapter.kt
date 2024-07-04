package com.beemer.unofficial.fromis_9.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.RowAlbumPhotoPageBinding
import com.beemer.unofficial.fromis_9.model.dto.PhotoListDto
import com.beemer.unofficial.fromis_9.view.diff.AlbumPhotoListDiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class AlbumPhotoPageAdapter : RecyclerView.Adapter<AlbumPhotoPageAdapter.ViewHolder>() {
    private var itemList = mutableListOf<PhotoListDto>()

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowAlbumPhotoPageBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowAlbumPhotoPageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PhotoListDto) {
            Glide.with(binding.root)
                .load(item.photo)
                .preload()

            Glide.with(binding.root)
                .load(item.photo)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.icon_fromis9_primary)
                .into(binding.imgPhoto)
            binding.txtConcept.apply {
                text = item.concept
                visibility = if (item.concept.isEmpty()) View.GONE else View.VISIBLE
            }
        }
    }

    fun setItemList(list: List<PhotoListDto>) {
        val diffCallBack = AlbumPhotoListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}