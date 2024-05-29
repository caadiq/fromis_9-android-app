package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beemer.unofficial.fromis_9.model.dto.ScheduleListDto
import com.beemer.unofficial.fromis_9.model.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(private val repository: ScheduleRepository) : ViewModel() {
    private val _scheduleList = MutableLiveData<List<ScheduleListDto>>()
    val scheduleList: MutableLiveData<List<ScheduleListDto>> = _scheduleList

    fun getScheduleList(year: Int?, month: Int?) {
        viewModelScope.launch {
            _scheduleList.postValue(repository.getScheduleList(year, month))
        }
    }
}