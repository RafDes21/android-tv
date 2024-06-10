package com.rafdev.practicestv

import android.content.Context
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.datasource.DataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.ui.PlayerView
import androidx.leanback.app.PlaybackSupportFragment
import androidx.leanback.app.PlaybackSupportFragmentGlueHost
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.media3.common.Player
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.HttpDataSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.leanback.LeanbackPlayerAdapter

class LeanbackFragmentPlayer : PlaybackSupportFragment(), Player.Listener,
    VideoPlayerGlue.OnActionClickedListener {

    private var player: ExoPlayer? = null
    private var playerGlue: VideoPlayerGlue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backgroundType = 2
//        progressBarManager.disableProgressBar()

    }

    @OptIn(UnstableApi::class)
    private fun preparePlayer(context: Context, player: ExoPlayer, videoUri: String) {
//        val dataSourceFactory: DefaultDataSource.Factory = DefaultDataSource.Factory(context)
//
//        val videoSource: MediaSource = HlsMediaSource.Factory(dataSourceFactory)
//            .createMediaSource(MediaItem.fromUri(videoUri))
//
//        player.setMediaSource(videoSource)
//        player.prepare()
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
//        playerGlue = VideoPlayerGlue(requireContext(), playerAdapter, this)
//        playerGlue?.setHost(PlaybackSupportFragmentGlueHost(this))
//        playerGlue?.title = "Avances"
//        playerGlue?.subtitle = "EN VIVO"
//        playerGlue?.playWhenPrepared()
//
//        adapter = ArrayObjectAdapter()


        val url = "https://storage.googleapis.com/shaka-demo-assets/angel-one-hls/hls.m3u8"
//        val url = "https://redirector.dps.live/hls/t13/playlist.m3u8"
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

    override fun onPrevious() {
        // Implement your previous action here
    }

    override fun onNext() {
        // Implement your next action here
    }

    override fun onBack() {
        TODO("Not yet implemented")
    }
}