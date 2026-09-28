package com.dafli.app.nav

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dafli.app.data.Art
import com.dafli.app.data.Artist
import com.dafli.app.data.Collection
import com.dafli.app.data.Person
import com.dafli.app.data.Track

/** Every screen from the Figma file. Numbers match the case study (S01–S54). */
sealed interface Route {
    // Brand + onboarding
    data object Splash : Route            // S01
    data object Welcome : Route           // S02
    data object SignUp : Route            // S03
    data object LogIn : Route             // S04
    data object ResetPassword : Route     // S05
    data object Preferences : Route       // S06

    // Tabs
    data object Home : Route              // S07
    data object Search : Route            // S11
    data object Library : Route           // S23

    // Home
    data object SeeAllMadeForYou : Route  // S08
    data object Updates : Route           // S09
    data object History : Route           // S10

    // Search
    data object SearchTyping : Route      // S12
    data class SearchResults(val query: String) : Route // S13
    data class Genre(val name: String) : Route          // S14
    data object VoiceSearch : Route       // S54

    // Detail
    data class ArtistPage(val artist: Artist? = null) : Route          // S15
    data class AlbumPage(val album: Collection? = null) : Route        // S16
    data class PlaylistPage(val playlist: Collection? = null) : Route  // S17

    // Player
    data object NowPlaying : Route        // S19
    data object Queue : Route             // S20
    data object Lyrics : Route            // S21
    data object MusicVideo : Route        // S52

    // Library
    data object LikedSongs : Route        // S24
    data object CreatePlaylist : Route    // S25
    data object EditPlaylist : Route      // S26
    data object Collaborators : Route     // S27
    data object Folder : Route            // S28
    data object Downloads : Route         // S29
    data object LocalFiles : Route        // S43

    // Profile & settings
    data object MyProfile : Route         // S32
    data class OtherProfile(val person: Person) : Route // S33
    data object Settings : Route          // S34
    data object Playback : Route          // S35
    data object AudioStorage : Route      // S36
    data object Privacy : Route           // S37
    data object Account : Route           // S38
    data object Help : Route              // S39

    // Social & more
    data object ListenTogether : Route    // S42
    data object SharedMix : Route         // S44
    data object Recap : Route             // S45
    data object CreateWithAi : Route      // S46
    data object Messages : Route          // S47
    data class Chat(val person: Person) : Route // S48
    data object YourTaste : Route         // S53
    data object PrivacyPolicy : Route
    /** Genre picker reused from onboarding, opened from Settings. */
    data object EditTaste : Route
}

/** Bottom sheets (drawn over the current screen with a scrim). */
sealed interface Sheet {
    data class TrackMenu(val track: Track) : Sheet          // S22
    data object OutputDevice : Sheet                        // S30
    data object SleepTimer : Sheet                          // S31
    data class Share(val title: String, val subtitle: String, val art: Art) : Sheet // S41
    data class Report(val what: String, val owner: String, val title: String = what, val ref: String = "") : Sheet // S40
}

enum class Tab { Home, Search, Library }

class Entry(val id: Long, val route: Route)

/**
 * Tiny back-stack navigator. Kept deliberately simple (no Navigation library) so every
 * screen is a plain composable and the flow reads like the Figma prototype.
 */
class Navigator(start: Route) {
    private var nextId = 0L
    val stack = mutableStateListOf(Entry(nextId++, start))
    var sheet by mutableStateOf<Sheet?>(null)
    var toast by mutableStateOf<String?>(null)
    /** +1 when pushing, -1 when popping; drives slide direction. */
    var direction by mutableStateOf(1)
        private set

    val current: Route get() = stack.last().route
    val canGoBack: Boolean get() = stack.size > 1

    fun go(route: Route) { direction = 1; stack.add(Entry(nextId++, route)) }

    fun back() {
        if (sheet != null) { sheet = null; return }
        if (stack.size > 1) { direction = -1; stack.removeAt(stack.lastIndex) }
    }

    /** Clears the stack and starts again from [route] (used after sign-in and for tabs). */
    fun resetTo(route: Route, forward: Boolean = true) {
        direction = if (forward) 1 else -1
        stack.clear(); stack.add(Entry(nextId++, route))
    }

    fun open(s: Sheet) { sheet = s }
    fun closeSheet() { sheet = null }
    fun say(message: String) { toast = message }

    val tab: Tab
        get() = when (stack.first().route) {
            Route.Search -> Tab.Search
            Route.Library -> Tab.Library
            else -> Tab.Home
        }

    fun switchTab(t: Tab) {
        val root = when (t) { Tab.Home -> Route.Home; Tab.Search -> Route.Search; Tab.Library -> Route.Library }
        if (stack.size == 1 && current == root) return
        direction = 0
        stack.clear(); stack.add(Entry(nextId++, root))
    }
}

/** Screens that keep the fixed mini-player + bottom navigation (like Figma's fixed children). */
fun Route.hasChrome(): Boolean = when (this) {
    Route.Home, Route.Search, Route.Library, Route.SeeAllMadeForYou, Route.Updates, Route.History,
    is Route.SearchResults, is Route.Genre, is Route.ArtistPage, is Route.AlbumPage, is Route.PlaylistPage,
    Route.LikedSongs, Route.Folder, Route.Downloads, Route.MyProfile, is Route.OtherProfile,
    Route.SharedMix, Route.Messages, Route.Settings, Route.Help, Route.PrivacyPolicy -> true
    else -> false
}
