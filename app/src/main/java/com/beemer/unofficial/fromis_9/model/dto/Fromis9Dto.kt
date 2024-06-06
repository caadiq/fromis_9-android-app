package com.beemer.unofficial.fromis_9.model.dto

data class Fromis9Dto(
    val bannerImage: String,
    val debut: String,
    val members: List<Member>,
    val socials: List<Social>,
    val albums: List<AlbumListDto>
)

data class Member(
    val name: String,
    val profileImage: String
)

data class Social(
    val sns: String,
    val url: String,
)