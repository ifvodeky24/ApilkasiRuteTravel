package com.example.aplikasirutetravel.data.source.remote.response

data class TrayekAsalResponse(
    val trayek: List<TrayekAsal>
)

data class TrayekAsal(
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
    val updated_at: String,
)

data class AsalResponse(
    val trayek: List<Asal>
)

data class Asal(
    val asal: String
)