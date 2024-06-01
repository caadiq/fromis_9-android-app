package com.beemer.unofficial.fromis_9.model.api

import com.beemer.unofficial.fromis_9.model.dto.Fromis9Dto
import com.beemer.unofficial.fromis_9.model.dto.MemberProfileDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Fromis9Api {
    @GET("/api/fromis9/fromis9")
    fun getFromis9(): Call<Fromis9Dto>

    @GET("/api/fromis9/fromis9/profile/{name}")
    fun getMemberProfile(@Path("name") name: String): Call<MemberProfileDto>
}