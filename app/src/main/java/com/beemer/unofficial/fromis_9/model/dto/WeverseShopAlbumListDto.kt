package com.beemer.unofficial.fromis_9.model.dto

import com.google.gson.annotations.SerializedName

data class WeverseShopAlbumListDto(
    val index: Int,
    val title: String,
    @SerializedName("img_src") val imgSrc: String,
    val url: String,
    val price: Int,
    @SerializedName("is_sold_out") val isSoldOut: Boolean
)