package com.rafdev.practicestv.ui.player

import android.content.Context
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.media.PlayerAdapter
import androidx.leanback.widget.Action
import androidx.leanback.widget.PlaybackControlsRow
import androidx.leanback.widget.ArrayObjectAdapter
import java.util.concurrent.TimeUnit

class VideoPlayerGlue(
    context: Context,
    playerAdapter: PlayerAdapter,
    private val actionListener: OnActionClickedListener
) : PlaybackTransportControlGlue<PlayerAdapter>(context, playerAdapter) {

    companion object {
        private val TEN_SECONDS = TimeUnit.SECONDS.toMillis(10)
    }

    interface OnActionClickedListener {
        fun onPrevious()
        fun onNext()
    }

    private val repeatAction = PlaybackControlsRow.RepeatAction(context)
    private val thumbsUpAction = PlaybackControlsRow.ThumbsUpAction(context).apply {
        index = PlaybackControlsRow.ThumbsUpAction.INDEX_OUTLINE
    }
    private val thumbsDownAction = PlaybackControlsRow.ThumbsDownAction(context).apply {
        index = PlaybackControlsRow.ThumbsDownAction.INDEX_OUTLINE
    }
    private val skipPreviousAction = PlaybackControlsRow.SkipPreviousAction(context)
    private val skipNextAction = PlaybackControlsRow.SkipNextAction(context)
    private val fastForwardAction = PlaybackControlsRow.FastForwardAction(context)
    private val rewindAction = PlaybackControlsRow.RewindAction(context)
    private val closedCaptionAction = PlaybackControlsRow.ClosedCaptioningAction(context)
    private val shuffleAction = PlaybackControlsRow.ShuffleAction(context)
    private val pictureAction = PlaybackControlsRow.PictureInPictureAction(context)

    override fun onCreatePrimaryActions(adapter: ArrayObjectAdapter) {
        super.onCreatePrimaryActions(adapter)
        adapter.add(rewindAction)
        adapter.add(skipPreviousAction)
        adapter.add(skipNextAction)
        adapter.add(fastForwardAction)
    }

    override fun onCreateSecondaryActions(adapter: ArrayObjectAdapter) {
        // Implement this method if needed
    }

    override fun onActionClicked(action: Action) {
        if (shouldDispatchAction(action)) {
            when (action) {
                fastForwardAction -> {
                    playerAdapter.seekTo(playerAdapter.currentPosition + 60000)
                }

                else -> return
            }
            return
        }
        super.onActionClicked(action)
    }

    private fun shouldDispatchAction(action: Action): Boolean {
        return action == rewindAction ||
                action == fastForwardAction ||
                action == thumbsDownAction ||
                action == thumbsUpAction ||
                action == repeatAction ||
                action == shuffleAction ||
                action == closedCaptionAction ||
                action == pictureAction
    }

    override fun next() {
        actionListener.onNext()
    }

    override fun previous() {
        actionListener.onPrevious()
    }
}