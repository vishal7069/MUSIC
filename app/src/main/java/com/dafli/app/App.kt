package com.dafli.app

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.dafli.app.data.LocalLibrary
import com.dafli.app.data.MusicRepository
import com.dafli.app.data.SampleRepository
import com.dafli.app.nav.Entry
import com.dafli.app.nav.Navigator
import com.dafli.app.nav.Route
import com.dafli.app.nav.Sheet
import com.dafli.app.nav.hasChrome
import com.dafli.app.platform.Platform
import com.dafli.app.platform.PlatformBackHandler
import com.dafli.app.platform.PreviewPlatform
import com.dafli.app.player.Player
import com.dafli.app.theme.DColor
import com.dafli.app.theme.DType
import com.dafli.app.ui.components.BottomNav
import com.dafli.app.ui.components.LocalChromePad
import com.dafli.app.ui.components.LocalLib
import com.dafli.app.ui.components.LocalPlatform
import com.dafli.app.ui.components.LocalRepo
import com.dafli.app.ui.components.LocalNav
import com.dafli.app.ui.components.LocalPlayer
import com.dafli.app.ui.components.MiniPlayer
import com.dafli.app.ui.components.T
import com.dafli.app.ui.screens.ScreenFor
import com.dafli.app.ui.screens.SheetContent
import kotlinx.coroutines.delay

/** App shell: back stack, fixed mini-player + bottom nav, sheets and toasts. */
@Composable
fun DafliApp(
    platform: Platform = PreviewPlatform(),
    repo: MusicRepository = SampleRepository(),
    start: Route = Route.Splash,
    startSheet: Sheet? = null,
    demoPlaying: Boolean = false,
) {
    val nav = remember { Navigator(start).also { it.sheet = startSheet } }
    val library = remember { LocalLibrary(platform.store) }
    val player = remember { Player(library).also { if (demoPlaying) it.demo() } }
    DisposableEffect(player) {
        player.engine = platform.createEngine(player)
        onDispose { player.engine?.release(); player.engine = null }
    }
    LaunchedEffect(player) { while (true) { delay(500); player.tick(if (player.engine != null) 0 else 1) } }
    // Autoplay: when the list runs out, keep going with similar trending songs.
    LaunchedEffect(player.hasTrack, player.contextQueue.size, player.userQueue.size, library.autoplay, player.current.id) {
        if (library.autoplay && player.hasTrack && player.current.streamUrl != null && player.contextQueue.isEmpty() && player.userQueue.isEmpty()) {
            runCatching { repo.trending(player.current.genre, 20) }.getOrNull()
                ?.filter { t -> t.id != player.current.id && library.recent.none { it.id == t.id } }
                ?.take(10)?.let { player.contextQueue.addAll(it) }
        }
    }
    LaunchedEffect(player.error) { player.error?.let { nav.say(it) } }

    CompositionLocalProvider(
        LocalNav provides nav, LocalPlayer provides player, LocalRepo provides repo,
        LocalLib provides library, LocalPlatform provides platform,
    ) {
        val route = nav.current
        val chrome = route.hasChrome()
        val navBar = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        PlatformBackHandler(enabled = nav.canGoBack || nav.sheet != null) { nav.back() }

        Box(Modifier.fillMaxSize().background(DColor.Night)) {
            val holder = rememberSaveableStateHolder()
            AnimatedContent(
                targetState = nav.stack.last(),
                contentKey = { it.id },
                transitionSpec = { transitionFor(initialState, targetState, nav.direction) },
                label = "screens",
            ) { entry ->
                holder.SaveableStateProvider(entry.id) {
                    val pad = if (entry.route.hasChrome()) (if (player.hasTrack) 136.dp else 72.dp) + navBar else navBar
                    CompositionLocalProvider(LocalChromePad provides pad) { ScreenFor(entry.route) }
                }
            }

            if (chrome) {
                // Status bar stays readable while content scrolls underneath (fixed, like Figma).
                Box(
                    Modifier.fillMaxWidth().windowInsetsTopHeight(WindowInsets.statusBars)
                        .background(Brush.verticalGradient(listOf(DColor.Night.copy(alpha = 0.95f), DColor.Night.copy(alpha = 0.6f))))
                )
                Column(
                    Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                        .background(Brush.verticalGradient(0f to DColor.Night.copy(alpha = 0f), 0.35f to DColor.Night.copy(alpha = 0.9f), 1f to DColor.Night))
                        .padding(top = 12.dp),
                ) {
                    if (player.hasTrack) {
                        MiniPlayer()
                        Spacer(Modifier.height(4.dp))
                    }
                    BottomNav()
                }
            }

            SheetHost(nav)
            ToastHost(nav, bottom = if (chrome) 150.dp + navBar else 32.dp + navBar)
        }
    }
}

private fun transitionFor(from: Entry, to: Entry, direction: Int): ContentTransform {
    val d = 280
    return when {
        direction == 0 -> fadeIn(tween(160)) togetherWith fadeOut(tween(120))
        direction > 0 && (to.route == Route.NowPlaying || to.route == Route.VoiceSearch) ->
            (slideInVertically(tween(d)) { it } + fadeIn(tween(d))) togetherWith fadeOut(tween(d))
        direction < 0 && (from.route == Route.NowPlaying || from.route == Route.VoiceSearch) ->
            fadeIn(tween(d)) togetherWith (slideOutVertically(tween(d)) { it } + fadeOut(tween(d)))
        from.route == Route.Splash -> fadeIn(tween(600)) togetherWith fadeOut(tween(600))
        direction > 0 -> (slideInHorizontally(tween(d)) { it / 3 } + fadeIn(tween(d))) togetherWith
            (slideOutHorizontally(tween(d)) { -it / 6 } + fadeOut(tween(d)))
        else -> (slideInHorizontally(tween(d)) { -it / 6 } + fadeIn(tween(d))) togetherWith
            (slideOutHorizontally(tween(d)) { it / 3 } + fadeOut(tween(d)))
    }
}

@Composable
private fun SheetHost(nav: Navigator) {
    var last by remember { mutableStateOf<Sheet?>(null) }
    nav.sheet?.let { last = it }
    val open = nav.sheet != null
    Box(Modifier.fillMaxSize()) {
        AnimatedVisibility(open, enter = fadeIn(tween(200)), exit = fadeOut(tween(200))) {
            Box(
                Modifier.fillMaxSize().background(DColor.Scrim)
                    .clickable(remember { MutableInteractionSource() }, indication = null) { nav.closeSheet() }
            )
        }
        AnimatedVisibility(
            open, modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(tween(280)) { it }, exit = slideOutVertically(tween(220)) { it },
        ) {
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)).background(DColor.Elevated)
                    .clickable(remember { MutableInteractionSource() }, indication = null) {}
                    .padding(WindowInsets.navigationBars.asPaddingValues()),
            ) {
                Box(Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 4.dp), contentAlignment = Alignment.Center) {
                    Box(Modifier.clip(RoundedCornerShape(3.dp)).background(DColor.Line).height(5.dp).fillMaxWidth(0.1f))
                }
                last?.let { SheetContent(it) }
            }
        }
    }
}

@Composable
private fun ToastHost(nav: Navigator, bottom: androidx.compose.ui.unit.Dp) {
    val msg = nav.toast
    LaunchedEffect(msg) { if (msg != null) { delay(2200); nav.toast = null } }
    Box(Modifier.fillMaxSize().padding(bottom = bottom, start = 24.dp, end = 24.dp), contentAlignment = Alignment.BottomCenter) {
        AnimatedVisibility(msg != null, enter = fadeIn() + slideInVertically { it / 2 }, exit = fadeOut()) {
            var shown by remember { mutableStateOf("") }
            msg?.let { shown = it }
            Box(Modifier.clip(RoundedCornerShape(12.dp)).background(DColor.Text).padding(horizontal = 16.dp, vertical = 12.dp)) {
                T(shown, DType.Label, color = DColor.Night)
            }
        }
    }
}
