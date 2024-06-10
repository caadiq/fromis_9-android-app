package com.beemer.unofficial.fromis_9.model.api

import com.beemer.unofficial.fromis_9.model.dto.WeverseShopAlbumListDto
import retrofit2.Call
import retrofit2.http.GET

interface NewsApi {
    @GET("/api/fromis9/news/weverse/shop")
    fun getWeverseShopAlbumList(): Call<List<WeverseShopAlbumListDto>>
}