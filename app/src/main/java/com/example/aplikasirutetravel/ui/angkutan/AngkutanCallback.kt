package com.example.aplikasirutetravel.ui.angkutan

import com.example.aplikasirutetravel.data.source.local.entity.TrayekEntity

interface AngkutanCallback {
    fun onItemClick(data: TrayekEntity)
}