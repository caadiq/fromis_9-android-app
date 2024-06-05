package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beemer.unofficial.fromis_9.model.dto.AlbumDetailsDto
import com.beemer.unofficial.fromis_9.model.dto.AlbumListDto
import com.beemer.unofficial.fromis_9.model.dto.AlbumSongDto
import com.beemer.unofficial.fromis_9.model.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class Type {
    SINGLE, MINI, ALBUM, ALL
}

enum class Sort {
    DATE, TITLE, TYPE
}

@HiltViewModel
class AlbumViewModel @Inject constructor(private val repository: AlbumRepository) : ViewModel() {
    private val _type = MutableLiveData(Type.ALL)
    val type: LiveData<Type> = _type

    private val _sort = MutableLiveData(Sort.DATE)
    val sort: LiveData<Sort> = _sort

    private val _albumList = MutableLiveData<List<AlbumListDto>>()
    val albumList: LiveData<List<AlbumListDto>> = _albumList

    private val _albumDetails = MutableLiveData<AlbumDetailsDto>()
    val albumDetails: LiveData<AlbumDetailsDto> = _albumDetails

    private val _albumSong = MutableLiveData<AlbumSongDto>()
    val albumSong: LiveData<AlbumSongDto> = _albumSong

    fun setType(type: Type) {
        _type.value = type
    }

    fun setSort(sort: Sort) {
        _sort.value = sort
    }

    fun getAlbumList() {
        viewModelScope.launch {
            _albumList.value = repository.getAlbumList()
        }
    }

    fun getAlbumDetails(album: String) {
        viewModelScope.launch {
            _albumDetails.value = repository.getAlbumDetails(album)
        }
    }

    fun getAlbumSong(song: String) {
        viewModelScope.launch {
            _albumSong.value = repository.getAlbumSong(song)
        }
    }
}