package com.example.aplikasirutetravel.data.source.remote.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        var perusahaan_image =
            "http://arslyn.com/rute-travel/web/files/images/perusahaan_images/"

        var kondisi_jalan =
            "http://arslyn.com/rute-travel/web/files/images/kondisi_jalan_images/"
    }

    private var retrofit: Retrofit? = null

    private fun getApiService(): Retrofit {
        if (retrofit == null) {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl("http://arslyn.com/rute-travel/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit as Retrofit
        }
        return retrofit as Retrofit
    }

    fun client(): ApiService = getApiService().create(ApiService::class.java)
}