package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beemer.unofficial.fromis_9.model.dto.PageDto
import com.beemer.unofficial.fromis_9.model.dto.VideosDto
import com.beemer.unofficial.fromis_9.model.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(private val videoRepository: VideoRepository) : ViewModel() {
    private val _toggleGroup = MutableLiveData<Int>()
    val toggleGroup: LiveData<Int> = _toggleGroup

    private val _page = MutableLiveData<PageDto>()
    val page: LiveData<PageDto> = _page

    private val _videoList = MutableLiveData<List<VideosDto>>()
    val videoList: LiveData<List<VideosDto>> = _videoList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRefreshed = MutableLiveData<Boolean>()
    val isRefreshed: LiveData<Boolean> = _isRefreshed

    fun setToggleGroup(toggleGroup: Int) {
        _toggleGroup.value = toggleGroup
    }

    fun getVideoList(page: Int?, limit: Int?, query: String?, playlist: String?, refresh: Boolean) {
        viewModelScope.launch {
            setLoading(true)
            _isRefreshed.value = refresh

            val response = videoRepository.getVideoList(page, limit, query, playlist)
            _videoList.postValue(
                if (refresh)
                    response?.videos ?: emptyList()
                else
                    _videoList.value?.let { it + (response?.videos ?: emptyList()) }
            )
            _page.postValue(response?.page)
        }
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }
}