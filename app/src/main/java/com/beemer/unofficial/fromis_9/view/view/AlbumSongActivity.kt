package com.beemer.unofficial.fromis_9.view.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.databinding.ActivityAlbumSongBinding
import com.beemer.unofficial.fromis_9.view.adapter.AlbumSongAdapter
import com.beemer.unofficial.fromis_9.view.adapter.AlbumSongItem
import com.beemer.unofficial.fromis_9.viewmodel.AlbumViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumSongActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAlbumSongBinding.inflate(layoutInflater) }

    private val albumViewModel: AlbumViewModel by viewModels()

    private val albumSongAdapter = AlbumSongAdapter()

    private val songName by lazy { intent.getStringExtra("songName") }
    private val colorMain by lazy { intent.getStringExtra("colorMain") }
    private val titleTrack by lazy { intent.getBooleanExtra("titleTrack", false) }
    private var videoId: String? = null
    private var fanchantVideoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupRecyclerView()
        setupViewModel()
    }

    private fun setupView() {
        binding.txtTitle.apply {
            text = songName
            if (titleTrack) setTextColor(Color.parseColor("#$colorMain"))
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = albumSongAdapter

        albumSongAdapter.setOnItemClickListener { item, _ ->
            if (item is AlbumSongItem.Video)
                openYoutube(item.videoId)
        }
    }

    private fun setupViewModel() {
        albumViewModel.apply {
            songName?.let { getAlbumSong(it) }

            albumSong.observe(this@AlbumSongActivity) { song ->
                val items = mutableListOf<AlbumSongItem>()

                items.add(AlbumSongItem.Title("참여"))
                items.add(AlbumSongItem.Credits("작사", song.lyricist))
                items.add(AlbumSongItem.Credits("작곡", song.composer))
                song.arranger?.let { items.add(AlbumSongItem.Credits("편곡", it)) }

                items.add(AlbumSongItem.Title("가사"))
                items.add(AlbumSongItem.Lyrics(song.lyrics))

                song.fanchant?.let {
                    items.add(AlbumSongItem.Title("응원법"))
                    items.add(AlbumSongItem.FanChant(it))
                }

                items.add(AlbumSongItem.Title(if (titleTrack) "뮤직비디오" else "음원"))
                items.add(AlbumSongItem.Video(song.videoId))

                song.fanchantVideoId?.let {
                    items.add(AlbumSongItem.Title("응원법 영상"))
                    items.add(AlbumSongItem.Video(it))
                }

                albumSongAdapter.setItemList(items)

                videoId = song.videoId
                fanchantVideoId = song.fanchantVideoId
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