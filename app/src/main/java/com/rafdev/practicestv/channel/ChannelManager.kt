package com.rafdev.practicestv.channel

import android.content.ContentUris
import android.content.Context
import android.util.Log
import androidx.tvprovider.media.tv.Channel
import androidx.tvprovider.media.tv.TvContractCompat

class ChannelManager(private val context: Context) {

    companion object {
        const val CHANNEL = "channelManager"
    }

    fun createChannel() {
        val builder = Channel.Builder()
        builder.setType(TvContractCompat.Channels.TYPE_PREVIEW)
            .setDisplayName("Recomendados")
//            .setAppLinkIntentUri(uri)


        var channelUri = context.contentResolver.insert(
            TvContractCompat.Channels.CONTENT_URI, builder.build().toContentValues()
        )

        var channelId = ContentUris.parseId(channelUri!!)

        Log.e(CHANNEL, "$channelId")

    }


}