package com.beemer.unofficial.fromis_9.model.repository

import com.beemer.unofficial.fromis_9.model.api.NewsApi
import com.beemer.unofficial.fromis_9.model.dto.WeverseShopAlbumListDto
import retrofit2.Retrofit
import retrofit2.awaitResponse
import javax.inject.Inject

class NewsRepository @Inject constructor(retrofit: Retrofit) {
    private val newsApi: NewsApi = retrofit.create(NewsApi::class.java)

    suspend fun getWeverseShopAlbumList(): List<WeverseShopAlbumListDto> {
        return newsApi.getWeverseShopAlbumList().awaitResponse().body() ?: emptyList()
    }
}