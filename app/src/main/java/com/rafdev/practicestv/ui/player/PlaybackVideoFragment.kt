package com.rafdev.practicestv.ui.player

import android.net.Uri
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.MediaPlayerAdapter
import androidx.leanback.media.PlaybackGlue
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.PlaybackSeekDataProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.media3.ui.leanback.LeanbackPlayerAdapter
import com.rafdev.practicestv.R
//@OptIn(androidx.media3.common.util.UnstableApi::class)
class PlaybackVideoFragment : VideoSupportFragment() {

    private lateinit var playerGlue: PlaybackTransportControlGlue<MediaPlayerAdapter>

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initPlayer()
    }

    private fun initPlayer() {
        val playerAdapter = MediaPlayerAdapter(requireContext())

        playerGlue = object : PlaybackTransportControlGlue<MediaPlayerAdapter>(requireContext(), playerAdapter) {

        }.apply {
            title = "Demo Live"
            subtitle = "Demo"

            playerAdapter.setDataSource(Uri.parse("https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"))

            playerAdapter.play()
        }
    }
}
//    private lateinit var player: ExoPlayer
//    private var mTransportControlGlue: PlaybackTransportControlGlue<*>? = null
//    private var mPlayerAdapter: LeanbackPlayerAdapter? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        initPlayer()
//    }

//    private fun initPlayer() {
//        player = ExoPlayer.Builder(requireContext()).build()
//        val playerView: PlayerView = requireActivity().findViewById(R.id.player)
//        playerView.player = player
//        val url = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"
//        val mediaItem = MediaItem.fromUri(url)
//        player.setMediaItem(mediaItem)
//
//        mPlayerAdapter = LeanbackPlayerAdapter(requireActivity(), player, 1000)
//        mTransportControlGlue = PlaybackTransportControlGlue(activity, mPlayerAdapter)
//        mTransportControlGlue!!.host = VideoSupportFragmentGlueHost(this)
//
//        mTransportControlGlue!!.playWhenPrepared()
//        mTransportControlGlue!!.setSeekEnabled(true)
//        mPlayerAdapter!!.play()
//    }


//        private fun initPlayer() {
//        val playerGlue = PlaybackTransportControlGlue(activity, MediaPlayerAdapter(activity))
//        playerGlue.addPlayerCallback(object : PlaybackGlue.PlayerCallback(){
//            override fun onPreparedStateChanged(glue: PlaybackGlue?) {
//                if (glue?.isPrepared == true){
//                    playerGlue.seekProvider = PlaybackSeekDataProvider()
//                    playerGlue.play()
//                }
//            }
//        })
//
//        playerGlue.subtitle = "Demo"
//        playerGlue.title = "Demo Live"
//
//        val uriPath = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"
//        playerGlue.playerAdapter.setDataSource(Uri.parse(uriPath))
//    }


//        player.prepare()
//        player.play()
//    }
//        val playerAdapter = LeanbackPlayerAdapter(requireContext(), player, 16)
//        playerGlue = VideoPlayerGlue(requireContext(), playerAdapter, this)
//        playerGlue.host = PlaybackSupportFragmentGlueHost(this)
//        playerGlue.playWhenPrepared()
//
//        setAdapter(ArrayObjectAdapter())


    // Implement Player.EventListener methods
    // Implement VideoPlayerGlue.OnActionClickedListener methods


//@OptIn(UnstableApi::class)
//private fun initPlayer() {
//    // Configura ExoPlayer
//    player = ExoPlayer.Builder(requireContext()).build()
//    val mediaItem = MediaItem.fromUri("https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8")
//    player.setMediaItem(mediaItem)
//
//    // Configura el LeanbackPlayerAdapter
//    val playerAdapter = LeanbackPlayerAdapter(requireContext(), player, 1000)
//
//    // Configura el PlaybackTransportControlGlue
//    playerGlue = PlaybackTransportControlGlue(requireContext(), playerAdapter)
//    playerGlue.host = VideoSupportFragmentGlueHost(this) // Esto es crucial para que los controles se muestren
//    playerGlue.isSeekEnabled = true
//    playerGlue.title = "Sample Video"
//    playerGlue.subtitle = "Playing with Leanback Controls"
//
//    // Prepara y reproduce el video
//    playerGlue.playWhenPrepared()
//    player.prepare()
//}


//    private fun initPlayer() {
//        val playerGlue = PlaybackTransportControlGlue(activity, MediaPlayerAdapter(activity))
//        playerGlue.addPlayerCallback(object :PlaybackGlue.PlayerCallback(){
//            override fun onPreparedStateChanged(glue: PlaybackGlue?) {
//                if (glue?.isPrepared == true){
//                    playerGlue.seekProvider = PlaybackSeekDataProvider()
//                    playerGlue.play()
//                }
//            }
//        })
//
////        playerGlue.host = VideoSupportFragmentGlueHost(this)
//        playerGlue.subtitle = "Demo"
//        playerGlue.title = "Demo Live"
//
//        val uriPath = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"
//        playerGlue.playerAdapter.setDataSource(Uri.parse(uriPath))
//    }