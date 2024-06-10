package com.beemer.unofficial.fromis_9.model.dto

data class WeverseShopAlbumListDto(
    val itemId: Int,
    val title: String,
    val img: String,
    val url: String,
    val price: Int,
    val isSoldOut: Boolean
)