package com.rafdev.practicestv.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.multidex.MultiDex
import androidx.tvprovider.media.tv.TvContractCompat
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
        MultiDex.install(this);
        setContentView(R.layout.activity_main)

//        contentResolver.delete(TvContractCompat.Channels.CONTENT_URI, null, null)

        val recommendedChannelId = channelManager.createRecommendedChannelIfNeeded()

        channelManager.updateProgramsForChannel(recommendedChannelId, dataItem.items)
        Log.i(CHANNEL, "main -> $recommendedChannelId")

        Log.i(CHANNEL, "DATA_> ${dataItem.items}")


    }

    fun requestChannelBrowsable(channelId: Long) {
        val intent = Intent(TvContractCompat.ACTION_REQUEST_CHANNEL_BROWSABLE)
        intent.putExtra(TvContractCompat.EXTRA_CHANNEL_ID, channelId)
        try {
            startActivityForResult(intent, 0)
        } catch (e: ActivityNotFoundException) {
            Log.e(CHANNEL, "Error: Activity not found to request channel browsable.")
            e.printStackTrace()
            // Manejar el error según sea necesario
        }
    }

}