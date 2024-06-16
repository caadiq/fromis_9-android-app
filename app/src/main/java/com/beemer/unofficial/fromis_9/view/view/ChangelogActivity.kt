package com.beemer.unofficial.fromis_9.view.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.databinding.ActivityChangelogBinding
import com.beemer.unofficial.fromis_9.view.adapter.ChangelogListAdapter
import com.beemer.unofficial.fromis_9.viewmodel.ChangelogViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangelogActivity : AppCompatActivity() {
    private val binding by lazy { ActivityChangelogBinding.inflate(layoutInflater) }

    private val changelogViewModel by viewModels<ChangelogViewModel>()

    private val changelogListAdapter = ChangelogListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView()
        setupViewModel()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = changelogListAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupViewModel() {
        changelogViewModel.apply {
            getChangelogList()

            changelogList.observe(this@ChangelogActivity) { list ->
                changelogListAdapter.setItemList(list.reversed())
            }
        }
    }
}