package com.rafdev.practicestv.ui.player

import android.os.Bundle
import androidx.annotation.OptIn
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.util.UnstableApi
import com.rafdev.practicestv.R

class PlayerActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
    }

//    private fun initPlayer() {
//        val player = ExoPlayer.Builder(this).build()
//        val playerView = findViewById<PlayerView>(R.id.player)
//        playerView.player = player
//
//        val url = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"
//
//        val mediaItem = MediaItem.fromUri(url)
//        player.setMediaItem(mediaItem)
//        player.prepare()
//        player.play()
//    }
}
