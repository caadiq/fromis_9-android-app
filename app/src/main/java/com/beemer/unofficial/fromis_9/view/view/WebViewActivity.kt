package com.beemer.unofficial.fromis_9.view.view

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.databinding.ActivityWebviewBinding
import com.beemer.unofficial.fromis_9.view.utils.IntentHelper.openUri

class WebViewActivity : AppCompatActivity() {
    private val binding by lazy { ActivityWebviewBinding.inflate(layoutInflater) }

    private val url by lazy { intent.getStringExtra("url") }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
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
        url?.let { setupWebView(it) }
    }

    private fun setupView() {
        binding.imgClose.setOnClickListener {
            finish()
        }

        binding.imgPrev.setOnClickListener {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            }
        }

        binding.imgNext.setOnClickListener {
            if (binding.webView.canGoForward()) {
                binding.webView.goForward()
            }
        }

        binding.imgReload.setOnClickListener {
            binding.webView.reload()
        }

        binding.imgMenu.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menuInflater.inflate(R.menu.menu_webview, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.copyUrl -> {
                        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText(binding.webView.title, binding.webView.url)
                        clipboard.setPrimaryClip(clip)
                        true
                    }
                    R.id.openBrowser -> {
                        binding.webView.url?.let { webUrl ->
                            openUri(this, webUrl)
                        }
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(url: String) {
        binding.webView.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            loadUrl(url)

            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    binding.progressIndicator.visibility = View.VISIBLE
                    binding.txtUrl.text = url
                    binding.txtTitle.text = view.title
                }

                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    binding.progressIndicator.visibility = View.GONE
                    binding.txtUrl.text = url
                    binding.txtTitle.text = view.title
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    binding.progressIndicator.setProgressCompat(newProgress, true)
                }
            }
        }
    }
}