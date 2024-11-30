package com.beemer.unofficial.fromis_9.model.api

import com.beemer.unofficial.fromis_9.model.dto.ScheduleDto
import com.beemer.unofficial.fromis_9.model.dto.ScheduleListDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ScheduleApi {
    @POST("/api/fromis9/schedules")
    fun getScheduleList(
        @Query("year") year: Int?,
        @Query("month") month: Int?,
        @Body category: List<String>
    ): Call<List<ScheduleDto>>

    @POST("/api/fromis9/schedules/search")
    fun getScheduleSearchList(
        @Query("page") page: Int?,
        @Query("limit") limit: Int?,
        @Query("query") query: String
    ): Call<ScheduleListDto>

    @GET("/api/fromis9/categories")
    fun getCategoryList(): Call<List<String>>
}