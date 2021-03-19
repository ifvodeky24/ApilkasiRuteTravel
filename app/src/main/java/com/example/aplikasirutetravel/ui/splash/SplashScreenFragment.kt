package com.example.aplikasirutetravel.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.aplikasirutetravel.R
import com.example.aplikasirutetravel.databinding.FragmentSplashScreenBinding

class SplashScreenFragment : Fragment() {

    private val SPLASH_TIME_OUT: Long = 3000

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAnim = AnimationUtils.loadAnimation(activity, R.anim.top_animation)
        val bottomAnim = AnimationUtils.loadAnimation(activity, R.anim.bottom_animation)

        binding?.imageViewLogo?.setAnimation(topAnim)
        binding?.textViewTitle?.setAnimation(bottomAnim)

        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.homeFragment)
        }, SPLASH_TIME_OUT)
    }
}