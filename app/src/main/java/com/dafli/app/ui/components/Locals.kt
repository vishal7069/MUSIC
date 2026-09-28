package com.dafli.app.ui.components

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dafli.app.data.LocalLibrary
import com.dafli.app.data.MusicRepository
import com.dafli.app.nav.Navigator
import com.dafli.app.platform.Platform
import com.dafli.app.player.Player

val LocalNav = staticCompositionLocalOf<Navigator> { error("Navigator not provided") }
val LocalPlayer = staticCompositionLocalOf<Player> { error("Player not provided") }

/** Space taken by the fixed mini-player + bottom nav, so scrolling content can clear it. */
val LocalChromePad = compositionLocalOf<Dp> { 0.dp }

val LocalRepo = staticCompositionLocalOf<MusicRepository> { error("Repository not provided") }
val LocalLib = staticCompositionLocalOf<LocalLibrary> { error("Library not provided") }
val LocalPlatform = staticCompositionLocalOf<Platform> { error("Platform not provided") }
