package com.beemer.unofficial.fromis_9.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.databinding.RowHomeAlbumListBinding
import com.beemer.unofficial.fromis_9.databinding.RowHomeDebutBinding
import com.beemer.unofficial.fromis_9.databinding.RowHomeMemberListBinding
import com.beemer.unofficial.fromis_9.databinding.RowHomeNewsListBinding
import com.beemer.unofficial.fromis_9.databinding.RowHomeTitleBinding
import com.beemer.unofficial.fromis_9.model.dto.AlbumListDto
import com.beemer.unofficial.fromis_9.model.dto.LatestNews
import com.beemer.unofficial.fromis_9.model.dto.Member
import com.beemer.unofficial.fromis_9.view.diff.HomeDiffUtil

sealed class HomeItem {
    data class HomeTitle(val title: String) : HomeItem()
    data class HomeDebut(val dday: String, val debut: String) : HomeItem()
    data class HomeMember(val memberList: List<Member>) : HomeItem()
    data class HomeAlbum(val albumList: List<AlbumListDto>) : HomeItem()
    data class HomeNews(val newsList: List<LatestNews>) : HomeItem()
}

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemList = mutableListOf<HomeItem>()
    private var onItemClickListener: ((HomeItem, Int) -> Unit)? = null
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int {
        return when (itemList[position]) {
            is HomeItem.HomeTitle -> 0
            is HomeItem.HomeDebut -> 1
            is HomeItem.HomeMember -> 2
            is HomeItem.HomeAlbum -> 3
            is HomeItem.HomeNews -> 4
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> {
                val binding = RowHomeTitleBinding.inflate(inflater, parent, false)
                TitleViewHolder(binding)
            }
            1 -> {
                val binding = RowHomeDebutBinding.inflate(inflater, parent, false)
                DebutViewHolder(binding)
            }
            2 -> {
                val binding = RowHomeMemberListBinding.inflate(inflater, parent, false)
                MemberViewHolder(binding)
            }
            3 -> {
                val binding = RowHomeAlbumListBinding.inflate(inflater, parent, false)
                AlbumViewHolder(binding)
            }
            4 -> {
                val binding = RowHomeNewsListBinding.inflate(inflater, parent, false)
                NewsViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = itemList[position]) {
            is HomeItem.HomeTitle -> (holder as TitleViewHolder).bind(item)
            is HomeItem.HomeDebut -> (holder as DebutViewHolder).bind(item)
            is HomeItem.HomeMember -> (holder as MemberViewHolder).bind(item)
            is HomeItem.HomeAlbum -> (holder as AlbumViewHolder).bind(item)
            is HomeItem.HomeNews -> (holder as NewsViewHolder).bind(item)
        }
    }

    inner class TitleViewHolder(private val binding: RowHomeTitleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeItem.HomeTitle) {
            binding.txtTitle.text = item.title
        }
    }

    inner class DebutViewHolder(private val binding: RowHomeDebutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeItem.HomeDebut) {
            binding.txtDDay.text = item.dday
            binding.txtDebut.text = item.debut
        }
    }

    inner class MemberViewHolder(binding: RowHomeMemberListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val homeMemberListAdapter = HomeMemberListAdapter()

        init {
            binding.recyclerView.apply {
                adapter = homeMemberListAdapter
                itemAnimator = null
                setRecycledViewPool(viewPool)
                setHasFixedSize(true)
            }

            homeMemberListAdapter.setOnItemClickListener { item, _ ->
                val parentPosition = bindingAdapterPosition
                if (parentPosition != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(HomeItem.HomeMember(memberList = listOf(item)), parentPosition)
                }
            }
        }

        fun bind(item: HomeItem.HomeMember) {
            homeMemberListAdapter.setItemList(item.memberList)
        }
    }

    inner class AlbumViewHolder(binding: RowHomeAlbumListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val homeALbumListAdapter = HomeAlbumListAdapter()

        init {
            binding.recyclerView.apply {
                adapter = homeALbumListAdapter
                itemAnimator = null
                setRecycledViewPool(viewPool)
                setHasFixedSize(true)
            }

            homeALbumListAdapter.apply {
                setOnAlbumClickListener { albumItem, _ ->
                    val parentPosition = bindingAdapterPosition
                    if (parentPosition != RecyclerView.NO_POSITION) {
                        onItemClickListener?.invoke(HomeItem.HomeAlbum(albumList = listOf(albumItem.item)), parentPosition)
                    }
                }
                setOnMoreClickListener {
                    val parentPosition = bindingAdapterPosition
                    if (parentPosition != RecyclerView.NO_POSITION) {
                        onItemClickListener?.invoke(HomeItem.HomeAlbum(albumList = emptyList()), parentPosition)
                    }
                }
            }
        }

        fun bind(item: HomeItem.HomeAlbum) {
            homeALbumListAdapter.setItemList(item.albumList.map { HomeAlbumItem.Album(it) })
        }
    }

    inner class NewsViewHolder(binding: RowHomeNewsListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val homeNewsListAdapter = HomeNewsListAdapter()

        init {
            binding.recyclerView.apply {
                adapter = homeNewsListAdapter
                itemAnimator = null
                setRecycledViewPool(viewPool)
                setHasFixedSize(true)
            }

            homeNewsListAdapter.apply {
                setOnNewsClickListener { albumItem, _ ->
                    val parentPosition = bindingAdapterPosition
                    if (parentPosition != RecyclerView.NO_POSITION) {
                        onItemClickListener?.invoke(HomeItem.HomeNews(newsList = listOf(albumItem.item)), parentPosition)
                    }
                }
                setOnMoreClickListener {
                    val parentPosition = bindingAdapterPosition
                    if (parentPosition != RecyclerView.NO_POSITION) {
                        onItemClickListener?.invoke(HomeItem.HomeNews(newsList = emptyList()), parentPosition)
                    }
                }
            }
        }

        fun bind(item: HomeItem.HomeNews) {
            homeNewsListAdapter.setItemList(item.newsList.map { HomeLatestNewsItem.News(it) })
        }
    }

    fun setOnItemClickListener(listener: (HomeItem, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<HomeItem>) {
        val diffCallback = HomeDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}