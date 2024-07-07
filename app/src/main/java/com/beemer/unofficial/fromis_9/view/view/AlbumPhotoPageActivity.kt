package com.beemer.unofficial.fromis_9.view.view

import android.Manifest
import android.app.ActivityOptions
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.beemer.unofficial.fromis_9.databinding.ActivityAlbumPhotoBinding
import com.beemer.unofficial.fromis_9.model.dto.PhotoListDto
import com.beemer.unofficial.fromis_9.view.adapter.AlbumPhotoPageAdapter
import com.beemer.unofficial.fromis_9.viewmodel.AlbumViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

@AndroidEntryPoint
class AlbumPhotoPageActivity : AppCompatActivity(), AlbumPhotoPageAdapter.OnClickListener {
    private val binding by lazy { ActivityAlbumPhotoBinding.inflate(layoutInflater) }

    private val albumViewModel: AlbumViewModel by viewModels()

    private lateinit var albumPhotoPageAdapter: AlbumPhotoPageAdapter

    private val albumName by lazy { intent.getStringExtra("albumName")}
    private val photos by lazy { intent.getParcelableArrayListExtra<PhotoListDto>("photos") }
    private val position by lazy { intent.getIntExtra("position", 0) }

    private lateinit var photoUrl: String

    companion object {
        private const val WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupViewPager()
        setupViewModel()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            WRITE_EXTERNAL_STORAGE_PERMISSION_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    downloadImage(photoUrl)
                } else {
                    Toast.makeText(this, "권한이 없습니다.", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
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
        binding.imgDownload.setOnClickListener {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                checkStoragePermission()
            } else {
                downloadImage(photoUrl)
            }
        }
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
                    albumViewModel.setPhotoUrl(photos?.get(position)?.photo ?: "")
                }
            })
        }
    }

    private fun setupViewModel() {
        albumViewModel.apply {
            setPhotoUrl(photos?.get(position)?.photo ?: "")

            photoUrl.observe(this@AlbumPhotoPageActivity) { url ->
                this@AlbumPhotoPageActivity.photoUrl = url
            }
        }
    }

    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_EXTERNAL_STORAGE_PERMISSION_CODE)
        } else {
            downloadImage(photoUrl)
        }
    }

    private fun downloadImage(url: String) {
        val fileName = createFileName(url)
        val mimeType = determineMimeType(url)

        try {
            enqueueDownload(url, fileName, mimeType)
            Toast.makeText(this, "다운로드를 시작합니다.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFormat(url: String): String? {
        val urlObject = URL(url)
        val query = urlObject.query ?: return null

        return query.split("&").map { it.split("=") }.associate { it[0] to it[1] }["format"]
    }

    // 현재 날짜 및 시간으로 파일 이름 설정
    private fun createFileName(url: String): String {
        val format = getFormat(url)
        val date = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return "$date.$format"
    }

    // 파일 확장자 설정
    private fun determineMimeType(url: String): String {
        val format = getFormat(url)

        return when (format) {
            "jpg" -> "image/jpg"
            "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            else -> ""
        }
    }

    // 다운로드 매니저로 이미지 다운로드
    private fun enqueueDownload(url: String, fileName: String, mimeType: String) {
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(fileName)
            .setDescription("이미지 다운로드 중...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setMimeType(mimeType)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
            request.setDestinationUri(Uri.fromFile(file))
        } else {
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        }

        downloadManager.enqueue(request)
    }
}