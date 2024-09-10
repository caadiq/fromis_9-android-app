package com.beemer.unofficial.fromis_9.model.dto

data class ScheduleDto(
    val scheduleId: Int,
    val platform: String,
    val image: String,
    val color: String,
    val dateTime: String,
    val schedule: String,
    val description: String?,
    val url: String?,
    val allDay: Boolean
)
