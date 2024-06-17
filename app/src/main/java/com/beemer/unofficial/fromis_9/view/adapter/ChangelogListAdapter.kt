package com.beemer.unofficial.fromis_9.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.databinding.RowChangelogListBinding
import com.beemer.unofficial.fromis_9.model.dto.ChangelogListDto
import com.beemer.unofficial.fromis_9.view.diff.ChangelogListDiffUtil
import com.beemer.unofficial.fromis_9.view.utils.ItemDecoratorDivider

class ChangelogListAdapter : RecyclerView.Adapter<ChangelogListAdapter.ViewHolder>() {
    private var itemList = mutableListOf<ChangelogListDto>()
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowChangelogListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowChangelogListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val changelogFeatureListAdapter = ChangelogFeatureListAdapter()

        init {
            binding.recyclerView.apply {
                adapter = changelogFeatureListAdapter
                setRecycledViewPool(viewPool)
                setHasFixedSize(true)
                addItemDecoration(ItemDecoratorDivider(context, 0, 0, 0, 16, 0, 0, null))
            }
        }

        fun bind(item: ChangelogListDto) {
            changelogFeatureListAdapter.setItemList(item.changelog)
            binding.txtVersion.text = "v${item.version}"
            binding.txtDate.text = item.date.replace("-", ". ")
        }
    }

    fun setItemList(list: List<ChangelogListDto>) {
        val diffCallback = ChangelogListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}