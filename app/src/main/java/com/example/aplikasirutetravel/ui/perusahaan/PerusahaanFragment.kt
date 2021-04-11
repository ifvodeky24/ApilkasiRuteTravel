package com.example.aplikasirutetravel.ui.perusahaan

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
import com.example.aplikasirutetravel.databinding.FragmentPerusahaanBinding
import com.example.aplikasirutetravel.viewmodel.PerusahaanViewModel
import com.example.aplikasirutetravel.viewmodel.ViewModelFactory
import com.example.aplikasirutetravel.vo.Status

class PerusahaanFragment : Fragment(), PerusahaanCallback {
    private var _binding: FragmentPerusahaanBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPerusahaanBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding?.toolbar?.toolbarTitle?.text = "Daftar Perusahaan"
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

        val perusahaanAdapter = PerusahaanAdapter(this@PerusahaanFragment)
        viewModel.getAllPerusahaan().observe(this, { perusahaan ->
            when (perusahaan.status) {
                Status.LOADING -> binding?.progressBar?.visibility = View.VISIBLE
                Status.SUCCESS -> {
                    binding?.progressBar?.visibility = View.GONE
                    perusahaanAdapter.setPerusahaan(perusahaan.data)
                    perusahaanAdapter.notifyDataSetChanged()
                }
                Status.ERROR -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        })

        with(binding?.rvPerusahaan) {
            this?.layoutManager = LinearLayoutManager(context)
            this?.setHasFixedSize(true)
            this?.adapter = perusahaanAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(id_perusahaan: String) {
        val arg = Bundle()
        arg.putString(
            DetailPerusahaanFragment.ID_PERUSAHAAN,
            id_perusahaan
        )
        findNavController().navigate(R.id.detailPerusahaanFragment, arg)
    }
}