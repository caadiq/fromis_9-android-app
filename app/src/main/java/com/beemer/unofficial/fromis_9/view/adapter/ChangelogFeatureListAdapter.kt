package com.beemer.unofficial.fromis_9.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.databinding.RowChangelogFeatureListBinding
import com.beemer.unofficial.fromis_9.model.dto.Changelog
import com.beemer.unofficial.fromis_9.view.diff.ChangelogFeatureListDiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class ChangelogFeatureListAdapter : RecyclerView.Adapter<ChangelogFeatureListAdapter.ViewHolder>() {
    private var itemList = mutableListOf<Changelog>()

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowChangelogFeatureListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowChangelogFeatureListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Changelog) {
            Glide.with(binding.root)
                .load(item.icon)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imgIcon)

            binding.txtType.text = item.type
            binding.txtFeature.text = item.feature
        }
    }

    fun setItemList(list: List<Changelog>) {
        val diffCallback = ChangelogFeatureListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}