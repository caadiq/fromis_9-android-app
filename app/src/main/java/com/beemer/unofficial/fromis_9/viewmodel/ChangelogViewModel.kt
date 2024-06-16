package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beemer.unofficial.fromis_9.model.dto.ChangelogListDto
import com.beemer.unofficial.fromis_9.model.repository.ChangelogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangelogViewModel @Inject constructor(private val repository: ChangelogRepository) : ViewModel() {
    private val _changelogList = MutableLiveData<List<ChangelogListDto>>()
    val changelogList: LiveData<List<ChangelogListDto>> = _changelogList

    fun getChangelogList() {
        viewModelScope.launch {
            _changelogList.value = repository.getChangelogList()
        }
    }
}