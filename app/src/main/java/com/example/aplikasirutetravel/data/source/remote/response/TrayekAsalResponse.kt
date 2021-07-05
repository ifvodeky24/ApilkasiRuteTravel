package com.example.aplikasirutetravel.data.source.remote.response

data class TrayekTujuanResponse(
    val trayek: List<TrayekTujuan>
)

data class TrayekTujuan(
    val id_trayek: String,
    val nama_trayek: String,
    val asal: String,
    val tujuan: String,
    val id_jadwal: String,
    val latitude_asal: String,
    val longitude_asal: String,
    val latitude_tujuan: String,
    val longitude_tujuan: String,
    val status: String,
    val created_at: String,
    val updated_at: String
)

data class TujuanResponse(
    val trayek: List<Tujuan>
)

data class Tujuan(
    val tujuan: String
)