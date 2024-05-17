package com.beemer.unofficial.fromis_9.view.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.FragmentAlbumDetailsBinding
import com.beemer.unofficial.fromis_9.view.adapter.AlbumDetailsAdapter
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator

class AlbumDetailsFragment : Fragment() {
    private var _binding: FragmentAlbumDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: AlbumDetailsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAlbumDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupTabLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        Glide.with(this)
            .load(args.Album.cover)
            .into(binding.imgCover)
    }

    private fun setupTabLayout() {
        val colorMainParsed = Color.parseColor("#${args.Album.colorMain}")

        val tabTitles = listOf("앨범 소개", "컨셉 사진", "수록곡")
        val tabIcons = listOf(R.drawable.icon_introduction, R.drawable.icon_photo, R.drawable.icon_track)

        binding.viewPager.adapter = AlbumDetailsAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
            tab.icon = AppCompatResources.getDrawable(requireContext(), tabIcons[position])
        }.attach()

        val states = arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf(-android.R.attr.state_selected))
        val colors = intArrayOf(colorMainParsed, requireContext().getColor(R.color.gray))
        val colorStateList = ColorStateList(states, colors)

        binding.tabLayout.apply {
            setSelectedTabIndicatorColor(colorMainParsed)
            setTabTextColors(requireContext().getColor(R.color.gray), colorMainParsed)
            tabIconTint = colorStateList
        }
    }
}