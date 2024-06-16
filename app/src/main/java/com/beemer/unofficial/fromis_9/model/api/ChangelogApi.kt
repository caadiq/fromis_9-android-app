package com.beemer.unofficial.fromis_9.model.api

import com.beemer.unofficial.fromis_9.model.dto.ChangelogListDto
import retrofit2.Call
import retrofit2.http.GET

interface ChangelogApi {
    @GET("/api/fromis9/changelog/list")
    fun getChangelogList(): Call<List<ChangelogListDto>>
}