package com.beemer.unofficial.fromis_9.view.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.ActivitySettingsBinding
import com.beemer.unofficial.fromis_9.viewmodel.SettingsViewModel
import com.mikepenz.aboutlibraries.LibsBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        observeViewModel()
    }

    private fun setupView() {
        binding.btnRemoveCache.setOnClickListener {
            settingsViewModel.clearCache()
        }

        binding.txtLicense.setOnClickListener {
            LibsBuilder()
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .withAboutAppName(getString(R.string.app_name))
                .start(this)
        }

        binding.txtChangelog.setOnClickListener {
            startActivity(Intent(this, ChangelogActivity::class.java))
        }
    }

    private fun observeViewModel() {
        settingsViewModel.apply {
            appVersion.observe(this@SettingsActivity) {
                binding.txtVersion.text = it
            }

            cacheSize.observe(this@SettingsActivity) { cache ->
                binding.txtCache.text = cache
                binding.btnRemoveCache.isEnabled = cache != "0MB"
            }
        }
    }
}