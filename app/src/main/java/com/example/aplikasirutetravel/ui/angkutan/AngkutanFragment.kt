package com.example.aplikasirutetravel.ui.angkutan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.aplikasirutetravel.R
import com.example.aplikasirutetravel.data.source.local.entity.TrayekEntity
import com.example.aplikasirutetravel.databinding.FragmentAngkutanBinding
import com.example.aplikasirutetravel.ui.perusahaan.DetailPerusahaanFragment
import com.example.aplikasirutetravel.ui.perusahaan.PerusahaanAdapter
import com.example.aplikasirutetravel.viewmodel.PerusahaanViewModel
import com.example.aplikasirutetravel.viewmodel.TrayekViewModel
import com.example.aplikasirutetravel.viewmodel.ViewModelFactory
import com.example.aplikasirutetravel.vo.Status
import timber.log.Timber

class AngkutanFragment : Fragment(), AngkutanCallback {

    private var _binding: FragmentAngkutanBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAngkutanBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.toolbar?.toolbarTitle?.text = "Daftar Angkutan"
        binding?.toolbar?.toolbarBack?.let {
            Glide.with(this)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .load(R.drawable.ic_back_toolbar)
                .into(it)
        }

        binding?.toolbar?.toolbarBack?.setOnClickListener {
            findNavController().popBackStack()
        }

        val factory = ViewModelFactory.getInstance(requireActivity())

        val viewModel = ViewModelProvider(this, factory)[PerusahaanViewModel::class.java]

        val perusahaanAdapter = AngkutanAdapter(this@AngkutanFragment)
        viewModel.getAllTrayek().observe(this, { angkutan ->
            when (angkutan.status) {

                Status.LOADING -> binding?.progressBar?.visibility = View.VISIBLE
                Status.SUCCESS -> {
                    Timber.d("cekk $angkutan")
                    binding?.progressBar?.visibility = View.GONE
                    perusahaanAdapter.setAngkutan(angkutan.data)
                    perusahaanAdapter.notifyDataSetChanged()
                }
                Status.ERROR -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        })

        with(binding?.rvAngkutan) {
            this?.layoutManager = LinearLayoutManager(context)
            this?.setHasFixedSize(true)
            this?.adapter = perusahaanAdapter
        }
    }

    override fun onItemClick(data: TrayekEntity) {
        val arg = Bundle()
        arg.putParcelable(
            DetailAngkutanFragment.ANGKUTAN,
            data
        )
        findNavController().navigate(R.id.detailAngkutanFragment, arg)
    }
}