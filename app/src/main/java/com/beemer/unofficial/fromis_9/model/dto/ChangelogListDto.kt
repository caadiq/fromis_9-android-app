package com.beemer.unofficial.fromis_9.model.dto

data class ChangelogListDto(
    val version: String,
    val date: String,
    val changelog: List<Changelog>
)

data class Changelog(
    val featureId: Int,
    val icon: String,
    val type: String,
    val feature: String
)

