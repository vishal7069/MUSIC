package com.dafli.app.player

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player as M3Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.dafli.app.data.Track
import com.dafli.app.platform.EngineEvents
import com.dafli.app.platform.PlaybackEngine

/**
 * Talks to [PlaybackService] through a MediaController, so audio keeps playing in the
 * background and the system shows the media notification / lock-screen controls.
 */
class Media3Engine(context: Context, private val events: EngineEvents) : PlaybackEngine {
    private var controller: MediaController? = null
    private var pending: (MediaController.() -> Unit)? = null

    init {
        PlaybackBridge.events = events
        val token = SessionToken(context, ComponentName(context, PlaybackService::class.java))
        val future = MediaController.Builder(context, token).buildAsync()
        future.addListener({
            val c = runCatching { future.get() }.getOrNull() ?: return@addListener
            controller = c
            c.addListener(object : M3Player.Listener {
                override fun onPlaybackStateChanged(state: Int) { if (state == M3Player.STATE_ENDED) events.onEnded() }
                override fun onPlayerError(error: PlaybackException) { events.onError("Couldn’t play this song. Check your connection.") }
            })
            pending?.invoke(c); pending = null
        }, ContextCompat.getMainExecutor(context))
    }

    private fun withController(block: MediaController.() -> Unit) {
        val c = controller
        if (c != null) c.block() else pending = block
    }

    override fun load(track: Track, playWhenReady: Boolean) = withController {
        val meta = MediaMetadata.Builder().setTitle(track.title).setArtist(track.artist)
            .apply { track.artUrl?.let { setArtworkUri(Uri.parse(it)) } }.build()
        setMediaItem(MediaItem.Builder().setMediaId(track.id).setUri(track.streamUrl).setMediaMetadata(meta).build())
        prepare()
        this.playWhenReady = playWhenReady
    }

    override fun play() = withController { play() }
    override fun pause() = withController { pause() }
    override fun seekTo(ms: Long) = withController { seekTo(ms) }
    override val positionMs: Long get() = controller?.currentPosition ?: 0L
    override val durationMs: Long get() = controller?.duration?.takeIf { it > 0 } ?: 0L
    override val isPlaying: Boolean get() = controller?.isPlaying ?: false
    override val isBuffering: Boolean get() = controller?.let { it.playbackState == M3Player.STATE_BUFFERING && it.playWhenReady } ?: false
    override fun release() { controller?.release(); controller = null }
}

/** Lets the notification's next/previous buttons reach the app's own queue. */
object PlaybackBridge {
    var events: EngineEvents? = null
}
