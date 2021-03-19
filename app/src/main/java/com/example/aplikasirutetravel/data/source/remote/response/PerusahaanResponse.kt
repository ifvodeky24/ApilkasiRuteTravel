package com.example.aplikasirutetravel.data.source.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class PerusahaanResponse(
    val perusahaan: List<Perusahaan>
)

@Parcelize
data class Perusahaan(
    val alamat_perusahaan: String,
    val asal: String,
    val created_at: String,
    val facebook: String,
    val foto: String,
    val grid_rute: String,
    val id_jadwal: String,
    val id_perusahaan: String,
    val id_trayek: String,
    val instagram: String,
    val nama_perusahaan: String,
    val nama_trayek: String,
    val nomor_handphone: String,
    val pimpinan: String,
    val status: String,
    val tujuan: String,
    val updated_at: String,
    val website: String
) : Parcelable