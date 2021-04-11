package com.example.aplikasirutetravel.ui.perusahaan

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
import com.example.aplikasirutetravel.databinding.FragmentDetailPerusahaanNewBinding
import com.example.aplikasirutetravel.viewmodel.PerusahaanViewModel
import com.example.aplikasirutetravel.viewmodel.ViewModelFactory
import com.example.aplikasirutetravel.vo.Status
import timber.log.Timber

class DetailPerusahaanFragment : Fragment() {

    private var id_perusahaan: String? = null

    companion object {
        const val ID_PERUSAHAAN = "id_perusahaan"
    }

    private var _binding: FragmentDetailPerusahaanNewBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailPerusahaanNewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding?.toolbarDetailPerusahaan?.toolbarTitle?.text = "Detail Perusahaan"
        binding?.toolbarDetailPerusahaan?.toolbarBack?.let {
            Glide.with(this)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .load(R.drawable.ic_back_toolbar)
                .into(it)
        }

        binding?.toolbarDetailPerusahaan?.toolbarBack?.setOnClickListener {
            findNavController().popBackStack()
        }

        val factory = ViewModelFactory.getInstance(requireActivity())

        val viewModel = ViewModelProvider(this, factory)[PerusahaanViewModel::class.java]

        arguments?.let {
            id_perusahaan = it.getString(ID_PERUSAHAAN)
            Timber.d("cek perusahaan $id_perusahaan")
        }

        id_perusahaan?.let {
            viewModel.getPerusahaanById(it).observe(this, { perusahaan ->
                when (perusahaan.status) {
                    Status.LOADING -> {

                    }
                    Status.SUCCESS -> {

                        activity?.let { activity ->
                            binding?.ivPerusahaan?.let { ivPerusahaan ->
                                Glide.with(activity)
                                    .load(ApiConfig.perusahaan_image + perusahaan.data?.foto)
                                    .into(ivPerusahaan)
                            }

                            binding?.tvNamaPerusahaan?.text = perusahaan.data?.nama_perusahaan
                            binding?.tvAlamatPerusahaan?.text = perusahaan.data?.alamat_perusahaan
                            binding?.tvAsal?.text = perusahaan.data?.asal
                            binding?.tvTujuan?.text = perusahaan.data?.tujuan
                            binding?.tvPimpinan?.text = perusahaan.data?.pimpinan
                            binding?.tvNomorHandphone?.text = perusahaan.data?.nomor_handphone
                            binding?.tvFacebook?.text = perusahaan.data?.facebook
                            binding?.tvInstagram?.text = perusahaan.data?.instagram
                            binding?.tvWebsite?.text = perusahaan.data?.website
                        }
                        Timber.d("cek dulu ${perusahaan.data}")
                    }
                    Status.ERROR -> {
                        Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }
}