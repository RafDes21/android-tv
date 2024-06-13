package com.rafdev.practicestv.ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.rafdev.practicestv.R
import com.rafdev.practicestv.channel.ChannelManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val channelManager = ChannelManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        channelManager.createChannel()
    }
}