package com.beemer.unofficial.fromis_9.view.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.ActivitySettingsBinding
import com.beemer.unofficial.fromis_9.model.utils.DownloadAndInstallApk
import com.beemer.unofficial.fromis_9.viewmodel.ChangelogViewModel
import com.beemer.unofficial.fromis_9.viewmodel.SettingsViewModel
import com.mikepenz.aboutlibraries.LibsBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    private val settingsViewModel: SettingsViewModel by viewModels()
    private val changelogViewModel: ChangelogViewModel by viewModels()

    private lateinit var apk: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupViewModel()
    }

    private fun setupView() {
        binding.btnRemoveCache.setOnClickListener {
            settingsViewModel.clearCache()
        }

        binding.btnUpdate.setOnClickListener {
            val downloadAndInstallApk = DownloadAndInstallApk(this, apk, "fromis_9")
            downloadAndInstallApk.startDownloadingApk()
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

    private fun setupViewModel() {
        settingsViewModel.apply {
            appVersion.observe(this@SettingsActivity) {
                binding.txtVersion.text = it
            }

            cacheSize.observe(this@SettingsActivity) { cache ->
                binding.txtCache.text = cache
                binding.btnRemoveCache.isEnabled = cache != "0MB"
            }
        }

        changelogViewModel.apply {
            getLatestVersion()

            latestVersion.observe(this@SettingsActivity) {
                binding.btnUpdate.isEnabled = it.version != binding.txtVersion.text.toString()
                binding.txtLatestVersion.apply {
                    text = "(${it.version})"
                    visibility = if (it.version != binding.txtVersion.text.toString()) View.VISIBLE else View.GONE
                }
                apk = it.apk
            }
        }
    }
}