package com.beemer.unofficial.fromis_9.view.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.ActivityAlbumDetailsBinding
import com.beemer.unofficial.fromis_9.view.adapter.AlbumDetailsAdapter
import com.beemer.unofficial.fromis_9.viewmodel.AlbumViewModel
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumDetailsActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAlbumDetailsBinding.inflate(layoutInflater) }

    private val albumViewModel: AlbumViewModel by viewModels()

    private val albumName by lazy { intent.getStringExtra("albumName") }
    private val cover by lazy { intent.getStringExtra("cover") }
    private val colorMain by lazy { intent.getStringExtra("colorMain") }
    private val colorPrimary by lazy { intent.getStringExtra("colorPrimary") }
    private val colorSecondary by lazy { intent.getStringExtra("colorSecondary") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupTabLayout()
        setupViewModel()
    }

    private fun setupView() {
        Glide.with(this)
            .load(cover)
            .into(binding.imgCover)
    }

    private fun setupTabLayout() {
        val colorMainParsed = Color.parseColor("#${colorMain}")

        val tabTitles = listOf("앨범 소개", "컨셉 사진", "수록곡")
        val tabIcons = listOf(R.drawable.icon_introduction, R.drawable.icon_photo, R.drawable.icon_track)

        binding.viewPager.adapter = AlbumDetailsAdapter(this, colorMain, colorPrimary, colorSecondary)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
            tab.icon = AppCompatResources.getDrawable(this, tabIcons[position])
        }.attach()

        val states = arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf(-android.R.attr.state_selected))
        val colors = intArrayOf(colorMainParsed, getColor(R.color.gray))
        val colorStateList = ColorStateList(states, colors)

        binding.tabLayout.apply {
            setSelectedTabIndicatorColor(colorMainParsed)
            setTabTextColors(getColor(R.color.gray), colorMainParsed)
            tabIconTint = colorStateList
        }
    }

    private fun setupViewModel() {
        albumName?.let { albumViewModel.getAlbumDetails(it) }
    }
}