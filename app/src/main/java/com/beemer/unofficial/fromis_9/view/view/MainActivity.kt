package com.beemer.unofficial.fromis_9.view.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.ActivityMainBinding
import com.beemer.unofficial.fromis_9.viewmodel.MainFragmentType
import com.beemer.unofficial.fromis_9.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val mainViewModel: MainViewModel by viewModels()

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

    companion object {
        private const val POST_NOTIFICATIONS_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkPermission()

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        if (savedInstanceState == null) {
            setupFragment()
        }
        setupBottomNavigation()
        setupViewModel()
    }

    private fun checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), POST_NOTIFICATIONS_CODE)
        }
    }

    private fun setupFragment() {
        supportFragmentManager.beginTransaction().apply {
            add(binding.containerView.id, HomeFragment(), MainFragmentType.HOME.tag)
            add(binding.containerView.id, VideoFragment(), MainFragmentType.VIDEO.tag)
            add(binding.containerView.id, ScheduleFragment(), MainFragmentType.SCHEDULE.tag)
            commit()
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> mainViewModel.setCurrentFragment(0)
                R.id.video -> mainViewModel.setCurrentFragment(1)
                R.id.schedule -> mainViewModel.setCurrentFragment(2)
            }
            true
        }
    }

    private fun setupViewModel() {
        mainViewModel.currentFragmentType.observe(this) { fragmentType ->
            val currentFragment = supportFragmentManager.findFragmentByTag(fragmentType.tag)
            supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                supportFragmentManager.fragments.forEach { fragment ->
                    if (fragment == currentFragment)
                        show(fragment)
                    else
                        hide(fragment)
                }
            }.commit()

            binding.bottomNavigation.selectedItemId = when (fragmentType) {
                MainFragmentType.HOME -> R.id.home
                MainFragmentType.VIDEO -> R.id.video
                MainFragmentType.SCHEDULE -> R.id.schedule
                else -> R.id.home
            }
        }
    }
}