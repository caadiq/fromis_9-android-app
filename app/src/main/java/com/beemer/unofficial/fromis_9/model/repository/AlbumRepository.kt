package com.beemer.unofficial.fromis_9.model.repository

import com.beemer.unofficial.fromis_9.model.api.AlbumApi
import com.beemer.unofficial.fromis_9.model.dto.AlbumDetailsDto
import com.beemer.unofficial.fromis_9.model.dto.AlbumListDto
import retrofit2.Retrofit
import retrofit2.awaitResponse
import javax.inject.Inject

class AlbumRepository @Inject constructor(retrofit: Retrofit) {
    private val albumApi: AlbumApi = retrofit.create(AlbumApi::class.java)

    suspend fun getAlbumList(): List<AlbumListDto> {
        return albumApi.getAlbumList().awaitResponse().body() ?: emptyList()
    }

    suspend fun getAlbumDetails(album: String): AlbumDetailsDto {
        return albumApi.getAlbumDetails(album).awaitResponse().body() ?: AlbumDetailsDto("", emptyList(), emptyList())
    }
}