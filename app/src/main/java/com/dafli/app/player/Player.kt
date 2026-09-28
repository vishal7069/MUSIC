package com.dafli.app.player

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dafli.app.data.LocalLibrary
import com.dafli.app.data.Sample
import com.dafli.app.data.Track
import com.dafli.app.platform.EngineEvents
import com.dafli.app.platform.PlaybackEngine

enum class RepeatMode { Off, All, One }

/**
 * The app's single source of truth for "what is playing".
 * Real songs (with a streamUrl) go through [engine] (Media3 on Android).
 * Sample songs without audio are simulated so previews still move.
 */
class Player(private val library: LocalLibrary? = null) : EngineEvents {
    var engine: PlaybackEngine? = null

    var current by mutableStateOf(Sample.noor)
        private set
    /** False until the user plays something — the mini-player stays hidden until then. */
    var hasTrack by mutableStateOf(false)
        private set
    var isPlaying by mutableStateOf(false)
    var isBuffering by mutableStateOf(false)
        private set
    var positionSec by mutableIntStateOf(0)
    var durationSec by mutableIntStateOf(0)
        private set
    var shuffle by mutableStateOf(false)
    var repeat by mutableStateOf(RepeatMode.Off)
    var output by mutableStateOf("This phone")
    var playingFrom by mutableStateOf("")
    var error by mutableStateOf<String?>(null)
    /** Seconds left on the sleep timer, or null when off. -1 = end of this song. */
    var sleepLeft by mutableStateOf<Int?>(null)

    val userQueue = mutableStateListOf<Track>()
    val contextQueue = mutableStateListOf<Track>()
    private val history = mutableListOf<Track>()

    val progress: Float get() = if (durationSec <= 0) 0f else (positionSec.toFloat() / durationSec).coerceIn(0f, 1f)
    private val isReal get() = current.streamUrl != null && engine != null

    // ---------------------------------------------------------------- likes (saved on the phone)

    private val sampleLikes = mutableStateListOf<String>()
    fun isLiked(t: Track) = library?.isLiked(t) ?: (t.id in sampleLikes)
    fun toggleLike(t: Track) {
        if (library != null) library.toggleLike(t)
        else if (t.id in sampleLikes) sampleLikes.remove(t.id) else sampleLikes.add(t.id)
    }

    // ---------------------------------------------------------------- playback

    /** Play [t]; if it came from a list, the rest of that list becomes "Next from". */
    fun play(t: Track, from: String? = null, list: List<Track>? = null) {
        if (list != null) {
            val i = list.indexOfFirst { it.id == t.id }
            contextQueue.clear()
            contextQueue.addAll(if (i >= 0) list.drop(i + 1) else list.filter { it.id != t.id })
        }
        from?.let { playingFrom = it }
        if (t.id == current.id && hasTrack) { if (!isPlaying) toggle(); return }
        if (hasTrack) history.add(current)
        start(t)
    }

    private fun start(t: Track) {
        current = t
        hasTrack = true
        positionSec = 0
        durationSec = t.durationSec
        error = null
        library?.addRecent(t)
        val e = engine
        if (t.streamUrl != null && e != null) { e.load(t, playWhenReady = true); isBuffering = true }
        isPlaying = true
    }

    fun toggle() {
        if (!hasTrack) return
        if (isReal) { if (engine!!.isPlaying) engine!!.pause() else engine!!.play() }
        isPlaying = !isPlaying
    }

    fun next() {
        val nextTrack = when {
            repeat == RepeatMode.One -> current
            userQueue.isNotEmpty() -> userQueue.removeAt(0)
            contextQueue.isNotEmpty() -> contextQueue.removeAt(if (shuffle) contextQueue.indices.random() else 0)
            repeat == RepeatMode.All && history.isNotEmpty() -> history.first()
            else -> null
        }
        if (nextTrack == null) { isPlaying = false; engine?.pause(); seekTo(0f); return }
        history.add(current)
        start(nextTrack)
    }

    fun previous() {
        if (positionSec > 3 || history.isEmpty()) { seekTo(0f); return }
        val prev = history.removeAt(history.lastIndex)
        contextQueue.add(0, current)
        start(prev)
    }

    fun seekTo(fraction: Float) {
        positionSec = (fraction * durationSec).toInt()
        if (isReal) engine!!.seekTo(positionSec * 1000L)
    }

    fun seekToSec(sec: Int) = seekTo(if (durationSec > 0) sec.toFloat() / durationSec else 0f)

    fun playNext(t: Track) { userQueue.add(0, t); if (!hasTrack) next() }
    fun addToQueue(t: Track) { userQueue.add(t); if (!hasTrack) next() }

    fun cycleRepeat() {
        repeat = when (repeat) { RepeatMode.Off -> RepeatMode.All; RepeatMode.All -> RepeatMode.One; RepeatMode.One -> RepeatMode.Off }
    }

    /** Called about twice a second by the app shell. */
    fun tick(deltaSec: Int = 1) {
        if (!hasTrack) return
        val e = engine
        if (isReal && e != null) {
            positionSec = (e.positionMs / 1000).toInt()
            val d = (e.durationMs / 1000).toInt()
            if (d > 0) durationSec = d
            isBuffering = e.isBuffering
            isPlaying = e.isPlaying || e.isBuffering
        } else if (isPlaying) {
            positionSec += deltaSec
            if (positionSec >= durationSec) onEnded()
        }
        if (isPlaying) sleepLeft?.let { if (it > 0) { sleepLeft = it - deltaSec; if (it - deltaSec <= 0) { sleepLeft = null; pauseNow() } } }
    }

    private fun pauseNow() { engine?.pause(); isPlaying = false }

    // ---------------------------------------------------------------- engine events

    override fun onEnded() {
        if (sleepLeft == -1) { sleepLeft = null; pauseNow(); return }
        next()
    }

    override fun onError(message: String) { error = message; isPlaying = false; isBuffering = false }
    override fun onNextRequested() = next()
    override fun onPreviousRequested() = previous()

    /** Used by previews: pretend a sample song is playing. */
    fun demo(t: Track = Sample.noor) {
        current = t; hasTrack = true; isPlaying = true; positionSec = 108; durationSec = t.durationSec; playingFrom = "Daily Mix 1"
        contextQueue.clear(); contextQueue.addAll(listOf(Sample.chaiRain, Sample.studioNights, Sample.ghatKiSubah, Sample.rooftopNights))
        userQueue.clear(); userQueue.addAll(listOf(Sample.marigoldHours, Sample.gullyMein))
    }
}
