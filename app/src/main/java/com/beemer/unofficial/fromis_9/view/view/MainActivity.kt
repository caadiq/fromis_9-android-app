package com.beemer.unofficial.fromis_9.view.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.ActivityMainBinding
import com.beemer.unofficial.fromis_9.viewmodel.MainFragment
import com.beemer.unofficial.fromis_9.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        observeViewModel()
    }

    private fun setupView() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            mainViewModel.setCurrentFragment(
                when (item.itemId) {
                    R.id.home -> MainFragment.HOME
                    R.id.video -> MainFragment.VIDEO
                    R.id.schedule -> MainFragment.SCHEDULE
                    else -> return@setOnItemSelectedListener false
                }
            )
            true
        }
    }

    private fun observeViewModel() {
        mainViewModel.currentFragment.observe(this) { fragment ->

        }
    }
}