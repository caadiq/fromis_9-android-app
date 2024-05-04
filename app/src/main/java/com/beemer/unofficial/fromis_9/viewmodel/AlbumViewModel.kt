package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class SortBy {
    RELEASE, TITLE, TYPE
}

class AlbumViewModel : ViewModel() {
    private val _sortBy = MutableLiveData(SortBy.RELEASE)
    val sortBy: LiveData<SortBy> = _sortBy

    private val _isDescending = MutableLiveData(true)
    val isDescending: LiveData<Boolean> = _isDescending

    fun setSortBy(sortBy: SortBy) {
        _sortBy.value = sortBy
    }

    fun setDescending(isDescending: Boolean) {
        _isDescending.value = isDescending
    }
}