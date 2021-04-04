package com.example.aplikasirutetravel.data.source.local

import androidx.lifecycle.LiveData
import com.example.aplikasirutetravel.data.source.local.entity.KondisiJalanEntity
import com.example.aplikasirutetravel.data.source.local.entity.PerusahaanEntity
import com.example.aplikasirutetravel.data.source.local.room.TravelDao

class LocalDataSource private constructor(private val travelDao: TravelDao) {

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(travelDao: TravelDao): LocalDataSource {
            if (INSTANCE == null) {
                INSTANCE = LocalDataSource(travelDao)
            }
            return INSTANCE as LocalDataSource
        }
    }

    fun getAllPerusahaan(): LiveData<List<PerusahaanEntity>> = travelDao.getAllPerusahaan()

    fun getPerusahaanById(id_perusahaan: String): LiveData<PerusahaanEntity> =
        travelDao.getPerusahaanById(id_perusahaan)

    fun getAllKondisiJalan(): LiveData<List<KondisiJalanEntity>> = travelDao.getAllKondisiJalan()

    fun getAllKondisiJalanSearch(query: String): LiveData<List<KondisiJalanEntity>> = travelDao.getAllKondisiJalanSearch(query)

    fun getKondisiJalanById(id_kondisi_jalan: String): LiveData<KondisiJalanEntity> =
        travelDao.getKondisiJalanById(id_kondisi_jalan)

    fun insertPerusahaan(perusahaan: List<PerusahaanEntity>) =
        travelDao.insertPerusahaan(perusahaan)

    fun insertKondisiJalan(kondisi_jalan: List<KondisiJalanEntity>) =
        travelDao.insertKondisiJalan(kondisi_jalan)

    fun insertPerusahaanById(perusahaan: PerusahaanEntity) =
        travelDao.insertPerusahaanById(perusahaan)

    fun insertKondisiJalanById(kondisi_jalan: KondisiJalanEntity) =
        travelDao.insertKondisiJalanById(kondisi_jalan)
}