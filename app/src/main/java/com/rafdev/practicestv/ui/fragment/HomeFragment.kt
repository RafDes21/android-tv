package com.rafdev.practicestv.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.rafdev.practicestv.databinding.FragmentHomeBinding
import com.rafdev.practicestv.ui.player.PlayerActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        handleButton()
    }

    private fun handleButton() {
        val btn = binding.goToPlayer
        btn.setOnClickListener {
//            Toast.makeText(requireContext(), "clickeaste", Toast.LENGTH_LONG).show()
            val intent = Intent(requireContext(), PlayerActivity::class.java)
            startActivity(intent)
        }
    }
}