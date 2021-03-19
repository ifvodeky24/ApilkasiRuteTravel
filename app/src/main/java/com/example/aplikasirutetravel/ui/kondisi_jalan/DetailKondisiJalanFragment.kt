package com.example.aplikasirutetravel.ui.kondisi_jalan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aplikasirutetravel.R
import timber.log.Timber

class DetailKondisiJalanFragment : Fragment() {

    private var id_kondisi_jalan: String? = null

    companion object {
        const val ID_KONDISI_JALAN = "id_kondisi_jalan"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_kondisi_jalan, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            id_kondisi_jalan = it.getString(ID_KONDISI_JALAN)
            Timber.d("cek kondisi jalan $id_kondisi_jalan")
        }
    }
}