package com.beemer.unofficial.fromis_9.model.repository

import com.beemer.unofficial.fromis_9.model.api.VideoApi
import com.beemer.unofficial.fromis_9.model.dto.VideoListDto
import retrofit2.Retrofit
import retrofit2.awaitResponse
import javax.inject.Inject

class VideoRepository @Inject constructor(retrofit: Retrofit) {
    private val videoApi: VideoApi = retrofit.create(VideoApi::class.java)

    suspend fun getVideoList(page: Int?, limit: Int?, query: String?, playlist: String?): VideoListDto? {
        return videoApi.getVideoList(page, limit, query, playlist).awaitResponse().body()
    }
}