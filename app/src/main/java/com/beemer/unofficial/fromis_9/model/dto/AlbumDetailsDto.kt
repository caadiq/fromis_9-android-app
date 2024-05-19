package com.beemer.unofficial.fromis_9.model.dto

data class AlbumDetailsDto(
    val description: String,
    val photoList: List<PhotoListDto>,
    val trackList: List<TrackListDto>
)

data class PhotoListDto(
    val photo: String,
    val concept: String
)

data class TrackListDto(
    val trackNumber: Int,
    val songName: String,
    val titleTrack: Boolean,
    val length: String
)