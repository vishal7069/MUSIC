package com.dafli.app.platform

import com.dafli.app.data.Track

/** Simple persistent key/value storage (SharedPreferences on Android). */
interface KeyValueStore {
    fun getString(key: String): String?
    fun putString(key: String, value: String?)
}

class MemoryStore : KeyValueStore {
    private val map = HashMap<String, String>()
    override fun getString(key: String) = map[key]
    override fun putString(key: String, value: String?) { if (value == null) map.remove(key) else map[key] = value }
}

/** Real audio output. Android: Media3 ExoPlayer inside a MediaSessionService. */
interface PlaybackEngine {
    fun load(track: Track, playWhenReady: Boolean)
    fun play()
    fun pause()
    fun seekTo(ms: Long)
    val positionMs: Long
    val durationMs: Long
    val isPlaying: Boolean
    val isBuffering: Boolean
    fun release()
}

/** Everything the shared UI needs from the operating system. */
interface Platform {
    val store: KeyValueStore
    /** Returns null when there is no audio output (previews, tests). */
    fun createEngine(events: EngineEvents): PlaybackEngine?
    fun share(title: String, text: String)
    fun openUrl(url: String)
    fun sendEmail(to: String, subject: String)
    val appVersion: String
}

interface EngineEvents {
    fun onEnded()
    fun onError(message: String)
    fun onNextRequested()
    fun onPreviousRequested()
}

/** Used by previews / desktop renders. */
class PreviewPlatform : Platform {
    override val store = MemoryStore()
    override fun createEngine(events: EngineEvents): PlaybackEngine? = null
    override fun share(title: String, text: String) {}
    override fun openUrl(url: String) {}
    override fun sendEmail(to: String, subject: String) {}
    override val appVersion = "1.0.0"
}
