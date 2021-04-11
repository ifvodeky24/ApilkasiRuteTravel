package com.example.aplikasirutetravel.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.aplikasirutetravel.data.TravelRepository
import com.example.aplikasirutetravel.data.source.local.entity.TrayekEntity
import com.example.aplikasirutetravel.vo.Resource

class TrayekViewModel(private val perusahaanRepository: TravelRepository) : ViewModel() {

    fun getAllTrayek(): LiveData<Resource<List<TrayekEntity>>> =
        perusahaanRepository.getAllTrayek()
}