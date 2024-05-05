package com.beemer.unofficial.fromis_9.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(@ApplicationContext private val context: Context) : ViewModel() {
    private val _appVersion = MutableLiveData<String?>()
    val appVersion: LiveData<String?> = _appVersion

    private val _cacheSize = MutableLiveData<String>()
    val cacheSize: LiveData<String> = _cacheSize

    init {
        getAppVersion()
        getCacheSize()
    }

    private fun getAppVersion() {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val versionName = packageInfo.versionName

            _appVersion.postValue(versionName)
        } catch (_: PackageManager.NameNotFoundException) {
            _appVersion.postValue(null)
        }
    }

    private fun getCacheSize() {
        val internalCacheSize = getDirectorySize(context.cacheDir)
        val externalCacheSize = context.externalCacheDir?.let { getDirectorySize(it) } ?: 0
        val totalCacheSize = internalCacheSize + externalCacheSize
        val totalCacheSizeInMB = BigDecimal(totalCacheSize / (1024.0 * 1024.0)).setScale(2, RoundingMode.HALF_UP).toDouble()
        _cacheSize.postValue("${NumberFormat.getNumberInstance().format(totalCacheSizeInMB)}MB")
    }

    private fun getDirectorySize(directory: File): Long {
        var size: Long = 0
        directory.listFiles()?.forEach { file ->
            size += if (file.isDirectory) getDirectorySize(file) else file.length()
        }
        return size
    }

    fun clearCache() {
        context.cacheDir.listFiles()?.forEach { it.deleteRecursively() }
        context.externalCacheDir?.deleteRecursively()
        getCacheSize()
    }
}