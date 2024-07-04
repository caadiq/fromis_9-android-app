package com.beemer.unofficial.fromis_9.view.view

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.databinding.ActivityMemberBinding
import com.beemer.unofficial.fromis_9.view.utils.OpenUrl.openUrl
import com.beemer.unofficial.fromis_9.viewmodel.Fromis9ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemberActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMemberBinding.inflate(layoutInflater) }

    private val fromis9ViewModel: Fromis9ViewModel by viewModels()

    private val name by lazy { intent.getStringExtra("name") }

    private var instagram: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupViewModel()
    }

    private fun setupView() {
        binding.txtName.text = name

        binding.imgInstagram.setOnClickListener {
            instagram?.let { openUrl(this, it) }
        }
    }

    private fun setupViewModel() {
        fromis9ViewModel.apply {
            name?.let { getMemberProfile(it) }

            memberProfile.observe(this@MemberActivity) {
                Glide.with(this@MemberActivity)
                    .load(it.profileImage)
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
                    .into(binding.imgProfile)

                binding.txtBirth.text = it.birth.replace("-", ".")
                binding.txtPosition.text = it.position

                instagram = it.instagram
            }
        }
    }
}