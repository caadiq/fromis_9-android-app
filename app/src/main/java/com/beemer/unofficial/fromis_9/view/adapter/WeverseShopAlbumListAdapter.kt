package com.beemer.unofficial.fromis_9.view.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.databinding.RowWeverseShopAlbumListBinding
import com.beemer.unofficial.fromis_9.model.dto.WeverseShopAlbumListDto
import com.beemer.unofficial.fromis_9.view.diff.WeverseShopAlbumListDiffUtil
import com.beemer.unofficial.fromis_9.view.utils.NumberFormatter.formatNumber
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class WeverseShopAlbumListAdapter : RecyclerView.Adapter<WeverseShopAlbumListAdapter.ViewHolder>() {
    private var itemList = mutableListOf<WeverseShopAlbumListDto>()
    private var onItemClickListener: ((WeverseShopAlbumListDto, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowWeverseShopAlbumListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowWeverseShopAlbumListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: WeverseShopAlbumListDto) {
            binding.progressIndicator.show()

            Glide.with(binding.root).load(item.imgSrc)
                .placeholder(ColorDrawable(Color.TRANSPARENT))
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                        binding.progressIndicator.hide()
                        return false
                    }

                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        binding.progressIndicator.hide()
                        return false
                    }
                })
                .centerCrop()
                .into(binding.imgAlbum)
            binding.txtTitle.text = item.title
            binding.txtPrice.text = "${formatNumber(item.price)}원"
            binding.txtSoldOut.visibility = if (item.isSoldOut) View.VISIBLE else View.GONE
        }
    }

    fun setOnItemClickListener(listener: (WeverseShopAlbumListDto, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<WeverseShopAlbumListDto>) {
        val diffCallback = WeverseShopAlbumListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}