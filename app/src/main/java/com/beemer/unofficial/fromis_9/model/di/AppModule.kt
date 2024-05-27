package com.beemer.unofficial.fromis_9.model.di

import com.beemer.unofficial.fromis_9.model.repository.AlbumRepository
import com.beemer.unofficial.fromis_9.model.repository.ScheduleRepository
import com.beemer.unofficial.fromis_9.model.repository.VideoRepository
import com.beemer.unofficial.fromis_9.model.service.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = RetrofitService.getRetrofit()

    @Provides
    @Singleton
    fun provideAlbumRepository(retrofit: Retrofit): AlbumRepository = AlbumRepository(retrofit)

    @Provides
    @Singleton
    fun provideVideoRepository(retrofit: Retrofit): VideoRepository = VideoRepository(retrofit)

    @Provides
    @Singleton
    fun provideScheduleRepository(retrofit: Retrofit): ScheduleRepository = ScheduleRepository(retrofit)
}