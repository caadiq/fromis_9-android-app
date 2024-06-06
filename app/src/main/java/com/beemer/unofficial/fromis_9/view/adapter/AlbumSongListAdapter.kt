package com.beemer.unofficial.fromis_9.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.databinding.RowAlbumSongListBinding
import com.beemer.unofficial.fromis_9.model.dto.AlbumSongListDto
import com.beemer.unofficial.fromis_9.view.diff.AlbumSongListDiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class AlbumSongListAdapter : RecyclerView.Adapter<AlbumSongListAdapter.ViewHolder>(), Filterable {
    private var itemList = mutableListOf<AlbumSongListDto>()
    private var filteredItemList = mutableListOf<AlbumSongListDto>()
    private var onItemClickListener: ((AlbumSongListDto, Int) -> Unit)? = null

    override fun getItemCount(): Int = filteredItemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowAlbumSongListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredItemList[position])
    }

    inner class ViewHolder(private val binding: RowAlbumSongListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: AlbumSongListDto) {
            Glide.with(binding.root)
                .load(item.albumCover)
                .transition(DrawableTransitionOptions.withCrossFade())
                .sizeMultiplier(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imgCover)

            binding.txtSong.text = item.songName
            binding.txtAlbum.text = item.albumName
        }
    }

    fun setOnItemClickListener(listener: (AlbumSongListDto, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<AlbumSongListDto>) {
        val diffCallback = AlbumSongListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        filteredItemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun setFilteredList(list: List<AlbumSongListDto>) {
        val diffCallback = AlbumSongListDiffUtil(filteredItemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        filteredItemList.clear()
        filteredItemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<AlbumSongListDto>()
                val query = constraint.toString().lowercase().replace(" ", "")

                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(itemList)
                } else {
                    itemList.forEach {
                        if (it.songName.lowercase().replace(" ", "")
                                .contains(query) || it.albumName.lowercase().replace(" ", "")
                                .contains(query)
                        ) {
                            filteredList.add(it)
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                val filteredList = results?.values as? List<AlbumSongListDto>
                filteredList?.let { setFilteredList(it) }
            }
        }
    }
}