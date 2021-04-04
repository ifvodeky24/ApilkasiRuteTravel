package com.example.aplikasirutetravel.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.aplikasirutetravel.data.TravelRepository
import com.example.aplikasirutetravel.data.source.local.entity.KondisiJalanEntity
import com.example.aplikasirutetravel.data.source.local.entity.PerusahaanEntity
import com.example.aplikasirutetravel.vo.Resource

class KondisiJalanViewModel(private val perusahaanRepository: TravelRepository) : ViewModel() {

    fun getAllKondisiJalan(): LiveData<Resource<List<KondisiJalanEntity>>> =
        perusahaanRepository.getAllKondisiJalan()

    fun getAllKondisiJalanSearch(query: String): LiveData<Resource<List<KondisiJalanEntity>>> =
        perusahaanRepository.getAllKondisiJalanSearch(query)

    fun getKondisiJalanById(id_kondisi_jalan: String): LiveData<Resource<KondisiJalanEntity>> =
        perusahaanRepository.getKondisiJalanById(id_kondisi_jalan)
}