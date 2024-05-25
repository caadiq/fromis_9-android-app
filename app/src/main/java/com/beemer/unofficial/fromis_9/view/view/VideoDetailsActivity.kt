package com.beemer.unofficial.fromis_9.view.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.databinding.ActivityVideoDetailsBinding
import com.beemer.unofficial.fromis_9.view.utils.DateTimeConverter.timeAgo
import com.beemer.unofficial.fromis_9.view.utils.NumberFormatter.formatNumber

class VideoDetailsActivity : AppCompatActivity() {
    private val binding by lazy { ActivityVideoDetailsBinding.inflate(layoutInflater) }

    private val videoId by lazy { intent.getStringExtra("videoId") }
    private val title by lazy { intent.getStringExtra("title") }
    private val views by lazy { intent.getIntExtra("views", -1) }
    private val publishedAt by lazy { intent.getStringExtra("publishedAt") }
    private val description by lazy { intent.getStringExtra("description") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        binding.txtTitle.text = title
        binding.txtViews.text = if (views == -1) "" else formatNumber(views)
        binding.txtPublished.text = publishedAt?.let { timeAgo(it) }
        binding.txtDescription.text = description
    }
}