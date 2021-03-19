package com.example.aplikasirutetravel.di

import android.content.Context
import com.example.aplikasirutetravel.data.TravelRepository
import com.example.aplikasirutetravel.data.source.local.LocalDataSource
import com.example.aplikasirutetravel.data.source.local.room.TravelDatabase
import com.example.aplikasirutetravel.data.source.remote.RemoteDataSource
import com.example.aplikasirutetravel.data.source.remote.service.ApiConfig
import com.example.aplikasirutetravel.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): TravelRepository {
        val database = TravelDatabase.getInstance(context)

        val remoteRepository = RemoteDataSource.getInstance(ApiConfig())
        val localDataSource = LocalDataSource.getInstance(database.travelDao())
        val appExecutors = AppExecutors()

        return TravelRepository.getInstance(remoteRepository, localDataSource, appExecutors)
    }
}