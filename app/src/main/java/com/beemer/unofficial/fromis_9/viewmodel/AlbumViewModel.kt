package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beemer.unofficial.fromis_9.model.dto.AlbumListDto
import com.beemer.unofficial.fromis_9.model.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

enum class SortBy {
    RELEASE, TITLE, TYPE
}

@HiltViewModel
class AlbumViewModel @Inject constructor(private val repository: AlbumRepository) : ViewModel() {
    private val _sortBy = MutableLiveData(SortBy.RELEASE)
    val sortBy: LiveData<SortBy> = _sortBy

    private val _isDescending = MutableLiveData(true)
    val isDescending: LiveData<Boolean> = _isDescending

    private val _albumList = MutableLiveData<List<AlbumListDto>>(emptyList())
    val albumList: LiveData<List<AlbumListDto>> = _albumList

    fun setSortBy(sortBy: SortBy) {
        _sortBy.value = sortBy
    }

    fun setDescending(isDescending: Boolean) {
        _isDescending.value = isDescending
    }

    fun getAlbumList() {
        viewModelScope.launch(Dispatchers.IO) {
            val albumList = repository.getAlbumList()
            withContext(Dispatchers.Main) {
                _albumList.value = albumList
            }
        }
    }

    fun sortAlbumList() {
        val sortedList = when (_sortBy.value) {
            SortBy.RELEASE -> _albumList.value?.sortedBy { it.release }?.toList()
            SortBy.TITLE -> _albumList.value?.sortedBy { it.albumName.lowercase() }?.toList()
            SortBy.TYPE ->_albumList.value?.sortedWith(compareBy<AlbumListDto> { it.type }.thenBy { it.albumName.lowercase() })?.toList()
            else -> _albumList.value
        }

        _albumList.value = if (_isDescending.value == true) sortedList?.reversed() else sortedList
    }
}