package com.beemer.unofficial.fromis_9.model.dto

data class ScheduleListDto(
    val page: PageDto,
    val schedules: List<ScheduleDto>
)