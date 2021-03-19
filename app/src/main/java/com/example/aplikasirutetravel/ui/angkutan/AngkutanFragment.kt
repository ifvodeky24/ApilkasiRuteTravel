package com.example.aplikasirutetravel.ui.angkutan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aplikasirutetravel.databinding.FragmentAngkutanBinding

class AngkutanFragment : Fragment() {

    private var _binding: FragmentAngkutanBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAngkutanBinding.inflate(inflater, container, false)
        return binding?.root
    }
}