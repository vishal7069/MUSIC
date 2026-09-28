package com.dafli.app.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ClosedCaption
import androidx.compose.material.icons.rounded.DragHandle
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.automirrored.rounded.QueueMusic
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.material.icons.rounded.Bedtime
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material.icons.rounded.Speaker
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dafli.app.AppConfig
import com.dafli.app.data.Art
import com.dafli.app.data.Sample
import com.dafli.app.data.asTime
import com.dafli.app.nav.Route
import com.dafli.app.nav.Sheet
import com.dafli.app.player.RepeatMode
import com.dafli.app.theme.DColor
import com.dafli.app.theme.DType
import com.dafli.app.ui.components.ArtImage
import com.dafli.app.ui.components.DSwitch
import com.dafli.app.ui.components.GroupLabel
import com.dafli.app.ui.components.IconBtn
import com.dafli.app.ui.components.LocalNav
import com.dafli.app.ui.components.LocalPlatform
import com.dafli.app.ui.components.LocalPlayer
import com.dafli.app.ui.components.PillButton
import com.dafli.app.ui.components.T
import com.dafli.app.ui.components.TextLink
import com.dafli.app.ui.components.TrackRow

/** Warm tint pulled from the current artwork (hand-picked per cover to keep it cheap). */
fun tintFor(art: Art): Color = when (art) {
    Art.J, Art.A, Art.Devotional, Art.H -> Color(0xFF5A4212)
    Art.C, Art.Indie, Art.G -> Color(0xFF3A2150)
    Art.I, Art.Punjabi, Art.D, Art.Workout -> Color(0xFF532018)
    Art.B, Art.K, Art.F -> Color(0xFF2C2A4A)
    Art.HipHop -> Color(0xFF203040)
    else -> Color(0xFF3A3020)
}

/** Thin seek bar with a round thumb (Figma style). Tap or drag to seek; exposed to TalkBack as a slider. */
@Composable
fun SeekBar(progress: Float, onSeek: (Float) -> Unit, modifier: Modifier = Modifier) {
    var width by remember { mutableIntStateOf(1) }
    val seek by rememberUpdatedState(onSeek)
    Box(
        modifier.fillMaxWidth().height(28.dp).onSizeChanged { width = it.width.coerceAtLeast(1) }
            .pointerInput(Unit) { detectTapGestures { seek((it.x / width).coerceIn(0f, 1f)) } }
            .pointerInput(Unit) { detectHorizontalDragGestures { change, _ -> seek((change.position.x / width).coerceIn(0f, 1f)) } }
            .semantics { progressBarRangeInfo = ProgressBarRangeInfo(progress, 0f..1f); setProgress { seek(it); true } },
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)).background(DColor.Text.copy(alpha = 0.2f)))
        Box(Modifier.fillMaxWidth(progress.coerceIn(0f, 1f)).height(4.dp).clip(RoundedCornerShape(2.dp)).background(DColor.Marigold))
        Box(
            Modifier.offset { IntOffset((progress.coerceIn(0f, 1f) * width - 7.dp.toPx()).roundToInt(), 0) }
                .size(14.dp).clip(CircleShape).background(DColor.Text)
        )
    }
}

// ------------------------------------------------------------------ S19 Now Playing

@Composable
fun NowPlayingScreen() {
    val nav = LocalNav.current
    val p = LocalPlayer.current
    val t = p.current
    val tint by animateColorAsState(tintFor(t.art), label = "tint")
    val liked = p.isLiked(t)

    Column(
        Modifier.fillMaxSize().background(Brush.verticalGradient(0f to tint, 0.55f to DColor.Night, 1f to DColor.Night))
            .statusBarsPadding().verticalScroll(rememberScrollState()).navigationBarsPadding(),
    ) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            IconBtn(Icons.Rounded.KeyboardArrowDown, "Close player", size = 28.dp) { nav.back() }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                T("PLAYING FROM", DType.Tiny.copy(fontWeight = FontWeight.SemiBold, letterSpacing = 0.8.sp), color = DColor.TextDim)
                T(p.playingFrom.ifBlank { AppConfig.MUSIC_SOURCE }, DType.Label, maxLines = 1)
            }
            IconBtn(Icons.Rounded.MoreVert, "More options") { nav.open(Sheet.TrackMenu(t)) }
        }
        Box(Modifier.padding(horizontal = 24.dp, vertical = 16.dp).fillMaxWidth().aspectRatio(1f)) {
            ArtImage(t, Modifier.fillMaxSize(), RoundedCornerShape(12.dp))
        }
        Row(Modifier.padding(start = 24.dp, end = 12.dp, top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                T(t.title, DType.H2, maxLines = 1)
                T(t.artist, DType.BodyL.copy(fontWeight = FontWeight.Normal), color = DColor.TextDim, maxLines = 1,
                    modifier = Modifier.clickable {
                        if (t.artistId != null) nav.go(Route.ArtistPage(com.dafli.app.data.Artist(t.artist, t.art, id = t.artistId, artUrl = null)))
                    })
            }
            IconBtn(if (liked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder, if (liked) "Remove from Liked Songs" else "Add to Liked Songs",
                tint = if (liked) DColor.Marigold else DColor.Text, size = 28.dp) {
                p.toggleLike(t); nav.say(if (p.isLiked(t)) "Added to Liked Songs" else "Removed from Liked Songs")
            }
        }
        SeekBar(p.progress, { p.seekTo(it) }, Modifier.padding(horizontal = 24.dp).padding(top = 12.dp))
        Row(Modifier.padding(horizontal = 24.dp)) {
            T(p.positionSec.asTime(), DType.Small.copy(fontWeight = FontWeight.Medium), color = DColor.TextDim)
            Spacer(Modifier.weight(1f))
            T(p.durationSec.asTime(), DType.Small.copy(fontWeight = FontWeight.Medium), color = DColor.TextDim)
        }
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically,
        ) {
            IconBtn(Icons.Rounded.Shuffle, if (p.shuffle) "Shuffle on" else "Shuffle off", tint = if (p.shuffle) DColor.Marigold else DColor.TextDim) { p.shuffle = !p.shuffle }
            IconBtn(Icons.Rounded.SkipPrevious, "Previous", size = 36.dp) { p.previous() }
            Box(
                Modifier.size(76.dp).clip(CircleShape).background(DColor.Marigold).clickable { p.toggle() },
                contentAlignment = Alignment.Center,
            ) {
                if (p.isBuffering) CircularProgressIndicator(Modifier.size(36.dp), color = DColor.Ink, strokeWidth = 3.dp)
                else Icon(if (p.isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, if (p.isPlaying) "Pause" else "Play", tint = DColor.Ink, modifier = Modifier.size(40.dp))
            }
            IconBtn(Icons.Rounded.SkipNext, "Next", size = 36.dp) { p.next() }
            IconBtn(
                if (p.repeat == RepeatMode.One) Icons.Rounded.RepeatOne else Icons.Rounded.Repeat, "Repeat ${p.repeat.name}",
                tint = if (p.repeat == RepeatMode.Off) DColor.TextDim else DColor.Marigold,
            ) { p.cycleRepeat() }
        }
        Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Spacer(Modifier.weight(1f))
            IconBtn(Icons.Rounded.Bedtime, if (p.sleepLeft != null) "Sleep timer on" else "Sleep timer", tint = if (p.sleepLeft != null) DColor.Marigold else DColor.TextDim) { nav.open(Sheet.SleepTimer) }
            val platform = LocalPlatform.current
            if (t.shareUrl != null) IconBtn(Icons.Rounded.Share, "Share", tint = DColor.TextDim) { platform.share(t.title, "${t.title} by ${t.artist} — ${t.shareUrl}") }
            IconBtn(Icons.AutoMirrored.Rounded.QueueMusic, "Queue", tint = DColor.TextDim) { nav.go(Route.Queue) }
        }
        p.sleepLeft?.let {
            T(if (it == -1) "Pausing at the end of this song" else "Pausing in ${it.asTime()}", DType.Meta.copy(fontWeight = FontWeight.Medium), Modifier.padding(horizontal = 24.dp), color = DColor.Marigold)
        }
        // About this song + required attribution to the music source
        Column(
            Modifier.padding(16.dp).padding(top = 8.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(DColor.Surface).padding(16.dp),
        ) {
            T("About this song", DType.BodyStrong.copy(fontWeight = FontWeight.Bold))
            Spacer(Modifier.height(10.dp))
            T(listOfNotNull("Artist: ${t.artist}", t.genre?.let { "Genre: $it" }).joinToString("\n"), DType.Meta.copy(lineHeight = 22.sp), color = DColor.TextDim)
            if (t.streamUrl != null) {
                Spacer(Modifier.height(10.dp))
                val platform = LocalPlatform.current
                T("Streaming from ${AppConfig.MUSIC_SOURCE}", DType.Small, color = DColor.TextDim)
                t.shareUrl?.let { url -> TextLink("Open on ${AppConfig.MUSIC_SOURCE}", Modifier.padding(top = 4.dp)) { platform.openUrl(url) } }
            }
        }
        Spacer(Modifier.height(32.dp))
    }
}

// ------------------------------------------------------------------ S20 Queue

@Composable
fun QueueScreen() {
    val p = LocalPlayer.current
    ListScreen("Queue", centerTitle = true) {
        item { GroupLabel("Now playing") }
        item { TrackRow(p.current, trailing = { Icon(Icons.Rounded.DragHandle, null, tint = DColor.Disabled, modifier = Modifier.padding(12.dp)) }) }
        if (p.userQueue.isNotEmpty()) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    GroupLabel("Next in queue", Modifier.weight(1f))
                    TextLink("Clear", Modifier.padding(end = 12.dp, top = 12.dp)) { p.userQueue.clear() }
                }
            }
            itemsIndexed(p.userQueue, key = { i, t -> "u$i${t.id}" }) { _, t ->
                TrackRow(t, onClick = { p.userQueue.remove(t); p.play(t) }, trailing = {
                    IconBtn(Icons.Rounded.Close, "Remove ${t.title} from queue", tint = DColor.TextDim, size = 20.dp) { p.userQueue.remove(t) }
                })
            }
        }
        item { GroupLabel(if (p.playingFrom.isBlank()) "Next up" else "Next from: ${p.playingFrom}") }
        itemsIndexed(p.contextQueue, key = { i, t -> "c$i${t.id}" }) { _, t ->
            TrackRow(t, onClick = { p.contextQueue.remove(t); p.play(t) }, trailing = {
                Icon(Icons.Rounded.DragHandle, "Reorder", tint = DColor.TextDim, modifier = Modifier.padding(12.dp))
            })
        }
        item {
            Row(Modifier.padding(16.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(DColor.Surface).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    T("Autoplay similar songs", DType.BodyStrong)
                    T("Keeps playing similar songs when this list ends.", DType.Small, color = DColor.TextDim)
                }
                val lib = com.dafli.app.ui.components.LocalLib.current
                DSwitch(lib.autoplay) { lib.setAutoplayPref(it) }
            }
        }
    }
}

// ------------------------------------------------------------------ S21 Lyrics

@Composable
fun LyricsScreen() {
    val nav = LocalNav.current
    val p = LocalPlayer.current
    val t = p.current
    val lines = Sample.lyrics
    val perLine = t.durationSec / (lines.size + 1)
    val active = (p.positionSec / perLine.coerceAtLeast(1)).coerceIn(0, lines.lastIndex)
    val list = rememberLazyListState()
    LaunchedEffect(active) { list.animateScrollToItem((active - 1).coerceAtLeast(0)) }

    Column(Modifier.fillMaxSize().background(tintFor(t.art)).statusBarsPadding().navigationBarsPadding()) {
        Row(Modifier.padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            IconBtn(Icons.Rounded.KeyboardArrowDown, "Close lyrics", size = 28.dp) { nav.back() }
            Column(Modifier.weight(1f)) {
                T(t.title, DType.BodyStrong)
                T(t.artist, DType.Small, color = DColor.TextDim)
            }
            IconBtn(Icons.Rounded.Share, "Share lyrics") { nav.open(Sheet.Share(t.title, "Lyrics · ${t.artist}", t.art)) }
        }
        LazyColumn(Modifier.weight(1f), state = list, contentPadding = PaddingValues(24.dp)) {
            itemsIndexed(lines) { i, line ->
                T(
                    line, DType.H1.copy(lineHeight = 36.sp),
                    Modifier.padding(vertical = 10.dp).clickable { p.positionSec = i * perLine },
                    color = when { i == active -> DColor.Text; i < active -> DColor.Text.copy(alpha = 0.75f); else -> DColor.Night.copy(alpha = 0.55f) },
                )
            }
            item { T("Lyrics provided by a licensed partner (sample)  ·  Report a problem", DType.Small, Modifier.padding(top = 24.dp), color = DColor.TextDim) }
        }
        SeekBar(p.progress, { p.seekTo(it) }, Modifier.padding(horizontal = 24.dp))
        Row(Modifier.padding(horizontal = 24.dp), verticalAlignment = Alignment.CenterVertically) {
            T(p.positionSec.asTime(), DType.Tiny, color = DColor.TextDim)
            Spacer(Modifier.weight(1f))
            IconBtn(if (p.isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, if (p.isPlaying) "Pause" else "Play", size = 36.dp) { p.toggle() }
            Spacer(Modifier.weight(1f))
            T(t.durationSec.asTime(), DType.Tiny, color = DColor.TextDim)
        }
    }
}

// ------------------------------------------------------------------ S52 Music video

@Composable
fun MusicVideoScreen() {
    val nav = LocalNav.current
    val p = LocalPlayer.current
    val t = p.current
    var cc by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().background(Color.Black).statusBarsPadding().navigationBarsPadding()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            IconBtn(Icons.Rounded.KeyboardArrowDown, "Close video", size = 28.dp) { nav.back() }
            Spacer(Modifier.weight(1f))
            Row(Modifier.clip(RoundedCornerShape(18.dp)).background(DColor.Elevated).padding(3.dp)) {
                Box(Modifier.clip(RoundedCornerShape(16.dp)).clickable { nav.back() }.padding(horizontal = 16.dp, vertical = 6.dp)) { T("Audio", DType.Meta.copy(fontWeight = FontWeight.SemiBold), color = DColor.TextDim) }
                Box(Modifier.clip(RoundedCornerShape(16.dp)).background(DColor.Text).padding(horizontal = 16.dp, vertical = 6.dp)) { T("Video", DType.Meta.copy(fontWeight = FontWeight.SemiBold), color = DColor.Night) }
            }
            Spacer(Modifier.weight(1f))
            IconBtn(Icons.Rounded.MoreVert, "More") { nav.open(Sheet.TrackMenu(t)) }
        }
        Spacer(Modifier.weight(0.6f))
        Box(Modifier.fillMaxWidth().aspectRatio(16f / 9f).clickable { p.toggle() }) {
            ArtImage(t.art, Modifier.fillMaxSize(), RoundedCornerShape(0.dp))
            if (!p.isPlaying) Box(Modifier.align(Alignment.Center).size(64.dp).clip(CircleShape).background(DColor.Night.copy(alpha = 0.6f)), contentAlignment = Alignment.Center) {
                Icon(Icons.Rounded.PlayArrow, "Play", tint = DColor.Text, modifier = Modifier.size(36.dp))
            }
            if (cc) T("Raat ki roshni mein", DType.BodyStrong, Modifier.align(Alignment.BottomCenter).padding(12.dp).background(Color.Black.copy(alpha = 0.6f)).padding(horizontal = 8.dp, vertical = 2.dp))
            Box(
                Modifier.align(Alignment.TopEnd).padding(12.dp).clip(RoundedCornerShape(4.dp)).background(if (cc) DColor.Text else Color.Black.copy(alpha = 0.5f))
                    .clickable { cc = !cc }.padding(horizontal = 6.dp, vertical = 2.dp),
            ) { Icon(Icons.Rounded.ClosedCaption, "Captions", tint = if (cc) DColor.Night else DColor.Text, modifier = Modifier.size(20.dp)) }
        }
        Spacer(Modifier.weight(0.4f))
        Column(Modifier.padding(horizontal = 24.dp)) {
            T(t.title, DType.H2)
            T("${t.artist} · Official video", DType.Body, color = DColor.TextDim)
        }
        SeekBar(p.progress, { p.seekTo(it) }, Modifier.padding(horizontal = 24.dp, vertical = 8.dp))
        Row(Modifier.padding(horizontal = 24.dp)) {
            T(p.positionSec.asTime(), DType.Small, color = DColor.TextDim); Spacer(Modifier.weight(1f)); T(t.durationSec.asTime(), DType.Small, color = DColor.TextDim)
        }
        Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            IconBtn(Icons.Rounded.SkipPrevious, "Previous", size = 34.dp) { p.previous() }
            Box(Modifier.size(64.dp).clip(CircleShape).background(DColor.Marigold).clickable { p.toggle() }, contentAlignment = Alignment.Center) {
                Icon(if (p.isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, null, tint = DColor.Ink, modifier = Modifier.size(34.dp))
            }
            IconBtn(Icons.Rounded.SkipNext, "Next", size = 34.dp) { p.next() }
        }
        T(
            "Video uses more data. On mobile data we switch to audio unless you allow video in Settings.", DType.Small,
            Modifier.padding(horizontal = 40.dp, vertical = 16.dp), color = DColor.TextDim,
        )
    }
}

