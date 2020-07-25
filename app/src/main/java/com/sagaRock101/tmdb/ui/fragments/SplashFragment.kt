package com.sagaRock101.tmdb.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sagaRock101.tmdb.R
import com.sagaRock101.tmdb.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private val handler = Handler()
    lateinit var binding:FragmentSplashBinding
    lateinit var task: Runnable
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         task = Runnable {
            findNavController().navigate(R.id.action_splashFragment_to_movieFragment)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        handler.postDelayed(task, 1000)
        return binding.root
    }

    override fun onPause() {
        handler.removeCallbacks(task)
//        findNavController().popBackStack()
        super.onPause()
    }
}