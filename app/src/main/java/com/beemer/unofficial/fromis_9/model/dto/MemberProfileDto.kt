package com.beemer.unofficial.fromis_9.model.dto

data class MemberProfileDto(
    val name: String,
    val birth: String,
    val profileImage: String,
    val position: String?,
    val instagram: String?
)