package com.beemer.unofficial.fromis_9.view.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.beemer.unofficial.fromis_9.databinding.ActivityNewsListBinding
import com.beemer.unofficial.fromis_9.view.adapter.NewsListAdapter
import com.beemer.unofficial.fromis_9.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsListActivity : AppCompatActivity() {
    private val binding by lazy { ActivityNewsListBinding.inflate(layoutInflater) }

    private val newsViewModel by viewModels<NewsViewModel>()

    private val newsListAdapter = NewsListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView()
        setupViewModel()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = newsListAdapter
            setHasFixedSize(true)
        }

        newsListAdapter.setOnItemClickListener { item, _ ->
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
            getNewsList()

            newsList.observe(this@NewsListActivity) { list ->
                newsListAdapter.setItemList(list)
            }
        }
    }
}