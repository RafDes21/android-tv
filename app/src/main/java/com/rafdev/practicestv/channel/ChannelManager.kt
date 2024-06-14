package com.rafdev.practicestv.channel

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.tvprovider.media.tv.Channel
import androidx.tvprovider.media.tv.ChannelLogoUtils.storeChannelLogo
import androidx.tvprovider.media.tv.PreviewProgram
import androidx.tvprovider.media.tv.TvContractCompat
import com.rafdev.practicestv.R

class ChannelManager(private val context: Context) {

    companion object {
        const val CHANNEL = "channelManager"
    }

    @SuppressLint("RestrictedApi")
    fun createChannel() {
        // Crear el canal
        val channelBuilder = Channel.Builder()
        channelBuilder.setType(TvContractCompat.Channels.TYPE_PREVIEW)
            .setDisplayName("Recomendados")

        val channelUri = context.contentResolver.insert(
            TvContractCompat.Channels.CONTENT_URI, channelBuilder.build().toContentValues()
        )

        val channelId = ContentUris.parseId(channelUri!!)

        // add logo Bitmap
        val logoDrawable = ContextCompat.getDrawable(context, R.drawable.ic_icon)
        val logoBitmap = (logoDrawable as BitmapDrawable).bitmap
        storeChannelLogo(context, channelId, logoBitmap)

        // add programs to channel
        val programBuilder = PreviewProgram.Builder()
        programBuilder.setChannelId(channelId)
            .setType(TvContractCompat.PreviewPrograms.TYPE_CLIP)
            .setTitle("Title")
            .setDescription("Program description")
            .setPosterArtUri(Uri.parse("https://cdn.pixabay.com/photo/2022/08/28/01/40/cyberpunk-city-7415576_1280.jpg"))
            .setInternalProviderId(channelId.toString())

        val programUri = context.contentResolver.insert(
            TvContractCompat.PreviewPrograms.CONTENT_URI,
            programBuilder.build().toContentValues()
        )

        val programId = ContentUris.parseId(programUri!!)

        Log.e(CHANNEL, "channelId=$channelId, programId=$programId")
    }
}

//    fun createChannel() {
//        val builder = Channel.Builder()
//        builder.setType(TvContractCompat.Channels.TYPE_PREVIEW)
//            .setDisplayName("Recomendados")
////            .setAppLinkIntentUri(uri)
//
//
//        var channelUri = context.contentResolver.insert(
//            TvContractCompat.Channels.CONTENT_URI, builder.build().toContentValues()
//        )
//
//        var channelId = ContentUris.parseId(channelUri!!)
//
//        val logoDrawable = ContextCompat.getDrawable(context, R.drawable.ic_icon)
//        val logoBitmap = (logoDrawable as BitmapDrawable).bitmap
//
//        storeChannelLogo(context, channelId, logoBitmap)
//
//
//        //add programs
//
//        val builderP = PreviewProgram.Builder()
//        builderP.setChannelId(channelId)
//            .setType(TvContractCompat.PreviewPrograms.TYPE_CLIP)
//            .setTitle("Title")
//            .setDescription("Program description")
//            .setPosterArtUri(Uri.parse("https://cdn.pixabay.com/photo/2022/08/28/01/40/cyberpunk-city-7415576_1280.jpg"))
//            .setInternalProviderId(channelId.toString())
//
//        var programUri = context.contentResolver.insert(
//            TvContractCompat.PreviewPrograms.CONTENT_URI,
//            builder.build().toContentValues()
//        )
//
//        val programId = ContentUris.parseId(programUri!!)
//
//        Log.e(CHANNEL, "channelId=$channelId, programId=$programId")
//
//    }

