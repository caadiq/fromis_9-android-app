package com.beemer.unofficial.fromis_9.view.view

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.databinding.ActivityVideoDetailsBinding
import com.beemer.unofficial.fromis_9.view.utils.DateTimeConverter.timeAgo
import com.beemer.unofficial.fromis_9.view.utils.NumberFormatter.formatViews
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions

class VideoDetailsActivity : AppCompatActivity() {
    private val binding by lazy { ActivityVideoDetailsBinding.inflate(layoutInflater) }

    private lateinit var youTubePlayer: YouTubePlayer

    private val videoId by lazy { intent.getStringExtra("videoId") }
    private val title by lazy { intent.getStringExtra("title") }
    private val views by lazy { intent.getIntExtra("views", -1) }
    private val publishedAt by lazy { intent.getStringExtra("publishedAt") }
    private val description by lazy { intent.getStringExtra("description") }

    private var isFullscreen = false

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isFullscreen) {
                youTubePlayer.toggleFullscreen()
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        setupView()
        setupYoutubePlayer()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideStatusBarAndNavigationBar()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            showStatusBarAndNavigationBar()
        }
    }

    private fun setupView() {
        binding.txtTitle.text = title
        binding.txtViews.text = if (views == -1) "" else formatViews(views)
        binding.txtPublished.text = publishedAt?.let { timeAgo(it) }
        binding.txtDescription.text = description
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun setupYoutubePlayer() {
        binding.youtubePlayer.apply {
            lifecycle.addObserver(binding.youtubePlayer)

            addFullscreenListener(object : FullscreenListener {
                override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                    isFullscreen = true

                    binding.layoutFullScreen.apply {
                        visibility = View.VISIBLE
                        addView(fullscreenView)
                    }

                    if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    }

                    hideStatusBarAndNavigationBar()
                }

                override fun onExitFullscreen() {
                    isFullscreen = false

                    binding.layoutFullScreen.apply {
                        visibility = View.GONE
                        removeAllViews()
                    }

                    if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    }

                    showStatusBarAndNavigationBar()
                }
            })

            val youTubePlayerListener = object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    this@VideoDetailsActivity.youTubePlayer = youTubePlayer
                    videoId?.let {
                        binding.progressIndicator.hide()
                        youTubePlayer.cueVideo(it, 0f)
                    }
                }
            }

            val iFramePlayerOptions = IFramePlayerOptions.Builder()
                .controls(1)
                .fullscreen(1)
                .build()

            enableAutomaticInitialization = false
            initialize(youTubePlayerListener, iFramePlayerOptions)
        }
    }

    @Suppress("DEPRECATION")
    private fun showStatusBarAndNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(true)

            val controller = window.insetsController
            controller?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
    }

    @Suppress("DEPRECATION")
    private fun hideStatusBarAndNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)

            val controller = window.insetsController
            if(controller != null){
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }
}