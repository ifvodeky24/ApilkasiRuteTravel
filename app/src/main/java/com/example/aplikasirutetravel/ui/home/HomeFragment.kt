package com.example.aplikasirutetravel.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasirutetravel.R
import com.example.aplikasirutetravel.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private val list = ArrayList<Informasi>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvInformasi?.setHasFixedSize(true)

        list.addAll(DataInformasi.listData)
        showRecyclerList()

        binding?.linearLayoutPerusahaan?.setOnClickListener {
            findNavController().navigate(R.id.perusahaanFragment)
        }

        binding?.linearLayoutKondisiJalan?.setOnClickListener {
            findNavController().navigate(R.id.kondisiJalanFragment)
        }
    }

    private fun showRecyclerList() {
        binding?.rvInformasi?.layoutManager =
            LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        val listHeroAdapter = InformasiAdapter(list)
        binding?.rvInformasi?.adapter = listHeroAdapter
    }

}