package com.rafdev.practicestv

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.tvprovider.media.tv.TvContractCompat
import com.rafdev.practicestv.channel.ChannelManager
import com.rafdev.practicestv.data.ImplementResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RunOnInstallReceiver : BroadcastReceiver() {

    companion object {
        const val TAG = "broadcastchannel"
    }

    override fun onReceive(context: Context, intent: Intent) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "onReceive ${intent.action}")

            when (intent.action) {
                // Initializes channel
                TvContractCompat.ACTION_INITIALIZE_PROGRAMS -> {
                    Log.d(TAG, "Handling INITIALIZE_PROGRAMS broadcast")
                    // Synchronizes all program and channel data
                    val channelManager = ChannelManager(context)
                    val recommendedChannelId = channelManager.createRecommendedChannelIfNeeded()
                    val dataItem = ImplementResponse()
                    channelManager.updateProgramsForChannel(recommendedChannelId, dataItem.items)
                    Log.d(TAG, "Recomendados inicializados")
                }
            }
        }
    }
}