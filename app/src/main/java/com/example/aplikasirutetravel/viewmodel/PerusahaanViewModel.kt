package com.example.aplikasirutetravel.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.aplikasirutetravel.data.TravelRepository
import com.example.aplikasirutetravel.data.source.local.entity.PerusahaanEntity
import com.example.aplikasirutetravel.data.source.remote.response.Perusahaan
import com.example.aplikasirutetravel.vo.Resource

class PerusahaanViewModel(private val perusahaanRepository: TravelRepository) : ViewModel() {

    fun getAllPerusahaan(): LiveData<Resource<List<Perusahaan>>> =
        perusahaanRepository.getAllPerusahaan()

    fun getPerusahaanSearch(query: String): LiveData<Resource<List<Perusahaan>>> =
        perusahaanRepository.getPerusahaanSearch(query)

    fun getPerusahaanById(id_perusahaan: String): LiveData<Resource<PerusahaanEntity>> =
        perusahaanRepository.getPerusahaanById(id_perusahaan)
}