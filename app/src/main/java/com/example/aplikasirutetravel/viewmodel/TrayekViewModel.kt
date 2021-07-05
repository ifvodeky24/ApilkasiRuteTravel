package com.example.aplikasirutetravel.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.aplikasirutetravel.data.TravelRepository
import com.example.aplikasirutetravel.data.source.local.entity.TrayekEntity
import com.example.aplikasirutetravel.data.source.remote.response.Trayek
import com.example.aplikasirutetravel.data.source.remote.response.Tujuan
import com.example.aplikasirutetravel.vo.Resource

class TrayekViewModel(private val trayekRepository: TravelRepository) : ViewModel() {

    fun getAllTrayek(): LiveData<Resource<List<TrayekEntity>>> =
        trayekRepository.getAllTrayek()

    fun getAllTujuan(): LiveData<Resource<List<Tujuan>>> =
        trayekRepository.getAllTujuan()

    fun getAllTrayekByTujuan(tujuan: String): LiveData<Resource<List<Trayek>>> =
        trayekRepository.getAllTrayekByTujuan(tujuan)
}