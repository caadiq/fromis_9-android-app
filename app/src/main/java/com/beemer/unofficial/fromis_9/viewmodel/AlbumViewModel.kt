package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beemer.unofficial.fromis_9.model.dto.AlbumDetailsDto
import com.beemer.unofficial.fromis_9.model.dto.AlbumListDto
import com.beemer.unofficial.fromis_9.model.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    private val _albumList = MutableLiveData<List<AlbumListDto>>()
    val albumList: LiveData<List<AlbumListDto>> = _albumList

    private val _albumDetails = MutableLiveData<AlbumDetailsDto>()
    val albumDetails: LiveData<AlbumDetailsDto> = _albumDetails

    fun setSortBy(sortBy: SortBy) {
        _sortBy.value = sortBy
    }

    fun setDescending(isDescending: Boolean) {
        _isDescending.value = isDescending
    }

    fun getAlbumList() {
        viewModelScope.launch {
            sortAlbumList(repository.getAlbumList())
        }
    }

    fun sortAlbumList(list: List<AlbumListDto>) {
        val sortedList = when (_sortBy.value) {
            SortBy.RELEASE -> list.sortedBy { it.release }.toList()
            SortBy.TITLE -> list.sortedBy { it.albumName.lowercase() }.toList()
            SortBy.TYPE -> list.sortedWith(compareBy<AlbumListDto> { it.type }.thenBy { it.albumName.lowercase() }).toList()
            else -> list
        }

        _albumList.value = if (_isDescending.value == true) sortedList.reversed() else sortedList
    }

    fun getAlbumDetails(album: String) {
        viewModelScope.launch {
            _albumDetails.value = repository.getAlbumDetails(album)
        }
    }
}