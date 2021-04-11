package com.example.aplikasirutetravel.data.source.remote.service

import com.example.aplikasirutetravel.data.source.remote.response.KondisiJalanResponse
import com.example.aplikasirutetravel.data.source.remote.response.KondisiJalanSearchResponse
import com.example.aplikasirutetravel.data.source.remote.response.PerusahaanResponse
import com.example.aplikasirutetravel.data.source.remote.response.TrayekResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("perusahaan/get-all")
    fun getAllPerusahaan(
    ): Call<PerusahaanResponse>

    @GET("perusahaan/by-id")
    fun getPerusahaanById(
        @Query("id_perusahaan") id_perusahaan: String?,
    ): Call<PerusahaanResponse>

    @GET("kondisi-jalan/get-all")
    fun getAllKondisiJalan(
    ): Call<KondisiJalanResponse>

    @GET("kondisi-jalan/by-id")
    fun getKondisiJalanById(
        @Query("id_kondisi_jalan") id_kondisi_jalan: String?,
    ): Call<KondisiJalanResponse>

    @GET("kondisi-jalan/search")
    fun getKondisiJalanSearch(
        @Query("query") query: String?,
    ): Call<KondisiJalanSearchResponse>

    @GET("trayek/get-all")
    fun getAllTrayek(
    ): Call<TrayekResponse>
}