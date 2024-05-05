package com.beemer.unofficial.fromis_9.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(@ApplicationContext private val context: Context) : ViewModel() {
    private val _appVersion = MutableLiveData<String?>()
    val appVersion: LiveData<String?> = _appVersion

    init {
        getAppVersion()
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
}