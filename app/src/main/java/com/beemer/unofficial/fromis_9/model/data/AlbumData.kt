package com.beemer.unofficial.fromis_9.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlbumData(
    val albumName: String,
    val cover: String,
    val colorMain: String
): Parcelable