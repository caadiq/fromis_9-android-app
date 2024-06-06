package com.beemer.unofficial.fromis_9.model.dto

data class AlbumSongListDto(
    val songName: String,
    val albumName: String,
    val albumCover: String,
    val colorMain: String,
    val colorPrimary: String,
    val colorSecondary: String,
    val titleTrack: Boolean
)
