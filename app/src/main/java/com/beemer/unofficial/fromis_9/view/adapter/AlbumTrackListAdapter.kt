package com.beemer.unofficial.fromis_9.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.databinding.RowAlbumTrackListBinding
import com.beemer.unofficial.fromis_9.model.dto.TrackListDto
import com.beemer.unofficial.fromis_9.view.diff.AlbumTrackListDiffUtil

class AlbumTrackListAdapter : RecyclerView.Adapter<AlbumTrackListAdapter.ViewHolder>() {
    private var itemList = mutableListOf<TrackListDto>()
    private var onItemClickListener: ((TrackListDto, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowAlbumTrackListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowAlbumTrackListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: TrackListDto) {
            binding.txtTrackNumber.text = item.trackNumber.toString()
            binding.txtSongName.text = item.songName
            binding.txtSongLength.text = item.length
            binding.viewTitleTrack.apply {
                setBackgroundColor(Color.parseColor("#${item.colorMain}"))
                visibility = if (item.titleTrack) View.VISIBLE else View.INVISIBLE
            }
        }
    }

    fun setOnItemClickListener(listener: (TrackListDto, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<TrackListDto>) {
        val diffCallback = AlbumTrackListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}