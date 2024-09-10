package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beemer.unofficial.fromis_9.model.dto.PageDto
import com.beemer.unofficial.fromis_9.model.dto.ScheduleDto
import com.beemer.unofficial.fromis_9.model.repository.ScheduleRepository
import com.beemer.unofficial.fromis_9.view.adapter.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(private val repository: ScheduleRepository) : ViewModel() {
    private val _scheduleList = MutableLiveData<List<ScheduleDto>>()
    val scheduleList: MutableLiveData<List<ScheduleDto>> = _scheduleList

    private val _categoryList = MutableLiveData<List<String>>()
    val categoryList: MutableLiveData<List<String>> = _categoryList

    private val _categories = MutableLiveData<List<Category>>()
    val categories: MutableLiveData<List<Category>> = _categories

    private val _page = MutableLiveData<PageDto>()
    val page: LiveData<PageDto> = _page

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRefreshed = MutableLiveData<Boolean>()
    val isRefreshed: LiveData<Boolean> = _isRefreshed

    fun getScheduleList(year: Int?, month: Int?, category: List<String>) {
        viewModelScope.launch {
            _scheduleList.postValue(repository.getScheduleList(year, month, category))
        }
    }

    fun getScheduleSearchList(page: Int?, limit: Int?, query: String, refresh: Boolean) {
        viewModelScope.launch {
            setLoading(true)
            _isRefreshed.value = refresh

            val response = repository.getScheduleSearchList(page, limit, query)
            _scheduleList.postValue(
                if (refresh)
                    response?.schedules ?: emptyList()
                else
                    _scheduleList.value?.let { it + (response?.schedules ?: emptyList()) }
            )

            _page.postValue(response?.page)
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

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }
}