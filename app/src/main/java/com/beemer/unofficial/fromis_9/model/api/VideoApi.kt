package com.beemer.unofficial.fromis_9.model.api

import com.beemer.unofficial.fromis_9.model.dto.VideoListDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface VideoApi {
    @GET("/api/fromis9/video/list")
    fun getVideoList(
        @Query("page") page: Int?,
        @Query("limit") limit: Int?,
        @Query("query") query: String?,
        @Query("playlist") playlist: String?,
    ): Call<List<VideoListDto>>
}