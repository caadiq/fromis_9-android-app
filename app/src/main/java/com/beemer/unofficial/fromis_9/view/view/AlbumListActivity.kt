package com.beemer.unofficial.fromis_9.view.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.databinding.ActivityAlbumListBinding
import com.beemer.unofficial.fromis_9.view.adapter.AlbumListAdapter
import com.beemer.unofficial.fromis_9.viewmodel.AlbumViewModel
import com.beemer.unofficial.fromis_9.viewmodel.SortBy
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumListActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAlbumListBinding.inflate(layoutInflater) }

    private val albumViewModel: AlbumViewModel by viewModels()

    private val albumListAdapter = AlbumListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupRecyclerView()
        setupViewModel()
    }

    private fun setupView() {
        binding.btnToggleGroup.apply {
            check(binding.btnRelease.id)
            addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) {
                    val sortBy = when (checkedId) {
                        binding.btnRelease.id -> SortBy.RELEASE
                        binding.btnTitle.id -> SortBy.TITLE
                        binding.btnType.id -> SortBy.TYPE
                        else -> SortBy.RELEASE
                    }
                    albumViewModel.setSortBy(sortBy)
                }
            }
        }

        binding.btnDesc.setOnCheckedChangeListener { _, isChecked ->
            albumViewModel.setDescending(!isChecked)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = albumListAdapter
            setHasFixedSize(true)
        }

        albumListAdapter.setOnItemClickListener { item, _ ->
            val intent = Intent(this, AlbumDetailsActivity::class.java)
            intent.putExtra("albumName", item.albumName)
            intent.putExtra("cover", item.cover)
            intent.putExtra("colorMain", item.colorMain)
            startActivity(intent)
        }
    }

    private fun setupViewModel() {
        albumViewModel.apply {
            getAlbumList()

            sortBy.observe(this@AlbumListActivity) {
                when (it) {
                    SortBy.RELEASE -> binding.btnToggleGroup.check(binding.btnRelease.id)
                    SortBy.TITLE -> binding.btnToggleGroup.check(binding.btnTitle.id)
                    SortBy.TYPE -> binding.btnToggleGroup.check(binding.btnType.id)
                    else -> binding.btnToggleGroup.check(binding.btnRelease.id)
                }
                sortAlbumList(albumList.value ?: emptyList())
            }

            isDescending.observe(this@AlbumListActivity) { descending ->
                binding.btnDesc.isChecked = !descending
                sortAlbumList(albumList.value ?: emptyList())
            }

            albumList.observe(this@AlbumListActivity) { list ->
                albumListAdapter.setItemList(list)
            }
        }
    }
}