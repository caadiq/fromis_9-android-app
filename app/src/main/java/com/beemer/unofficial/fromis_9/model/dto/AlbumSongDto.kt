package com.beemer.unofficial.fromis_9.model.dto

data class AlbumSongDto(
    val lyricist: String,
    val composer: String,
    val arranger: String?,
    val lyrics: String,
    val videoId: String
)
