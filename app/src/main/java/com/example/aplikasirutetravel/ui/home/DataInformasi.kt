package com.example.aplikasirutetravel.ui.home

import com.example.aplikasirutetravel.R

object DataInformasi {
    private val id = intArrayOf(
        1,
        2,
        3
    )

    private val informasiImage = intArrayOf(
        R.drawable.informasi1,
        R.drawable.informasi2,
        R.drawable.infromasi3
    )

    val listData: ArrayList<Informasi>
        get() {
            val list = arrayListOf<Informasi>()
            for (position in informasiImage.indices) {
                val informasi = Informasi()
                informasi.id = informasiImage[position]
                informasi.photo = informasiImage[position]
                list.add(informasi)
            }
            return list
        }
}