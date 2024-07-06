package com.beemer.unofficial.fromis_9.view.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.databinding.ActivityAlbumPhotoZoomBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

class AlbumPhotoZoomActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAlbumPhotoZoomBinding.inflate(layoutInflater) }

    private val imageUrl by lazy { intent.getStringExtra("imageUrl") }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            binding.imgPhoto.resetZoom()
            supportFinishAfterTransition()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        setupView()
    }

    private fun setupView() {
        supportPostponeEnterTransition()
        binding.imgPhoto.transitionWithGlide(imageUrl, ::supportStartPostponedEnterTransition)

    }

    private fun ImageView.transitionWithGlide(url: String?, onLoadingFinished: () -> Unit = {}) {
        val listener = object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                onLoadingFinished()
                return false
            }

            override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                onLoadingFinished()
                return false
            }
        }

        Glide.with(this)
            .load(url)
            .apply(RequestOptions().dontTransform().placeholder(ColorDrawable(Color.TRANSPARENT)))
            .listener(listener)
            .fitCenter()
            .into(this)
    }
}