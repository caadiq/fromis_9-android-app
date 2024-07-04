package com.beemer.unofficial.fromis_9.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class AlbumDetailsDto(
    val description: String,
    val photoList: List<PhotoListDto>,
    val trackList: List<TrackListDto>
)

@Parcelize
data class PhotoListDto(
    val photo: String,
    val concept: String
) : Parcelable

data class TrackListDto(
    val trackNumber: Int,
    val songName: String,
    val titleTrack: Boolean,
    val length: String
)