package com.beemer.unofficial.fromis_9.view.view

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.beemer.unofficial.fromis_9.databinding.ActivityFromis9Binding
import com.beemer.unofficial.fromis_9.view.adapter.MemberListAdapter
import com.beemer.unofficial.fromis_9.view.adapter.SnsListAdapter
import com.beemer.unofficial.fromis_9.view.utils.DateTimeConverter.stringToDate
import com.beemer.unofficial.fromis_9.view.utils.ItemDecoratorDivider
import com.beemer.unofficial.fromis_9.view.utils.OpenUrl.openUrl
import com.beemer.unofficial.fromis_9.viewmodel.Fromis9ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.util.Locale

@AndroidEntryPoint
class Fromis9Activity : AppCompatActivity() {
    private val binding by lazy { ActivityFromis9Binding.inflate(layoutInflater) }

    private val fromis9ViewModel: Fromis9ViewModel by viewModels()

    private val memberListAdapter = MemberListAdapter()
    private val snsListAdapter = SnsListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        binding.recyclerMembers.apply {
            adapter = memberListAdapter
            addItemDecoration(ItemDecoratorDivider(this@Fromis9Activity, 0, 0, 0, 12, 0, 0, null))
        }

        binding.recyclerSns.apply {
            adapter = snsListAdapter
            addItemDecoration(ItemDecoratorDivider(this@Fromis9Activity, 0, 16, 0, 0, 0, 0, null))
        }

        snsListAdapter.setOnItemClickListener { item, _ ->
            openUrl(this, item.url)
        }
    }

    private fun setupViewModel() {
        fromis9ViewModel.apply {
            getFromis9()

            fromis9.observe(this@Fromis9Activity) {
                memberListAdapter.setItemList(it.members)
                snsListAdapter.setItemList(it.socials)

                Glide.with(this@Fromis9Activity)
                    .load(it.bannerImage)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                            binding.progressIndicator.hide()
                            binding.scrollView.visibility = View.VISIBLE
                            return false
                        }

                        override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                            binding.progressIndicator.hide()
                            binding.scrollView.visibility = View.VISIBLE
                            return false
                        }
                    })
                    .into(binding.imgBanner)
                binding.txtDetail.text = it.detail
                binding.txtDebut.text = it.debut

                ddayCounter(it.debut)
            }
        }
    }

    private fun ddayCounter(debutDate: String) {
        val debutDateTime = stringToDate(debutDate, "yyyy.MM.dd", Locale.KOREA).atStartOfDay()

        lifecycleScope.launch {
            while (isActive) {
                val now = LocalDateTime.now()
                val diff = Duration.between(debutDateTime, now)

                val days = diff.toDays()
                val hours = diff.toHoursPart()
                val minutes = diff.toMinutesPart()
                val seconds = diff.toSecondsPart()

                binding.txtDdayDays.text = days.toString()
                binding.txtDdayHours.text = String.format(Locale.KOREA, "%02d", hours)
                binding.txtDdayMinutes.text = String.format(Locale.KOREA, "%02d", minutes)
                binding.txtDdaySeconds.text = String.format(Locale.KOREA, "%02d", seconds)

                delay(1000L)
            }
        }
    }
}