package com.rafdev.practicestv.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafdev.practicestv.R
import com.rafdev.practicestv.databinding.FragmentSplahBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSplahBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = SplashFragment()
    }
}