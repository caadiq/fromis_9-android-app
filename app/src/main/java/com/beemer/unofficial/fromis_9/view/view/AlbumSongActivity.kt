package com.beemer.unofficial.fromis_9.view.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.databinding.ActivityAlbumSongBinding
import com.beemer.unofficial.fromis_9.view.adapter.AlbumSong
import com.beemer.unofficial.fromis_9.view.adapter.AlbumSongAdapter
import com.beemer.unofficial.fromis_9.view.utils.ItemDecoratorDivider
import com.beemer.unofficial.fromis_9.viewmodel.AlbumViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumSongActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAlbumSongBinding.inflate(layoutInflater) }

    private val albumViewModel: AlbumViewModel by viewModels()

    private val albumSongAdapter = AlbumSongAdapter()

    private val songName by lazy { intent.getStringExtra("songName") }
    private val colorMain by lazy { intent.getStringExtra("colorMain") }
    private val colorPrimary by lazy { intent.getStringExtra("colorPrimary") }
    private val colorSecondary by lazy { intent.getStringExtra("colorSecondary") }
    private val titleTrack by lazy { intent.getBooleanExtra("titleTrack", false) }
    private var videoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupRecyclerView()
        setupViewModel()
    }

    private fun setupView() {
        binding.txtSongName.apply {
            text = songName
            if (titleTrack) setTextColor(Color.parseColor("#$colorMain"))
        }

        binding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY)
                binding.fab.hide()
            else
                binding.fab.show()
        }

        binding.fab.apply {
            supportBackgroundTintList = ColorStateList.valueOf(Color.parseColor("#$colorPrimary"))
            supportImageTintList = ColorStateList.valueOf(Color.parseColor("#$colorSecondary"))
            setOnClickListener { videoId?.let { openYoutube(it) } }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = albumSongAdapter
            addItemDecoration(ItemDecoratorDivider(0, 80, 0, 0, 0, 0, null))
        }
    }

    private fun setupViewModel() {
        albumViewModel.apply {
            getAlbumSong(songName ?: "")

            albumSong.observe(this@AlbumSongActivity) { song ->
                albumSongAdapter.addItem(AlbumSong("작사", song.lyricist))
                albumSongAdapter.addItem(AlbumSong("작곡", song.composer))
                song.arranger?.let { albumSongAdapter.addItem(AlbumSong("편곡", it)) }
                albumSongAdapter.addItem(AlbumSong("가사", song.lyrics))

                videoId = song.videoId
            }
        }
    }

    private fun openYoutube(id: String) {
        val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
        val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$id"))
        try {
            startActivity(intentApp)
        } catch (ex: ActivityNotFoundException) {
            startActivity(intentBrowser)
        }
    }
}