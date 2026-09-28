package com.dafli.app.data

import androidx.compose.ui.graphics.Color

/** All artwork shipped in res/drawable-nodpi (generated for the case study). */
enum class Art { A, B, C, D, E, F, G, H, I, J, K, KabirSen, AravKapoor, IraMenon, W2, Hero, Bollywood, Punjabi, HipHop, Devotional, Indie, Workout, Retro, LoFi }

data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val art: Art,
    val durationSec: Int,
    val explicit: Boolean = false,
    val album: String = title,
    /** Remote cover (Audius). When null the bundled [art] is shown. */
    val artUrl: String? = null,
    /** Playable audio URL. Null = sample track (UI only, no audio). */
    val streamUrl: String? = null,
    val artistId: String? = null,
    val genre: String? = null,
    /** Public web link used for sharing. */
    val shareUrl: String? = null,
)

data class Artist(
    val name: String,
    val art: Art,
    val tagline: String = "Artist",
    val id: String? = null,
    val artUrl: String? = null,
    val followers: Int = 0,
    val bio: String? = null,
)

enum class Kind { Album, Playlist, Mix, Single, EP, Folder, Artist }

data class Collection(
    val id: String,
    val title: String,
    val subtitle: String,
    val art: Art,
    val kind: Kind = Kind.Playlist,
    val artUrl: String? = null,
    /** Audius playlist id when this is a real playlist. */
    val remoteId: String? = null,
    /** Audius genre when this collection is "trending in genre". */
    val genre: String? = null,
)

data class Person(val name: String, val handle: String, val initial: String, val tint: Color)

/** Accessible string for track durations, e.g. 3:48. */
fun Int.asTime(): String = "%d:%02d".format(this / 60, this % 60)
