package com.beemer.unofficial.fromis_9.model.repository

import com.beemer.unofficial.fromis_9.model.api.Fromis9Api
import com.beemer.unofficial.fromis_9.model.dto.Fromis9Dto
import com.beemer.unofficial.fromis_9.model.dto.MemberProfileDto
import retrofit2.Retrofit
import retrofit2.awaitResponse
import javax.inject.Inject

class Fromis9Repository @Inject constructor(retrofit: Retrofit) {
    private val fromis9Api: Fromis9Api = retrofit.create(Fromis9Api::class.java)

    suspend fun getFromis9(): Fromis9Dto? {
        return fromis9Api.getFromis9().awaitResponse().body()
    }

    suspend fun getMemberProfile(name: String): MemberProfileDto? {
        return fromis9Api.getMemberProfile(name).awaitResponse().body()
    }
}