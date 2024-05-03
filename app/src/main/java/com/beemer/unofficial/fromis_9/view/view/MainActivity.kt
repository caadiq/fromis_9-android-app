package com.beemer.unofficial.fromis_9.view.view

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.ActivityMainBinding
import com.beemer.unofficial.fromis_9.viewmodel.MainFragment
import com.beemer.unofficial.fromis_9.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private var backPressedTime: Long = 0
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime >= 2000) {
                backPressedTime = System.currentTimeMillis()
                Snackbar.make(binding.layoutParent, getString(R.string.str_main_press_back), 2000).apply {
                    view.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.black))
                    setTextColor(ContextCompat.getColor(this@MainActivity, R.color.white))
                    show()
                }
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        setupNav()
        setupView()
        observeViewModel()
    }

    private fun setupNav() {
        navHostFragment = supportFragmentManager.findFragmentById(binding.containerView.id) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val visibleDestinations = setOf(R.id.fragmentHome, R.id.fragmentVideo, R.id.fragmentSchedule)
            binding.bottomNavigation.visibility = if (destination.id in visibleDestinations) View.VISIBLE else View.GONE
        }
    }

    private fun setupView() {
        binding.bottomNavigation.apply {
            setupWithNavController(navController)

            setOnItemSelectedListener { item ->
                mainViewModel.setCurrentFragment(
                    when (item.itemId) {
                        R.id.home -> MainFragment.HOME
                        R.id.video -> MainFragment.VIDEO
                        R.id.schedule -> MainFragment.SCHEDULE
                        else -> MainFragment.HOME
                    }
                )
                true
            }
        }
    }

    private fun observeViewModel() {
        mainViewModel.currentFragment.observe(this) { fragment ->
            navController.navigate(
                when (fragment) {
                    MainFragment.HOME -> R.id.fragmentHome
                    MainFragment.VIDEO -> R.id.fragmentVideo
                    MainFragment.SCHEDULE -> R.id.fragmentSchedule
                    else -> R.id.fragmentHome
                }
            )
        }
    }
}