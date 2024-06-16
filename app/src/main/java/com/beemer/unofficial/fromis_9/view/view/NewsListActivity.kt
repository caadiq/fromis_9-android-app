package com.beemer.unofficial.fromis_9.view.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.databinding.ActivityNewsListBinding
import com.beemer.unofficial.fromis_9.view.adapter.NewsItem
import com.beemer.unofficial.fromis_9.view.adapter.NewsListAdapter
import com.beemer.unofficial.fromis_9.view.utils.IntentHelper.openUri
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
            openUri(this, item.item.url)
        }
    }

    private fun setupViewModel() {
        newsViewModel.apply {
            getNewsList()

            newsList.observe(this@NewsListActivity) { list ->
                newsListAdapter.setItemList(list.map { NewsItem.News(it) })
            }
        }
    }
}