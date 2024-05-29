package com.beemer.unofficial.fromis_9.model.repository

import com.beemer.unofficial.fromis_9.model.api.ScheduleApi
import com.beemer.unofficial.fromis_9.model.dto.ScheduleListDto
import retrofit2.Retrofit
import retrofit2.awaitResponse
import javax.inject.Inject

class ScheduleRepository @Inject constructor(retrofit: Retrofit) {
    private val scheduleApi: ScheduleApi = retrofit.create(ScheduleApi::class.java)

    suspend fun getScheduleList(year: Int?, month: Int?): List<ScheduleListDto> {
        return scheduleApi.getScheduleList(year, month).awaitResponse().body() ?: emptyList()
    }
}