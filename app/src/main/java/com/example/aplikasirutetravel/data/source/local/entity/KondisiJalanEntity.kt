package com.example.aplikasirutetravel.data.source.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "kondisijalanentities")
data class KondisiJalanEntity(
    @PrimaryKey
    @ColumnInfo(name = "id_kondisi_jalan")
    val id_kondisi_jalan: String,

    @ColumnInfo(name = "created_at")
    val created_at: String,

    @ColumnInfo(name = "deskripsi")
    val deskripsi: String,

    @ColumnInfo(name = "foto")
    val foto: String,

    @ColumnInfo(name = "latitude")
    val latitude: String,

    @ColumnInfo(name = "longitude")
    val longitude: String,

    @ColumnInfo(name = "nama_lokasi")
    val nama_lokasi: String,

    @ColumnInfo(name = "tanggal")
    val tanggal: String,

    @ColumnInfo(name = "updated_at")
    val updated_at: String
) : Parcelable