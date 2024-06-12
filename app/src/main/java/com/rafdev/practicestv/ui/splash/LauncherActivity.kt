package com.rafdev.practicestv.ui.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.rafdev.practicestv.R
import com.rafdev.practicestv.databinding.ActivityLauncherBinding
import com.rafdev.practicestv.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {

    private val viewModel: LauncherViewModel by viewModels()
    private var player: ExoPlayer? = null
    private var playerView: PlayerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerView = binding.playerViewSplash
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            playerView?.player = exoPlayer
            val uri = Uri.parse("asset:///splash.mp4")
            val mediaItem = MediaItem.fromUri(uri)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            binding.playerViewSplash.visibility = View.GONE
        }

        viewModel.showSplash.asLiveData().observe(this) {
            if (it) {
                binding.playerViewSplash.visibility = View.VISIBLE
                player?.playWhenReady = true
                player?.addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_ENDED) {
                            startMainActivity()
                        }
                    }
                })
            } else {
                binding.playerViewSplash.visibility = View.GONE
            }
        }
    }

    fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        playerView = null
    }

    override fun onPause() {
        super.onPause()
        player?.release()
        playerView = null
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        playerView = null
    }
}