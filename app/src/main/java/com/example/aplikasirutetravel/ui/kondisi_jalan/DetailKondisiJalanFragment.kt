package com.example.aplikasirutetravel.ui.kondisi_jalan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.aplikasirutetravel.R
import com.example.aplikasirutetravel.data.source.remote.service.ApiConfig
import com.example.aplikasirutetravel.databinding.FragmentDetailKondisiJalanNewBinding
import com.example.aplikasirutetravel.viewmodel.KondisiJalanViewModel
import com.example.aplikasirutetravel.viewmodel.ViewModelFactory
import com.example.aplikasirutetravel.vo.Status
import timber.log.Timber

class DetailKondisiJalanFragment : Fragment() {

    private var id_kondisi_jalan: String? = null

    companion object {
        const val ID_KONDISI_JALAN = "id_kondisi_jalan"
    }

    private var _binding: FragmentDetailKondisiJalanNewBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailKondisiJalanNewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding?.toolbarDetailKondisiJalan?.toolbarTitle?.text = "Detail Kondisi Jalan"
        binding?.toolbarDetailKondisiJalan?.toolbarBack?.let {
            Glide.with(this)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .load(R.drawable.ic_back_toolbar)
                .into(it)
        }

        binding?.toolbarDetailKondisiJalan?.toolbarBack?.setOnClickListener {
            findNavController().popBackStack()
        }

        val factory = ViewModelFactory.getInstance(requireActivity())

        val viewModel = ViewModelProvider(this, factory)[KondisiJalanViewModel::class.java]

        arguments?.let {
            id_kondisi_jalan = it.getString(ID_KONDISI_JALAN)
            Timber.d("cek kondisi jalan $id_kondisi_jalan")
        }

        id_kondisi_jalan?.let {
            viewModel.getKondisiJalanById(it).observe(this, { kondisiJalan ->

                when (kondisiJalan.status) {
                    Status.SUCCESS -> {

                        activity?.let { activity ->
                            binding?.ivKondisiJalan?.let { ivPerusahaan ->
                                Glide.with(activity)
                                    .load(ApiConfig.kondisi_jalan + kondisiJalan.data?.foto)
                                    .into(ivPerusahaan)
                            }

                            binding?.tvNamaLokasi?.text = kondisiJalan.data?.nama_lokasi
                            binding?.tvDeskripsi?.text = kondisiJalan.data?.deskripsi
                            binding?.tvTanggal?.text = kondisiJalan.data?.tanggal
                            binding?.tvUpdated?.text = kondisiJalan.data?.updated_at
                        }

                    }

                    Status.ERROR -> {
                        Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }
}
