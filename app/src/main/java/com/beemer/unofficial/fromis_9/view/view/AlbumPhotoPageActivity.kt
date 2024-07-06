package com.beemer.unofficial.fromis_9.view.view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.beemer.unofficial.fromis_9.databinding.ActivityAlbumPhotoBinding
import com.beemer.unofficial.fromis_9.model.dto.PhotoListDto
import com.beemer.unofficial.fromis_9.view.adapter.AlbumPhotoPageAdapter
import kotlin.math.abs

class AlbumPhotoPageActivity : AppCompatActivity(), AlbumPhotoPageAdapter.OnClickListener {
    private val binding by lazy { ActivityAlbumPhotoBinding.inflate(layoutInflater) }

    private lateinit var albumPhotoPageAdapter: AlbumPhotoPageAdapter

    private val albumName by lazy { intent.getStringExtra("albumName")}
    private val photos by lazy { intent.getParcelableArrayListExtra<PhotoListDto>("photos") }
    private val position by lazy { intent.getIntExtra("position", 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupViewPager()
    }

    override fun setOnClick(item: PhotoListDto, imageView: ImageView) {
        val options  = ActivityOptions.makeSceneTransitionAnimation(this, imageView, "transitionImagePhoto").toBundle()
        val intent = Intent(this, AlbumPhotoZoomActivity::class.java)
        intent.putExtra("imageUrl", item.photo)
        startActivity(intent, options)
    }

    private fun setupView() {
        binding.txtTitle.text = albumName
        binding.txtCount.text = "${position + 1} / ${photos?.size}"
    }

    private fun setupViewPager() {
        albumPhotoPageAdapter = AlbumPhotoPageAdapter(this)

        binding.viewPager.apply {
            adapter = albumPhotoPageAdapter
            albumPhotoPageAdapter.setItemList(photos ?: emptyList())
            setCurrentItem(position, false)

            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            val transform = CompositePageTransformer()
            transform.addTransformer(MarginPageTransformer(8))
            transform.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.8f + r * 0.2f
            }
            setPageTransformer(transform)

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.txtCount.text = "${position + 1} / ${photos?.size}"
                }
            })
        }
    }
}