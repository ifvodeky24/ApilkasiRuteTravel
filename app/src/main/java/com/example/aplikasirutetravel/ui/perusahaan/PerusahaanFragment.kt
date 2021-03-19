package com.example.aplikasirutetravel.ui.perusahaan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasirutetravel.databinding.FragmentPerusahaanBinding
import com.example.aplikasirutetravel.viewmodel.PerusahaanViewModel
import com.example.aplikasirutetravel.viewmodel.ViewModelFactory
import com.example.aplikasirutetravel.vo.Status

class PerusahaanFragment : Fragment() {
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

        val factory = ViewModelFactory.getInstance(requireActivity())

        val viewModel = ViewModelProvider(this, factory)[PerusahaanViewModel::class.java]

        val perusahaanAdapter = PerusahaanAdapter()
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
}