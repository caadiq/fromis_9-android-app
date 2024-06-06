package com.beemer.unofficial.fromis_9.view.view

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils.loadLayoutAnimation
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.ActivityAlbumSongSearchBinding
import com.beemer.unofficial.fromis_9.view.adapter.AlbumSongListAdapter
import com.beemer.unofficial.fromis_9.viewmodel.AlbumViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumSongSearchActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAlbumSongSearchBinding.inflate(layoutInflater) }

    private val albumViewModel : AlbumViewModel by viewModels()

    private val albumSongListAdapter = AlbumSongListAdapter()

    private val imm by lazy { getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager }

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

            addTextChangedListener {
                albumSongListAdapter.filter.filter(it.toString())

                if (albumSongListAdapter.itemCount != 0) {
                    binding.recyclerView.scrollToPosition(0)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = albumSongListAdapter
            layoutAnimation = loadLayoutAnimation(this@AlbumSongSearchActivity, R.anim.layout_animation_slide_from_bottom)
        }

        albumSongListAdapter.setOnItemClickListener { item, _ ->
            imm.hideSoftInputFromWindow(binding.editSearch.windowToken, 0)

            val intent = Intent(this, AlbumSongActivity::class.java)
            intent.putExtra("songName", item.songName)
            intent.putExtra("colorMain", item.colorMain)
            intent.putExtra("colorPrimary", item.colorPrimary)
            intent.putExtra("colorSecondary", item.colorSecondary)
            intent.putExtra("titleTrack", item.titleTrack)
            startActivity(intent)
        }
    }

    private fun setupViewModel() {
        albumViewModel.apply {
            getAlbumSongList()

            albumSongList.observe(this@AlbumSongSearchActivity) { list ->
                albumSongListAdapter.setItemList(list)
                binding.recyclerView.scheduleLayoutAnimation()
            }
        }
    }
}