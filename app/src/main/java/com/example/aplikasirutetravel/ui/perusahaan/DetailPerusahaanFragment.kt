package com.example.aplikasirutetravel.ui.perusahaan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aplikasirutetravel.databinding.FragmentDetailPerusahaanBinding

class DetailPerusahaanFragment : Fragment() {

    private var _binding: FragmentDetailPerusahaanBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailPerusahaanBinding.inflate(inflater, container, false)
        return binding?.root
    }

}