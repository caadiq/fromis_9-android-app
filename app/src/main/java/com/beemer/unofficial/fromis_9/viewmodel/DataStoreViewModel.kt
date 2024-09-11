package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.beemer.unofficial.fromis_9.model.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class DataStoreViewModel @Inject constructor(private val repository: DataStoreRepository) : ViewModel() {
    val notiMemberTime = repository.getNotiMemberTime().asLiveData()

    fun setNotiMemberTime(value: Boolean) {
        viewModelScope.launch {
            repository.setNotiMemberTime(value)
        }
    }
}