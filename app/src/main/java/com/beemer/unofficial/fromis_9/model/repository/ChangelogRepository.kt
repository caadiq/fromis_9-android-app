package com.beemer.unofficial.fromis_9.model.repository

import com.beemer.unofficial.fromis_9.model.api.ChangelogApi
import com.beemer.unofficial.fromis_9.model.dto.ChangelogListDto
import com.beemer.unofficial.fromis_9.model.dto.LatestVersionDto
import retrofit2.Retrofit
import retrofit2.awaitResponse
import javax.inject.Inject

class ChangelogRepository @Inject constructor(retrofit: Retrofit) {
    private val changelogApi: ChangelogApi = retrofit.create(ChangelogApi::class.java)

    suspend fun getChangelogList(): List<ChangelogListDto> {
        return changelogApi.getChangelogList().awaitResponse().body() ?: emptyList()
    }

    suspend fun getLatestVersion(): LatestVersionDto? {
        return changelogApi.getLatestChangelog().awaitResponse().body()
    }
}