package com.rafdev.practicestv.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.rafdev.practicestv.R
import com.rafdev.practicestv.channel.ChannelManager
import com.rafdev.practicestv.data.ImplementResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    companion object {
        const val CHANNEL = "channelManager"
    }

    private val channelManager = ChannelManager(this)
    private val dataItem = ImplementResponse()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recommendedChannelId = channelManager.createRecommendedChannelIfNeeded()

        channelManager.updateProgramsForChannel(recommendedChannelId, dataItem.items)
        Log.i(CHANNEL, "main -> $recommendedChannelId")

        Log.i(CHANNEL, "DATA_> ${dataItem.items}")


    }
}