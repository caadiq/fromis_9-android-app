package com.beemer.unofficial.fromis_9.model.api

import com.beemer.unofficial.fromis_9.model.dto.FcmTokenDto
import com.beemer.unofficial.fromis_9.model.dto.MessageDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {
    @POST("/api/fromis9/fcm/token")
    fun sendFcmToken(
        @Body token: FcmTokenDto
    ): Call<MessageDto>
}