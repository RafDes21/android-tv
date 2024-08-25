package com.rafdev.practicestv.ui.player

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.DefaultTimeBar
import androidx.media3.ui.PlayerView
import com.rafdev.practicestv.R
import dagger.hilt.android.AndroidEntryPoint

@UnstableApi
@AndroidEntryPoint
class PlayerActivity : FragmentActivity() {

    private lateinit var playPauseButton: ImageView
    private lateinit var backButton: ImageView
    private lateinit var controlsContainer: FrameLayout
    private lateinit var handler: Handler
    private lateinit var hideControlsRunnable: Runnable
    private lateinit var timeBar: DefaultTimeBar
    private val updateProgressRunnable = object : Runnable {
        override fun run() {
            updateTimeBar()
            handler.postDelayed(this, 1000) // Actualiza cada segundo
        }
    }

    companion object {
        const val TAG = "debugPlayerActivity"
    }

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerView = findViewById(R.id.video_surface)
        controlsContainer = findViewById(R.id.controls_container)

        val controlsView =
            layoutInflater.inflate(R.layout.custom_playback_controls, controlsContainer, true)
        playPauseButton = controlsView.findViewById(R.id.play_pause_button)

        backButton = controlsView.findViewById(R.id.ic_back)

        timeBar = controlsView.findViewById(R.id.exo_progress)
        timeBar.setBufferedColor(ContextCompat.getColor(this, R.color.test))
        timeBar.setPlayedColor(ContextCompat.getColor(this, R.color.test1))
        timeBar.setScrubberColor(ContextCompat.getColor(this, R.color.test2))
        timeBar.setUnplayedColor(ContextCompat.getColor(this, R.color.test3))

        // Configurar el reproductor
        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        // Cargar el video
        val mediaItem =
            MediaItem.fromUri("https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8") // Cambia a tu URL de video
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true

        controlsContainer.visibility = View.GONE

        handler = Handler(Looper.getMainLooper())
        hideControlsRunnable = Runnable { hideControls() }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (controlsContainer.visibility == View.VISIBLE) {
                    controlsContainer.visibility = View.GONE
                    handler.removeCallbacks(hideControlsRunnable)
                } else {
                    finish()
                }
            }
        })
        setupButtonListeners()
        setupDirectionalPadListener()
        handler.post(updateProgressRunnable)
    }

    private fun setupButtonListeners() {
        playPauseButton.setOnClickListener {
            playPauseButton.isPressed = true
            if (player.isPlaying){
                player.pause()
                playPauseButton.setImageResource(R.drawable.ic_play)
            }else{
                player.play()
                playPauseButton.setImageResource(R.drawable.ic_pause)
            }
        }
    }

    private fun setupDirectionalPadListener() {
        playerView.setOnKeyListener { _, keyCode, event ->
            if (event?.action == KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_DPAD_DOWN,
                    KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        showControls()
                        return@setOnKeyListener true
                    }
                }
            }
            false
        }

        playPauseButton.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                handler.postDelayed(hideControlsRunnable, 5000)
            } else {
                handler.removeCallbacks(hideControlsRunnable)
            }
        }
       backButton.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                handler.postDelayed(hideControlsRunnable, 5000)
            } else {
                handler.removeCallbacks(hideControlsRunnable)
            }
        }
    }

    private fun showControls() {
        controlsContainer.visibility = View.VISIBLE
        controlsContainer.alpha = 0f
        controlsContainer.animate()
            .alpha(1f)
            .setDuration(300)
            .setListener(null)

        playPauseButton.requestFocus()
//        handler.postDelayed(hideControlsRunnable, 5000)
    }

    private fun hideControls() {
        controlsContainer.animate()
            .alpha(0f)
            .setDuration(300)
            .withEndAction { controlsContainer.visibility = View.GONE }
    }

    private fun updateTimeBar() {
        val duration = player.duration
        val position = player.currentPosition
        val bufferedPosition = player.bufferedPosition

        if (duration > 0) {
            timeBar.setDuration(duration)
            timeBar.setPosition(position)
            timeBar.setBufferedPosition(bufferedPosition)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
        handler.removeCallbacks(hideControlsRunnable)
    }

    override fun onResume() {
        super.onResume()
        playerView.requestFocus()
    }
}

//    @SuppressLint("RestrictedApi")
//    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
//        if (event.action == KeyEvent.ACTION_DOWN) {
//            when (event.keyCode) {
//                KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_DPAD_DOWN,
//                KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT -> {
//                    showControls()
//                    return true
//                }
//            }
//        }
//        return super.dispatchKeyEvent(event)
//    }
