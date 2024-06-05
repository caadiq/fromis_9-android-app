package com.beemer.unofficial.fromis_9.view.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.databinding.ActivityWeverseShopBinding
import com.beemer.unofficial.fromis_9.view.adapter.WeverseShopAlbumListAdapter
import com.beemer.unofficial.fromis_9.viewmodel.AlbumViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeverseShopActivity : AppCompatActivity() {
    private val binding by lazy { ActivityWeverseShopBinding.inflate(layoutInflater) }

    private val albumViewModel: AlbumViewModel by viewModels()

    private val weverseShopAlbumListAdapter = WeverseShopAlbumListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView()
        setupViewModel()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = weverseShopAlbumListAdapter
    }

    private fun setupViewModel() {
        albumViewModel.apply {
            getWeverseShopAlbumList()

            weverseShopAlbumList.observe(this@WeverseShopActivity) { list ->
                weverseShopAlbumListAdapter.setItemList(list)
            }
        }
    }
}