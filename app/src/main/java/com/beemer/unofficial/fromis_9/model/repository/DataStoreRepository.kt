package com.beemer.unofficial.fromis_9.model.repository

import com.beemer.unofficial.fromis_9.model.data.DataStoreModule
import javax.inject.Inject

class DataStoreRepository @Inject constructor(private val dataStore: DataStoreModule) {
    suspend fun setNotiMemberTime(value: Boolean) {
        dataStore.setNotiMemberTime(value)
    }

    fun getNotiMemberTime() = dataStore.getNotiMemberTime()
}