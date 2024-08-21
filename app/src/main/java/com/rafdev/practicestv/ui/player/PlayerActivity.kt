package com.rafdev.practicestv.ui.player

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.rafdev.practicestv.R

class PlayerActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.video_fragment, PlaybackVideoFragment())
                .commit()
        }
//        initPlayer()
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
