package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beemer.unofficial.fromis_9.model.dto.ScheduleListDto
import com.beemer.unofficial.fromis_9.model.repository.ScheduleRepository
import com.beemer.unofficial.fromis_9.view.adapter.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(private val repository: ScheduleRepository) : ViewModel() {
    private val _scheduleList = MutableLiveData<List<ScheduleListDto>>()
    val scheduleList: MutableLiveData<List<ScheduleListDto>> = _scheduleList

    private val _categoryList = MutableLiveData<List<String>>()
    val categoryList: MutableLiveData<List<String>> = _categoryList

    private val _categories = MutableLiveData<List<Category>>()
    val categories: MutableLiveData<List<Category>> = _categories

    fun getScheduleList(year: Int?, month: Int?, category: List<String>) {
        viewModelScope.launch {
            _scheduleList.postValue(repository.getScheduleList(year, month, category))
        }
    }

    fun getCategoryList() {
        viewModelScope.launch {
            _categoryList.postValue(repository.getCategoryList())
        }
    }

    fun setSelectedCategory(selectedCategory: List<Category>) {
        _categories.postValue(selectedCategory)
    }
}