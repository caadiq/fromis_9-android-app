package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beemer.unofficial.fromis_9.model.dto.Fromis9Dto
import com.beemer.unofficial.fromis_9.model.dto.MemberProfileDto
import com.beemer.unofficial.fromis_9.model.repository.Fromis9Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Fromis9ViewModel @Inject constructor(private val repository: Fromis9Repository) : ViewModel() {
    private val _fromis9 = MutableLiveData<Fromis9Dto>()
    val fromis9: LiveData<Fromis9Dto> = _fromis9

    private val _memberProfile = MutableLiveData<MemberProfileDto>()
    val memberProfile: LiveData<MemberProfileDto> = _memberProfile

    fun getFromis9() {
        viewModelScope.launch {
            _fromis9.value = repository.getFromis9()
        }
    }

    fun getMemberProfile(name: String) {
        viewModelScope.launch {
            _memberProfile.value = repository.getMemberProfile(name)
        }
    }
}