package com.beemer.unofficial.fromis_9.view.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.databinding.RowProgressBinding
import com.beemer.unofficial.fromis_9.databinding.RowVideoListBinding
import com.beemer.unofficial.fromis_9.model.dto.VideosDto
import com.beemer.unofficial.fromis_9.view.diff.VideoListDiffUtil
import com.beemer.unofficial.fromis_9.view.utils.DateTimeConverter.dateTimeToString
import com.beemer.unofficial.fromis_9.view.utils.DateTimeConverter.secToTime
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.util.Locale

class VideoListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemList = mutableListOf<VideosDto>()
    private var onItemClickListener: ((VideosDto, Int) -> Unit)? = null
    private var isLoading = false

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    override fun getItemCount(): Int {
        return if (isLoading) itemList.size + 1 else itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading && position == itemList.size) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding = RowVideoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolder(binding)
        } else {
            val binding = RowProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(itemList[position])
        }
    }

    inner class ViewHolder(private val binding: RowVideoListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: VideosDto) {
            Glide.with(binding.root).load(item.thumbnail)
                .placeholder(ColorDrawable(Color.TRANSPARENT))
                .transition(DrawableTransitionOptions.withCrossFade())
                .sizeMultiplier(0.5f)
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
                .into(binding.imgThumbnail)
            binding.txtTitle.text = item.title
            binding.txtPublishedAt.text = dateTimeToString(item.publishedAt, "yyyy-MM-dd'T'HH:mm:ss", "yyyy.MM.dd", Locale.KOREA)
            if (item.length != null) {
                binding.txtLength.visibility = View.VISIBLE
                binding.txtLength.text = secToTime(item.length)
            } else {
                binding.txtLength.visibility = View.GONE
            }
        }
    }

    inner class LoadingViewHolder(binding: RowProgressBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickListener(listener: (VideosDto, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<VideosDto>) {
        val diffCallback = VideoListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun showProgress() {
        isLoading = true
        notifyItemInserted(itemList.size)
    }

    fun hideProgress() {
        isLoading = false
        notifyItemRemoved(itemList.size)
    }
}