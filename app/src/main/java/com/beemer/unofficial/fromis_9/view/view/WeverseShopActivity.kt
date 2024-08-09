package com.beemer.unofficial.fromis_9.view.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.databinding.ActivityWeverseShopBinding
import com.beemer.unofficial.fromis_9.view.adapter.WeverseShopAlbumListAdapter
import com.beemer.unofficial.fromis_9.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeverseShopActivity : AppCompatActivity() {
    private val binding by lazy { ActivityWeverseShopBinding.inflate(layoutInflater) }

    private val newsViewModel: NewsViewModel by viewModels()

    private val weverseShopAlbumListAdapter = WeverseShopAlbumListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView()
        setupViewModel()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = weverseShopAlbumListAdapter

        weverseShopAlbumListAdapter.setOnItemClickListener { item, _ ->
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("url", item.url)
            startActivity(intent)
        }
    }

    private fun setupViewModel() {
        newsViewModel.apply {
            getWeverseShopAlbumList()
            binding.shimmerFrameLayout.startShimmer()

            weverseShopAlbumList.observe(this@WeverseShopActivity) { list ->
                weverseShopAlbumListAdapter.setItemList(list.reversed())
                binding.shimmerFrameLayout.apply {
                    stopShimmer()
                    visibility = View.GONE
                }
            }
        }
    }
}