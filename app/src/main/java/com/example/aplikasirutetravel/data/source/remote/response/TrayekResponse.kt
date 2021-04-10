package com.example.aplikasirutetravel.data.source.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class TrayekResponse(
    val trayek: List<Trayek>
)

@Parcelize
data class Trayek(
    val asal: String,
    val created_at: String,
    val hari: String,
    val id_jadwal: String,
    val id_trayek: String,
    val jam: String,
    val latitude_asal: String,
    val latitude_tujuan: String,
    val longitude_asal: String,
    val longitude_tujuan: String,
    val nama_perusahaan: String,
    val nama_trayek: String,
    val status: String,
    val tujuan: String,
    val updated_at: String
) : Parcelable