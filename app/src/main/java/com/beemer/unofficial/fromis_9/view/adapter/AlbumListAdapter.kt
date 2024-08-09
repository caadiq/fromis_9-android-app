package com.beemer.unofficial.fromis_9.view.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.RowAlbumListBinding
import com.beemer.unofficial.fromis_9.model.dto.AlbumListDto
import com.beemer.unofficial.fromis_9.view.diff.AlbumListDiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class AlbumListAdapter : RecyclerView.Adapter<AlbumListAdapter.ViewHolder>(), Filterable {
    private var itemList = mutableListOf<AlbumListDto>()
    private var filteredItemList = mutableListOf<AlbumListDto>()
    private var onItemClickListener: ((AlbumListDto, Int) -> Unit)? = null

    override fun getItemCount(): Int = filteredItemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowAlbumListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredItemList[position])
    }

    inner class ViewHolder(private val binding: RowAlbumListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(filteredItemList[position], position)
                }
            }
        }

        fun bind(item: AlbumListDto) {
            binding.progressIndicator.show()

            Glide.with(binding.root)
                .load(item.cover)
                .error(R.drawable.icon_fromis9_gray)
                .fallback(R.drawable.icon_fromis9_gray)
                .transition(DrawableTransitionOptions.withCrossFade())
                .sizeMultiplier(0.7f)
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
                .diskCacheStrategy(DiskCacheStrategy.ALL)
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

    fun setItemList(list: List<AlbumListDto>) {
        val diffCallback = AlbumListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        filteredItemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun setFilteredList(list: List<AlbumListDto>) {
        val diffCallback = AlbumListDiffUtil(filteredItemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        filteredItemList.clear()
        filteredItemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<AlbumListDto>()
                val query = constraint.toString().lowercase().replace(" ", "")

                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(itemList)
                } else {
                    itemList.forEach {
                        if (it.type.lowercase().replace(" ", "").contains(query)) {
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
                val filteredList = results?.values as? List<AlbumListDto>
                filteredList?.let { setFilteredList(it) }
            }
        }
    }

    fun getItemSize(): Int = filteredItemList.size
}