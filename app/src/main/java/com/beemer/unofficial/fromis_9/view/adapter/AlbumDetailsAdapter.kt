package com.beemer.unofficial.fromis_9.view.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.beemer.unofficial.fromis_9.view.view.AlbumDetailsIntroductionFragment
import com.beemer.unofficial.fromis_9.view.view.AlbumDetailsPhotoFragment
import com.beemer.unofficial.fromis_9.view.view.AlbumDetailsTrackFragment

class AlbumDetailsAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AlbumDetailsIntroductionFragment()
            1 -> AlbumDetailsPhotoFragment()
            2 -> AlbumDetailsTrackFragment()
            else -> AlbumDetailsIntroductionFragment()
        }
    }
}