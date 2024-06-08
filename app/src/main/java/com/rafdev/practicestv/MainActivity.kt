package com.rafdev.practicestv

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.rafdev.practicestv.databinding.ActivityMainBinding

class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        binding.playButton.apply {
            requestFocus()
            setOnClickListener {
                Toast.makeText(this@MainActivity, "pruebas", Toast.LENGTH_LONG).show()
            }
        }
    }
}