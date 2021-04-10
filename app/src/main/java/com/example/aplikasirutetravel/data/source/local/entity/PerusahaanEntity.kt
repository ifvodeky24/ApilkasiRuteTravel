package com.example.aplikasirutetravel.data.source.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "perusahaanentities")
data class PerusahaanEntity(
    @PrimaryKey
    @ColumnInfo(name = "id_perusahaan")
    val id_perusahaan: String,

    @ColumnInfo(name = "alamat_perusahaan")
    val alamat_perusahaan: String,

    @ColumnInfo(name = "asal")
    val asal: String,

    @ColumnInfo(name = "created_at")
    val created_at: String,

    @ColumnInfo(name = "facebook")
    val facebook: String,

    @ColumnInfo(name = "foto")
    val foto: String,

    @ColumnInfo(name = "id_jadwal")
    val id_jadwal: String,

    @ColumnInfo(name = "id_trayek")
    val id_trayek: String,

    @ColumnInfo(name = "instagram")
    val instagram: String,

    @ColumnInfo(name = "nama_perusahaan")
    val nama_perusahaan: String,

    @ColumnInfo(name = "nama_trayek")
    val nama_trayek: String,

    @ColumnInfo(name = "nomor_handphone")
    val nomor_handphone: String,

    @ColumnInfo(name = "pimpinan")
    val pimpinan: String,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "tujuan")
    val tujuan: String,

    @ColumnInfo(name = "updated_at")
    val updated_at: String,

    @ColumnInfo(name = "website")
    val website: String
) : Parcelable