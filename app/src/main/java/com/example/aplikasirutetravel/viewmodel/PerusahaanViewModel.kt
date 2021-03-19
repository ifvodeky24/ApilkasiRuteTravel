package com.example.aplikasirutetravel.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.aplikasirutetravel.data.TravelRepository
import com.example.aplikasirutetravel.data.source.local.entity.PerusahaanEntity
import com.example.aplikasirutetravel.vo.Resource

class PerusahaanViewModel(private val perusahaanRepository: TravelRepository) : ViewModel() {

    fun getAllPerusahaan(): LiveData<Resource<List<PerusahaanEntity>>> =
        perusahaanRepository.getAllPerusahaan()
}