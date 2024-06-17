package com.beemer.unofficial.fromis_9.view.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.databinding.RowAlbumSongCreditsBinding
import com.beemer.unofficial.fromis_9.databinding.RowAlbumSongLyricsBinding
import com.beemer.unofficial.fromis_9.databinding.RowAlbumSongTitleBinding
import com.beemer.unofficial.fromis_9.databinding.RowAlbumSongVideoBinding
import com.beemer.unofficial.fromis_9.view.diff.AlbumSongDiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

sealed class AlbumSongItem {
    data class Title(val title: String) : AlbumSongItem()
    data class Credits(val credits: String, val people: String) : AlbumSongItem()
    data class Lyrics(val lyrics: String) : AlbumSongItem()
    data class FanChant(val fanChant: String) : AlbumSongItem()
    data class Video(val videoId: String) : AlbumSongItem()
}

class AlbumSongAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemList = mutableListOf<AlbumSongItem>()
    private var onItemClickListener: ((AlbumSongItem, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int {
        return when (itemList[position]) {
            is AlbumSongItem.Title -> 0
            is AlbumSongItem.Credits -> 1
            is AlbumSongItem.Lyrics -> 2
            is AlbumSongItem.FanChant -> 3
            is AlbumSongItem.Video -> 4
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> {
                val binding = RowAlbumSongTitleBinding.inflate(inflater, parent, false)
                TitleViewHolder(binding)
            }
            1 -> {
                val binding = RowAlbumSongCreditsBinding.inflate(inflater, parent, false)
                CreditsViewHolder(binding)
            }
            2 -> {
                val binding = RowAlbumSongLyricsBinding.inflate(inflater, parent, false)
                LyricsViewHolder(binding)
            }
            3 -> {
                val binding = RowAlbumSongLyricsBinding.inflate(inflater, parent, false)
                FanchantViewHolder(binding)
            }
            4 -> {
                val binding = RowAlbumSongVideoBinding.inflate(inflater, parent, false)
                VideoViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = itemList[position]) {
            is AlbumSongItem.Title -> (holder as TitleViewHolder).bind(item)
            is AlbumSongItem.Credits -> (holder as CreditsViewHolder).bind(item)
            is AlbumSongItem.Lyrics -> (holder as LyricsViewHolder).bind(item)
            is AlbumSongItem.FanChant -> (holder as FanchantViewHolder).bind(item)
            is AlbumSongItem.Video -> (holder as VideoViewHolder).bind(item)
        }
    }

    inner class TitleViewHolder(private val binding: RowAlbumSongTitleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AlbumSongItem.Title) {
            binding.txtTitle.text = item.title
        }
    }

    inner class CreditsViewHolder(private val binding: RowAlbumSongCreditsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AlbumSongItem.Credits) {
            binding.txtCredits.text = item.credits
            binding.txtPeople.text = item.people.replace("\r", "")
        }
    }

    inner class LyricsViewHolder(private val binding: RowAlbumSongLyricsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AlbumSongItem.Lyrics) {
            binding.txtLyrics.text = item.lyrics
        }
    }

    inner class FanchantViewHolder(private val binding: RowAlbumSongLyricsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AlbumSongItem.FanChant) {
            binding.txtLyrics.text = HtmlCompat.fromHtml(item.fanChant, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    inner class VideoViewHolder(private val binding: RowAlbumSongVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: AlbumSongItem.Video) {
            Glide.with(binding.root)
                .load("https://i.ytimg.com/vi/${item.videoId}/maxresdefault.jpg")
                .error("https://i.ytimg.com/vi/${item.videoId}/mqdefault.jpg")
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                        binding.progressIndicator.hide()
                        binding.imgThumbnail.foreground = ColorDrawable(Color.parseColor("#80000000"))
                        binding.imgPlay.visibility = View.VISIBLE
                        return false
                    }

                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        binding.progressIndicator.hide()
                        binding.imgThumbnail.foreground = ColorDrawable(Color.parseColor("#80000000"))
                        binding.imgPlay.visibility = View.VISIBLE
                        return false
                    }
                })
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imgThumbnail)
        }
    }

    fun setOnItemClickListener(listener: (AlbumSongItem, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<AlbumSongItem>) {
        val diffCallback = AlbumSongDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}