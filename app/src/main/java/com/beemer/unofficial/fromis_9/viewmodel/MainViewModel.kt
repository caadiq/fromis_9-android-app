package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class MainFragment {
    HOME,
    VIDEO,
    SCHEDULE
}

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _currentFragment = MutableLiveData(MainFragment.HOME)
    val currentFragment: LiveData<MainFragment> = _currentFragment

    fun setCurrentFragment(fragment: MainFragment) {
        _currentFragment.value = fragment
    }
}