package com.example.aplikasirutetravel.ui.angkutan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aplikasirutetravel.R
import com.example.aplikasirutetravel.ui.kondisi_jalan.DetailKondisiJalanFragment
import timber.log.Timber

class DetailAngkutanFragment : Fragment() {

    private var id_angkutan: String? = null

    companion object {
        const val ID_ANGKUTAN = "id_angkutan"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_angkutan, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            id_angkutan = it.getString(DetailAngkutanFragment.ID_ANGKUTAN)
            Timber.d("cek kondisi jalan $id_angkutan")
        }
    }

}