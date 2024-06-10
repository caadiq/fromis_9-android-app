package com.beemer.unofficial.fromis_9.view.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
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
            val intent = Intent(Intent.ACTION_VIEW, item.url.toUri())

            val packageManager = packageManager
            val activities = packageManager.queryIntentActivities(intent, 0)
            val isIntentSafe = activities.isNotEmpty()

            if (isIntentSafe)
                startActivity(intent)
            else
                Toast.makeText(this, "해당 URL을 열 수 있는 앱이 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupViewModel() {
        newsViewModel.apply {
            getWeverseShopAlbumList()

            weverseShopAlbumList.observe(this@WeverseShopActivity) { list ->
                weverseShopAlbumListAdapter.setItemList(list.reversed())
            }
        }
    }
}