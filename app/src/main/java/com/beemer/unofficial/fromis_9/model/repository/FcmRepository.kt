package com.beemer.unofficial.fromis_9.model.repository

import com.beemer.unofficial.fromis_9.model.api.FcmApi
import com.beemer.unofficial.fromis_9.model.dto.FcmNotiDto
import com.beemer.unofficial.fromis_9.model.dto.FcmTokenDto
import com.beemer.unofficial.fromis_9.model.dto.MessageDto
import retrofit2.Retrofit
import retrofit2.awaitResponse
import javax.inject.Inject

class FcmRepository @Inject constructor(retrofit: Retrofit) {
    private val fcmApi: FcmApi = retrofit.create(FcmApi::class.java)

    suspend fun sendFcmToken(dto: FcmTokenDto): MessageDto? {
        return fcmApi.sendFcmToken(dto).awaitResponse().body()
    }

    suspend fun setNotiMemberTime(dto: FcmNotiDto): MessageDto? {
        return fcmApi.setNotiMemberTime(dto).awaitResponse().body()
    }
}