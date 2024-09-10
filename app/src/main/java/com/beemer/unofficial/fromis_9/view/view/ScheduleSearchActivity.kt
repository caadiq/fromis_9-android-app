package com.beemer.unofficial.fromis_9.view.view

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.databinding.ActivityScheduleSearchBinding
import com.beemer.unofficial.fromis_9.view.adapter.ScheduleSearchListAdapter
import com.beemer.unofficial.fromis_9.view.utils.OpenUrl.openUrl
import com.beemer.unofficial.fromis_9.viewmodel.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleSearchActivity : AppCompatActivity() {
    private val binding by lazy  { ActivityScheduleSearchBinding.inflate(layoutInflater) }

    private val scheduleViewModel: ScheduleViewModel by viewModels()

    private val scheduleSearchListAdapter = ScheduleSearchListAdapter()

    private val imm by lazy { getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager }

    private var searchQuery = ""
    private var isLoading = false
    private var isRefreshed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupRecyclerView()
        setupViewModel()
    }

    private fun setupView() {
        imm.showSoftInput(binding.editSearch, InputMethodManager.SHOW_IMPLICIT)

        binding.editSearch.apply {
            requestFocus()

            setOnEditorActionListener { _, _, _ ->
                imm.hideSoftInputFromWindow(binding.editSearch.windowToken, 0)
                clearFocus()
                searchQuery = text.toString().trim()
                scheduleViewModel.getScheduleSearchList(0, 20, searchQuery, true)
                true
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            if (searchQuery.trim().isNotEmpty())
                scheduleViewModel.getScheduleSearchList(0, 20, searchQuery, true)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = scheduleSearchListAdapter
            itemAnimator = null

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = recyclerView.adapter?.itemCount ?: 0

                    if (totalItemCount > 0 && !isLoading && lastVisibleItemPosition >= totalItemCount - 3) {
                        scheduleViewModel.page.value?.let { page ->
                            page.nextPage?.let {
                                scheduleViewModel.getScheduleSearchList(it, 20, searchQuery, false)
                            }
                        }
                    }
                }
            })
        }

        scheduleSearchListAdapter.setOnItemClickListener { item, _ ->
            item.url?.let { openUrl(this, it) }
        }
    }

    private fun setupViewModel() {
        scheduleViewModel.apply {
            scheduleList.observe(this@ScheduleSearchActivity) { list ->
                binding.swipeRefreshLayout.isRefreshing = false
                setLoading(false)

                scheduleSearchListAdapter.setItemList(list)
                if (this@ScheduleSearchActivity.isRefreshed)
                    binding.recyclerView.scrollToPosition(0)
            }

            isLoading.observe(this@ScheduleSearchActivity) { isLoading ->
                this@ScheduleSearchActivity.isLoading = isLoading
                if (isLoading)
                    scheduleSearchListAdapter.showProgress()
                else
                    scheduleSearchListAdapter.hideProgress()
            }

            isRefreshed.observe(this@ScheduleSearchActivity) { isRefreshed ->
                this@ScheduleSearchActivity.isRefreshed = isRefreshed
            }
        }
    }
}