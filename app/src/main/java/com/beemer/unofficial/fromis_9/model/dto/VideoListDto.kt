package com.beemer.unofficial.fromis_9.model.dto

data class VideoListDto(
    val page: PageDto,
    val videos: List<VideosDto>
)

data class PageDto(
    val previousPage: Int?,
    val currentPage: Int,
    val nextPage: Int?
)

data class VideosDto(
    val videoId: String,
    val title: String,
    val thumbnail: String,
    val publishedAt: String,
    val isNew: Boolean,
    val description: String,
    val length: Int?,
    val views: Int?
)