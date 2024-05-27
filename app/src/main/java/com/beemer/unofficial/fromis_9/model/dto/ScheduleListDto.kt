package com.beemer.unofficial.fromis_9.model.dto

data class ScheduleListDto(
    val image: String,
    val date: String,
    val schedule: String,
    val description: String?,
    val url: String?
)
