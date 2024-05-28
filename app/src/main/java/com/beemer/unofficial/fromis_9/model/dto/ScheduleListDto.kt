package com.beemer.unofficial.fromis_9.model.dto

data class ScheduleListDto(
    val scheduleId: Int,
    val platform: String,
    val image: String,
    val dateTime: String,
    val schedule: String,
    val description: String?,
    val url: String?,
    val allDay: Boolean
)
