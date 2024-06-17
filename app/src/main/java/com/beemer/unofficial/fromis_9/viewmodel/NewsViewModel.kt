package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beemer.unofficial.fromis_9.model.dto.LatestNews
import com.beemer.unofficial.fromis_9.model.dto.WeverseShopAlbumListDto
import com.beemer.unofficial.fromis_9.model.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel() {
    private val _weverseShopAlbumList = MutableLiveData<List<WeverseShopAlbumListDto>>()
    val weverseShopAlbumList: LiveData<List<WeverseShopAlbumListDto>> = _weverseShopAlbumList

    private val _newsList = MutableLiveData<List<LatestNews>>()
    val newsList: LiveData<List<LatestNews>> = _newsList

    fun getWeverseShopAlbumList() {
        viewModelScope.launch {
            _weverseShopAlbumList.value = repository.getWeverseShopAlbumList()
        }
    }

    fun getNewsList() {
        viewModelScope.launch {
            _newsList.value = repository.getNewsList()
        }
    }
}