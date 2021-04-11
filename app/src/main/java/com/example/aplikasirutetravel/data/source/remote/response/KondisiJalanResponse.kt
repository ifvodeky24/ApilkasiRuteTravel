package com.example.aplikasirutetravel.data.source.remote.response

import android.os.Parcelable
import com.example.aplikasirutetravel.data.source.local.entity.KondisiJalanEntity
import kotlinx.parcelize.Parcelize

data class KondisiJalanResponse(
    val kondisi_jalan: List<KondisiJalan>
)

data class KondisiJalanSearchResponse(
    val kondisi_jalan: List<KondisiJalanEntity>
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