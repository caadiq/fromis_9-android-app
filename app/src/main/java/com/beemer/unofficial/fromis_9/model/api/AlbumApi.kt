package com.beemer.unofficial.fromis_9.model.api

import com.beemer.unofficial.fromis_9.model.dto.AlbumDetailsDto
import com.beemer.unofficial.fromis_9.model.dto.AlbumListDto
import com.beemer.unofficial.fromis_9.model.dto.AlbumSongDto
import com.beemer.unofficial.fromis_9.model.dto.AlbumSongListDto
import com.beemer.unofficial.fromis_9.model.dto.WeverseShopAlbumListDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AlbumApi {
    @GET("/api/fromis9/album/list")
    fun getAlbumList(): Call<List<AlbumListDto>>

    @GET("/api/fromis9/album/details")
    fun getAlbumDetails(@Query("album") album: String): Call<AlbumDetailsDto>

    @GET("/api/fromis9/album/song")
    fun getAlbumSong(@Query("name") name: String): Call<AlbumSongDto>

    @GET("/api/fromis9/album/songs")
    fun getAlbumSongList(): Call<List<AlbumSongListDto>>

    @GET("/api/fromis9/album/weverseshop")
    fun getWeverseShopAlbumList(): Call<List<WeverseShopAlbumListDto>>
}