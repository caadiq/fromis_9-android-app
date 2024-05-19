package com.beemer.unofficial.fromis_9.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.databinding.RowAlbumSongBinding

data class AlbumSong(val title: String, val content: String)

class AlbumSongAdapter : RecyclerView.Adapter<AlbumSongAdapter.ViewHolder>() {
    private var itemList = mutableListOf<AlbumSong>()

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowAlbumSongBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowAlbumSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AlbumSong) {
            binding.txtTitle.text = item.title
            binding.txtContent.text = if (item.title == "가사") item.content else addMiddleDot(item.content)
        }
    }

    fun addItem(item: AlbumSong) {
        itemList.add(item)
        notifyItemInserted(itemList.size - 1)
    }

    private fun addMiddleDot(originalText: String): String {
        return originalText.split("\n").mapIndexed { _, line -> "· $line" }.joinToString(separator = "\n")
    }
}