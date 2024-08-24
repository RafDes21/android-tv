package com.rafdev.practicestv.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.rafdev.practicestv.R

class VideoPlayerFragment : Fragment() {

    private var player: ExoPlayer? = null
    private lateinit var surfaceView: SurfaceView
    private lateinit var controlsContainer: FrameLayout
    private lateinit var customControlsView: View

    private val hideControlsRunnable = Runnable {
        // Usar animaciones para desvanecer los controles
        controlsContainer.animate()
            .alpha(0f)
            .setDuration(300)
            .withEndAction { controlsContainer.visibility = View.GONE }
    }

    private val handler = Handler(Looper.getMainLooper())
    private val controlVisibilityDelay = 3000L // 3 segundos

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_player_video, container, false)
        surfaceView = rootView.findViewById(R.id.video_surface)
        controlsContainer = rootView.findViewById(R.id.controls_container)
        customControlsView = LayoutInflater.from(requireContext())
            .inflate(R.layout.custom_playback_controls, controlsContainer, false)
        controlsContainer.addView(customControlsView)

        val playPauseButton = customControlsView.findViewById<ImageView>(R.id.play_pause_button)
        playPauseButton.requestFocus()

        playPauseButton.setOnClickListener {
            togglePlayPause()
        }

        // Configura el OnKeyListener para manejar eventos de teclado
        rootView.setOnKeyListener { _, keyCode, event ->
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

        // Asegúrate de que el contenedor pueda recibir el enfoque
        rootView.isFocusable = true
        rootView.isFocusableInTouchMode = true

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePlayer()
        setupControlsVisibility()

        // Maneja el botón de retroceso
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (controlsContainer.visibility == View.VISIBLE) {
                    // Si los controles están visibles, ocúltalos en lugar de salir del fragmento
                    hideControlsRunnable.run()
                } else {
                    requireActivity().onBackPressed() // De lo contrario, llama al comportamiento predeterminado
                }
            }
        })
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(requireContext()).build()
        val mediaItem = MediaItem.fromUri("https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8")
        player?.setMediaItem(mediaItem)
        player?.prepare()

        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                player?.setVideoSurfaceHolder(holder)
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                // Maneja los cambios en el Surface si es necesario
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                player?.clearVideoSurface()
            }
        })

        player?.playWhenReady = true
    }

    private fun setupControlsVisibility() {
        // Mostrar controles al tocar la pantalla
        controlsContainer.setOnTouchListener { _, _ ->
            showControls()
            true
        }

        // Manejamos la visibilidad de los controles con un temporizador
        handler.postDelayed(hideControlsRunnable, controlVisibilityDelay)
    }

    private fun showControls() {
        controlsContainer.visibility = View.VISIBLE
        controlsContainer.animate()
            .alpha(1f)
            .setDuration(300)
            .setListener(null)
        handler.removeCallbacks(hideControlsRunnable)
        handler.postDelayed(hideControlsRunnable, controlVisibilityDelay)
    }

    private fun togglePlayPause() {
        if (player?.isPlaying == true) {
            player?.pause()
        } else {
            player?.play()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player?.release()
        player = null
    }
}
