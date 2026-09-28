package com.dafli.app.ui.screens

import androidx.compose.runtime.Composable
import com.dafli.app.data.Genres
import com.dafli.app.nav.Route
import com.dafli.app.ui.brand.SplashScreen
import com.dafli.app.ui.components.LocalNav

/** Maps every route to its screen. */
@Composable
fun ScreenFor(route: Route) {
    val nav = LocalNav.current
    when (route) {
        Route.Splash -> {
            val lib = com.dafli.app.ui.components.LocalLib.current
            SplashScreen { if (nav.current == Route.Splash) nav.resetTo(if (lib.onboarded) Route.Home else Route.Welcome) }
        }
        Route.Welcome -> WelcomeLive()
        Route.SignUp -> SignUpScreen()
        Route.LogIn -> LogInScreen()
        Route.ResetPassword -> ResetPasswordScreen()
        Route.Preferences -> PreferencesLive(editing = false)

        Route.Home -> HomeLive()
        Route.Search -> SearchLive()
        Route.Library -> LibraryLive()

        Route.SeeAllMadeForYou -> SeeAllScreen()
        Route.Updates -> UpdatesScreen()
        Route.History -> HistoryLive()

        Route.SearchTyping -> SearchTypingLive()
        is Route.SearchResults -> SearchResultsLive(route.query)
        is Route.Genre -> if (Genres.any { it.name == route.name }) GenreLive(route.name) else GenreScreen(route.name)
        Route.VoiceSearch -> VoiceSearchScreen()

        is Route.ArtistPage -> if (route.artist?.id != null) ArtistLive(route.artist) else ArtistScreen(route.artist)
        is Route.AlbumPage -> AlbumScreen(route.album)
        is Route.PlaylistPage -> if (route.playlist != null && (route.playlist.remoteId != null || route.playlist.id == "trending" || route.playlist.genre != null)) PlaylistLive(route.playlist) else PlaylistScreen(route.playlist)

        Route.NowPlaying -> NowPlayingScreen()
        Route.Queue -> QueueScreen()
        Route.Lyrics -> LyricsScreen()
        Route.MusicVideo -> MusicVideoScreen()

        Route.LikedSongs -> LikedLive()
        Route.CreatePlaylist -> CreatePlaylistScreen()
        Route.EditPlaylist -> EditPlaylistScreen()
        Route.Collaborators -> CollaboratorsScreen()
        Route.Folder -> FolderScreen()
        Route.Downloads -> DownloadsScreen()
        Route.LocalFiles -> LocalFilesScreen()

        Route.MyProfile -> ProfileLive()
        is Route.OtherProfile -> OtherProfileScreen(route.person)
        Route.Settings -> SettingsLive()
        Route.Playback -> PlaybackScreen()
        Route.AudioStorage -> AudioStorageScreen()
        Route.Privacy -> PrivacyScreen()
        Route.Account -> AccountScreen()
        Route.Help -> HelpLive()

        Route.ListenTogether -> ListenTogetherScreen()
        Route.SharedMix -> SharedMixScreen()
        Route.Recap -> RecapScreen()
        Route.CreateWithAi -> CreateWithAiScreen()
        Route.Messages -> MessagesScreen()
        is Route.Chat -> ChatScreen(route.person)
        Route.YourTaste -> YourTasteScreen()
        Route.PrivacyPolicy -> PrivacyPolicyScreen()
        Route.EditTaste -> PreferencesLive(editing = true)
    }
}
