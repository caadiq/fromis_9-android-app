package com.beemer.unofficial.fromis_9.model.api

import com.beemer.unofficial.fromis_9.model.dto.AlbumListDto
import retrofit2.Call
import retrofit2.http.GET

interface AlbumApi {
    @GET("/api/fromis9/album/list")
    fun getAlbumList(): Call<List<AlbumListDto>>
}