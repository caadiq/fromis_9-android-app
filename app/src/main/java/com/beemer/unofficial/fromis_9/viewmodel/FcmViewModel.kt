package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beemer.unofficial.fromis_9.model.dto.FcmNotiDto
import com.beemer.unofficial.fromis_9.model.dto.MessageDto
import com.beemer.unofficial.fromis_9.model.repository.FcmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FcmViewModel @Inject constructor(private val repository: FcmRepository) : ViewModel() {
    private val _memberTime = MutableLiveData<MessageDto>()
    val memberTime: LiveData<MessageDto> = _memberTime

    fun setMemberTime(dto: FcmNotiDto) {
        viewModelScope.launch {
            _memberTime.value = repository.setNotiMemberTime(dto)
        }
    }
}