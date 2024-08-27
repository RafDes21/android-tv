package com.rafdev.practicestv.ui.player

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.ima.ImaAdsLoader
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.ui.DefaultTimeBar
import androidx.media3.ui.PlayerView
import com.bumptech.glide.Glide
import com.google.ads.interactivemedia.v3.api.AdEvent
import com.rafdev.practicestv.R
import dagger.hilt.android.AndroidEntryPoint


@UnstableApi
@AndroidEntryPoint
class PlayerActivity : FragmentActivity() {

    companion object {
        const val TAG_ADS = "debugAds"
        const val TAG = "debugPlayerActivity"
    }

    private lateinit var adsLoader: ImaAdsLoader
    private lateinit var player: ExoPlayer

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

    private lateinit var playerView: PlayerView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        // Inicializar las vistas
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

        // Configurar el AdsLoader
        adsLoader = ImaAdsLoader.Builder(this).setAdEventListener(buildAdEventListener()).build()

        val dataSourceFactory = DefaultDataSource.Factory(this)
        val mediaSourceFactory =
            DefaultMediaSourceFactory(dataSourceFactory).setLocalAdInsertionComponents({adsLoader}, playerView)

        val adAdsUri = Uri.parse(getString(R.string.ad_tag_url))
        val contentUri = Uri.parse("https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8")

        // Configurar el reproductor con Media3
        player = ExoPlayer.Builder(this)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()
        playerView.player = player;
        adsLoader.setPlayer(player);

        // Configurar el contenido del video con anuncios
        val mediaItem = MediaItem.Builder()
            .setUri(contentUri)
            .setAdsConfiguration(MediaItem.AdsConfiguration.Builder(adAdsUri).build())
            .build()

        // Preparar y reproducir
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

    private fun buildAdEventListener(): AdEvent.AdEventListener {
        val imaAdEventListener =
            AdEvent.AdEventListener { event: AdEvent ->

                Log.i(TAG_ADS, "ads --> $event")
                val ad = event.ad
                if (ad != null && !ad.isLinear) {
                    playerView.overlayFrameLayout?.let { overlayLayout ->
                        // Puedes agregar vistas personalizadas al overlayLayout para mostrar el anuncio no lineal
                        // Por ejemplo, podrías mostrar un banner o una imagen
//                        findViewById<TextView>(R.id.title).text = ad.title
                        val imageUrl = ad.contentType // Verifica si esta es la URL de la imagen

                        // Mostrar la imagen en el ImageView
                        val adImageView: ImageView = findViewById(R.id.ad_image_view)
                        // Usar Picasso para cargar la imagen
                        Glide.with(this)
                            .load(imageUrl)
                            .into(adImageView)

                        Log.i(TAG_ADS, "Anuncio no lineal detectado: ${ad.title}")
                    }
                }
            }

        return imaAdEventListener
    }

    private fun setupButtonListeners() {
        playPauseButton.setOnClickListener {
            playPauseButton.isPressed = true
            if (player.isPlaying) {
                player.pause()
                playPauseButton.setImageResource(R.drawable.ic_play)
            } else {
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
