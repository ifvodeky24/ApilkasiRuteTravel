package com.example.aplikasirutetravel.data.source.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "trayekentities")
data class TrayekEntity(
    @PrimaryKey
    @ColumnInfo(name = "id_trayek")
    val id_trayek: String,

    @ColumnInfo(name = "nama_trayek")
    val nama_trayek: String,

    @ColumnInfo(name = "asal")
    val asal: String,

    @ColumnInfo(name = "tujuan")
    val tujuan: String,

    @ColumnInfo(name = "id_jadwal")
    val id_jadwal: String,

    @ColumnInfo(name = "latitude_asal")
    val latitude_asal: String,

    @ColumnInfo(name = "longitude_asal")
    val longitude_asal: String,

    @ColumnInfo(name = "latitude_tujuan")
    val latitude_tujuan: String,

    @ColumnInfo(name = "longitude_tujuan")
    val longitude_tujuan: String,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "created_at")
    val created_at: String,

    @ColumnInfo(name = "updated_at")
    val updated_at: String,

    @ColumnInfo(name = "jam")
    val jam: String,

    @ColumnInfo(name = "hari")
    val hari: String,

    @ColumnInfo(name = "nama_perusahaan")
    val nama_perusahaan: String
) : Parcelable