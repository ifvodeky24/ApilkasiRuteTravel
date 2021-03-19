package com.example.aplikasirutetravel.data.source.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class KondisiJalanResponse(
    val kondisi_jalan: List<KondisiJalan>
)

@Parcelize
data class KondisiJalan(
    val created_at: String,
    val deskripsi: String,
    val foto: String,
    val id_kondisi_jalan: String,
    val latitude: String,
    val longitude: String,
    val nama_lokasi: String,
    val tanggal: String,
    val updated_at: String
) : Parcelable