package com.rafdev.practicestv.ui.player

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.media3.common.util.Util
import androidx.media3.ui.leanback.LeanbackPlayerAdapter
import com.rafdev.practicestv.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class  PlaybackVideoFragment : VideoSupportFragment(){

    private var player: ExoPlayer? = null
    private var playerGlue: PlaybackTransportControlGlue<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @OptIn(UnstableApi::class)
    private fun preparePlayer(context: Context, player: ExoPlayer, videoUri: String) {
        val mediaItem = MediaItem.fromUri(videoUri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    @OptIn(UnstableApi::class)
    private fun initPlayer() {
        val playerView = requireActivity().findViewById<PlayerView>(R.id.player)

        player = ExoPlayer.Builder(requireContext()).build()
        playerView.player = player

        val playerAdapter = LeanbackPlayerAdapter(requireContext(), player!!, 16)
        playerGlue = PlaybackTransportControlGlue(requireContext(), playerAdapter)
        playerGlue?.host = VideoSupportFragmentGlueHost(this)


        val url = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"
        preparePlayer(requireContext(), player!!, url)
    }


    private fun releasePlayer() {
        player?.release()
        player = null
    }

    @OptIn(UnstableApi::class)
    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initPlayer()
        }
    }

    @OptIn(UnstableApi::class)
    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24 || player == null) {
            initPlayer()
        }
    }

    @OptIn(UnstableApi::class)
    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    @OptIn(UnstableApi::class)
    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

}

//import android.content.Context
//import android.net.Uri
//import android.os.Bundle
//import androidx.annotation.OptIn
//import androidx.leanback.app.PlaybackSupportFragmentGlueHost
//import androidx.leanback.app.VideoSupportFragment
//import androidx.leanback.app.VideoSupportFragmentGlueHost
//import androidx.leanback.media.MediaPlayerAdapter
//import androidx.leanback.media.PlaybackGlue
//import androidx.leanback.media.PlaybackTransportControlGlue
//import androidx.leanback.widget.ArrayObjectAdapter
//import androidx.leanback.widget.PlaybackSeekDataProvider
//import androidx.media3.common.MediaItem
//import androidx.media3.common.util.UnstableApi
//import androidx.media3.common.util.Util
//import androidx.media3.exoplayer.ExoPlayer
//import androidx.media3.ui.PlayerView
//import androidx.media3.ui.leanback.LeanbackPlayerAdapter
//import com.rafdev.practicestv.R
//
//class PlaybackVideoFragment : VideoSupportFragment() {
//
//    private var player: ExoPlayer? = null
//    private var playerGlue: PlaybackTransportControlGlue<*>? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        backgroundType = 2
////        progressBarManager.disableProgressBar()
//    }
//
//    @OptIn(UnstableApi::class)
//    private fun preparePlayer(context: Context, player: ExoPlayer, videoUri: String) {
////        val dataSourceFactory: DefaultDataSource.Factory = DefaultDataSource.Factory(context)
////
////        val videoSource: MediaSource = HlsMediaSource.Factory(dataSourceFactory)
////            .createMediaSource(MediaItem.fromUri(videoUri))
////
////        player.setMediaSource(videoSource)
////        player.prepare()
//        val mediaItem = MediaItem.fromUri(videoUri)
//        player.setMediaItem(mediaItem)
//        player.prepare()
//        player.play()
//    }
//
//    @OptIn(UnstableApi::class)
//    private fun initPlayer() {
//        val playerView = requireActivity().findViewById<PlayerView>(R.id.player)
//
//        player = ExoPlayer.Builder(requireContext()).build()
//        playerView.player = player
//
//        val playerAdapter = LeanbackPlayerAdapter(requireContext(), player!!, 16)
//        playerGlue = PlaybackTransportControlGlue(requireContext(), playerAdapter)
//        playerGlue?.host = VideoSupportFragmentGlueHost(this)
////        playerGlue?.setHost(VideoSupportFragmentGlueHost(this))//
////        playerGlue = VideoPlayerGlue(requireContext(), playerAdapter, this)
////        playerGlue?.title = "Avances"
////        playerGlue?.subtitle = "EN VIVO"
////        playerGlue?.playWhenPrepared()
////
//        adapter = ArrayObjectAdapter()
//
//
//        val url = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"
////        val url = "https://redirector.dps.live/hls/t13/playlist.m3u8"
//        preparePlayer(requireContext(), player!!, url)
//    }
//
//    private fun releasePlayer() {
//        player?.release()
//        player = null
//    }
//
//    @OptIn(UnstableApi::class)
//    override fun onStart() {
//        super.onStart()
//        if (Util.SDK_INT >= 24) {
//            initPlayer()
//        }
//    }
//
//    @OptIn(UnstableApi::class)
//    override fun onResume() {
//        super.onResume()
//        if (Util.SDK_INT < 24 || player == null) {
//            initPlayer()
//        }
//    }
//
//    @OptIn(UnstableApi::class)
//    override fun onPause() {
//        super.onPause()
//        if (Util.SDK_INT < 24) {
//            releasePlayer()
//        }
//    }
//
//    @OptIn(UnstableApi::class)
//    override fun onStop() {
//        super.onStop()
//        if (Util.SDK_INT >= 24) {
//            releasePlayer()
//        }
//    }
//
//}
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


//val playerAdapter = LeanbackPlayerAdapter(requireContext(), player!!, 16)
//playerGlue = PlaybackTransportControlGlue(requireContext(), playerAdapter)
////        playerGlue = VideoPlayerGlue(requireContext(), playerAdapter, this)
//playerGlue?.host = VideoSupportFragmentGlueHost(this)