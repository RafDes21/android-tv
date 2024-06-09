package com.rafdev.practicestv.player

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.MediaPlayerAdapter
import androidx.leanback.media.PlaybackGlue
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.PlaybackSeekDataProvider

class MyVideoFragment : VideoSupportFragment() {

    private lateinit var transportGlue: CustomTransportControlGlue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val playerGlue = PlaybackTransportControlGlue(activity, MediaPlayerAdapter(activity))
//
//        playerGlue.host = VideoSupportFragmentGlueHost(this)
//        playerGlue.addPlayerCallback(object : PlaybackGlue.PlayerCallback() {
//            override fun onPreparedStateChanged(glue: PlaybackGlue?) {
//                if (glue?.isPrepared == true) {
//                    playerGlue.seekProvider = PlaybackSeekDataProvider()
//                }
//            }
//        })
//
//        playerGlue.subtitle = "My Demo Subtitle"
//        playerGlue.title = "My Android Tv Development"
//        val uriPath = "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
////        val uriPath = "https://live-hls-abr-cdn.livepush.io/live/bigbuckbunnyclip/index.m3u8"
//        playerGlue.playerAdapter.setDataSource(Uri.parse(uriPath))

        transportGlue = CustomTransportControlGlue(
            context = requireContext(),
            playerAdapter = BasicMediaPlayerAdapter(requireContext())
        )

        transportGlue.host = VideoSupportFragmentGlueHost(this)
        transportGlue.subtitle = "My Demo Subtitle"
        transportGlue.title = "My Android Tv Development"
//        val uriPath = "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        val uriPath = "https://redirector.dps.live/hls/t13/playlist.m3u8"
        transportGlue.playerAdapter.setDataSource(Uri.parse(uriPath))
        transportGlue.playWhenPrepared()

//        part 2
//        setOnKeyInterceptListener { view, keyCode, event ->
//            if (isControlsOverlayVisible || event.repeatCount > 0) {
////                isShowOrHideControlsOverlayOnUserInteraction = true
//            } else when (keyCode) {
//                KeyEvent.KEYCODE_DPAD_RIGHT -> {
////                    is
//                }
//            }
//
//
//            transportGlue.onKey(view, keyCode, event)
//        }
    }


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val playerGlue = PlaybackTransportControlGlue(activity, MediaPlayerAdapter(activity))
//
//        playerGlue.host = VideoSupportFragmentGlueHost(this)
//        playerGlue.addPlayerCallback(object : PlaybackGlue.PlayerCallback() {
//            override fun onPreparedStateChanged(glue: PlaybackGlue?) {
//                if (glue?.isPrepared == true) {
//                    playerGlue.seekProvider = PlaybackSeekDataProvider()
//                }
//            }
//        })
//
//        playerGlue.subtitle = "My Demo Subtitle"
//        playerGlue.title = "My Android Tv Development"
//        val uriPath = "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
////        val uriPath = "https://live-hls-abr-cdn.livepush.io/live/bigbuckbunnyclip/index.m3u8"
//        playerGlue.playerAdapter.setDataSource(Uri.parse(uriPath))
//
//    }
}