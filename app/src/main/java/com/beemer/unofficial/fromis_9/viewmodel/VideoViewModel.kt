package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.beemer.unofficial.fromis_9.model.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(private val videoRepository: VideoRepository) : ViewModel() {
    private val _toggleGroup = MutableLiveData<Int>()
    val toggleGroup: LiveData<Int> = _toggleGroup

    fun setToggleGroup(toggleGroup: Int) {
        _toggleGroup.value = toggleGroup
    }
}