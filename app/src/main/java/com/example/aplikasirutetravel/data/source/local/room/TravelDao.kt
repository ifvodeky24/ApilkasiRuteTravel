package com.example.aplikasirutetravel.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aplikasirutetravel.data.source.local.entity.KondisiJalanEntity
import com.example.aplikasirutetravel.data.source.local.entity.PerusahaanEntity

@Dao
interface TravelDao {

    @Query("SELECT * FROM perusahaanentities")
    fun getAllPerusahaan(): LiveData<List<PerusahaanEntity>>

    @Query("SELECT * FROM perusahaanentities WHERE id_perusahaan = :id_perusahaan")
    fun getPerusahaanById(id_perusahaan: String): LiveData<PerusahaanEntity>

    @Query("SELECT * FROM kondisijalanentities")
    fun getAllKondisiJalan(): LiveData<List<KondisiJalanEntity>>

    @Query("SELECT * FROM kondisijalanentities WHERE nama_lokasi= :query")
    fun getAllKondisiJalanSearch(query: String): LiveData<List<KondisiJalanEntity>>

    @Query("SELECT * FROM kondisijalanentities WHERE id_kondisi_jalan = :id_kondisi_jalan")
    fun getKondisiJalanById(id_kondisi_jalan: String): LiveData<KondisiJalanEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPerusahaan(module: List<PerusahaanEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKondisiJalan(module: List<KondisiJalanEntity>)
}