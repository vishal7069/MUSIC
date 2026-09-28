package com.dafli.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.GroupAdd
import androidx.compose.material.icons.rounded.NewReleases
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.automirrored.rounded.PlaylistAdd
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dafli.app.data.Art
import com.dafli.app.data.Sample
import com.dafli.app.data.Track
import com.dafli.app.nav.Route
import com.dafli.app.theme.DColor
import com.dafli.app.theme.DType
import com.dafli.app.ui.components.ArtImage
import com.dafli.app.ui.components.Avatar
import com.dafli.app.ui.components.ChipRow
import com.dafli.app.ui.components.CollectionCard
import com.dafli.app.ui.components.CoverCard
import com.dafli.app.ui.components.GroupLabel
import com.dafli.app.ui.components.IconBtn
import com.dafli.app.ui.components.LibraryRow
import com.dafli.app.ui.components.LocalChromePad
import com.dafli.app.ui.components.LocalNav
import com.dafli.app.ui.components.LocalPlayer
import com.dafli.app.ui.components.PillButton
import com.dafli.app.ui.components.SecondaryButton
import com.dafli.app.ui.components.SectionHeader
import com.dafli.app.ui.components.T
import com.dafli.app.ui.components.TrackRow

private val Rail = PaddingValues(horizontal = 16.dp)

// ------------------------------------------------------------------ S07 Home

@Composable
fun HomeScreen() {
    val nav = LocalNav.current
    val player = LocalPlayer.current
    LazyColumn(
        Modifier.fillMaxSize().background(
            Brush.verticalGradient(0f to Color2.HomeGlow, 0.25f to DColor.Night, 1f to DColor.Night)
        ),
        contentPadding = PaddingValues(bottom = LocalChromePad.current + 16.dp),
    ) {
        item {
            Row(
                Modifier.statusBarsPadding().fillMaxWidth().padding(start = 16.dp, end = 4.dp, top = 8.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Avatar(Sample.me, 36.dp, Modifier.clip(CircleShape).clickable { nav.go(Route.MyProfile) })
                Spacer(Modifier.width(12.dp))
                T("Good evening", DType.H3, Modifier.weight(1f))
                Box {
                    IconBtn(Icons.Rounded.Notifications, "Updates, 1 new") { nav.go(Route.Updates) }
                    Box(Modifier.align(Alignment.TopEnd).padding(top = 12.dp, end = 12.dp).size(8.dp).clip(CircleShape).background(DColor.Info))
                }
            }
        }
        // Jump back in
        item {
            val c = Sample.localTrainNights
            Box(
                Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(210.dp).clip(RoundedCornerShape(20.dp))
                    .clickable { nav.go(Route.PlaylistPage(c)) },
            ) {
                ArtImage(c.art, Modifier.fillMaxSize(), RoundedCornerShape(20.dp))
                Box(Modifier.fillMaxSize().background(Brush.verticalGradient(0f to DColor.Night.copy(alpha = 0.25f), 0.45f to DColor.Night.copy(alpha = 0f), 1f to DColor.Night.copy(alpha = 0.9f))))
                T("JUMP BACK IN", DType.Tiny.copy(fontWeight = FontWeight.SemiBold), Modifier.padding(16.dp), color = DColor.Text)
                Column(Modifier.align(Alignment.BottomStart).padding(16.dp).padding(end = 72.dp)) {
                    T(c.title, DType.Title.copy(fontSize = 22.sp, lineHeight = 28.sp))
                    T(c.subtitle, DType.Meta, color = DColor.TextDim)
                    Spacer(Modifier.height(10.dp))
                    Box(Modifier.width(240.dp).height(3.dp).clip(RoundedCornerShape(2.dp)).background(DColor.Text.copy(alpha = 0.25f))) {
                        Box(Modifier.fillMaxWidth(0.34f).height(3.dp).background(DColor.Marigold))
                    }
                }
                val playingThis = player.playingFrom == c.title && player.isPlaying
                Box(
                    Modifier.align(Alignment.BottomEnd).padding(16.dp).size(52.dp).clip(CircleShape).background(DColor.Marigold)
                        .clickable { if (playingThis) player.toggle() else player.play(Sample.localTrain, c.title) },
                    contentAlignment = Alignment.Center,
                ) { Icon(if (playingThis) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, "Play ${c.title}", tint = DColor.Ink, modifier = Modifier.size(30.dp)) }
            }
        }
        item { Column { Spacer(Modifier.height(28.dp)); SectionHeader("Recently played") { nav.go(Route.History) } } }
        item {
            LazyRow(contentPadding = Rail, horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 12.dp)) {
                items(Sample.recentlyPlayed) { c -> CollectionCard(c, 120.dp) { nav.go(Route.AlbumPage(c)) } }
            }
        }
        item { Column { Spacer(Modifier.height(28.dp)); SectionHeader("Made for you", sub = "Mixes built from what you play") { nav.go(Route.SeeAllMadeForYou) } } }
        item {
            LazyRow(contentPadding = Rail, horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 12.dp)) {
                items(Sample.madeForYou) { c -> CollectionCard(c, 160.dp) { nav.go(Route.PlaylistPage(c)) } }
            }
        }
        item { Column { Spacer(Modifier.height(28.dp)); SectionHeader("Artists you might like") { nav.go(Route.ArtistPage()) } } }
        item {
            LazyRow(contentPadding = Rail, horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 12.dp)) {
                items(Sample.artists) { a -> CoverCard(a.art, a.name, "Artist", 108.dp, circle = true) { nav.go(Route.ArtistPage(a)) } }
            }
        }
        item { Column { Spacer(Modifier.height(28.dp)); SectionHeader("New releases") { nav.go(Route.Updates) } } }
        item { Spacer(Modifier.height(4.dp)) }
        item { TrackRow(Sample.bassWala, subtitle = "DJ Rudra · Single") }
        item { TrackRow(Sample.noor, subtitle = "Sufi Circle · Album") }
        item { TrackRow(Sample.paadalNights, subtitle = "Nila Raghavan · EP") }
        item { Column { Spacer(Modifier.height(24.dp)); SectionHeader("Browse by mood") { nav.switchTab(com.dafli.app.nav.Tab.Search) } } }
        item {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Sample.moods.chunked(2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        row.forEach { m -> MoodTile(m.name, m.sub, m.art, Modifier.weight(1f)) { nav.go(Route.Genre(m.name)) } }
                    }
                }
            }
        }
    }
}

private object Color2 { val HomeGlow = androidx.compose.ui.graphics.Color(0xFF2A1F0C) }

@Composable
fun MoodTile(title: String, sub: String?, art: Art, modifier: Modifier = Modifier, height: Int = 80, onClick: () -> Unit) {
    Box(modifier.height(height.dp).clip(RoundedCornerShape(12.dp)).clickable(onClick = onClick)) {
        ArtImage(art, Modifier.fillMaxSize(), RoundedCornerShape(12.dp))
        Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(DColor.Night.copy(alpha = 0.85f), DColor.Night.copy(alpha = 0.1f)))))
        Column(Modifier.align(Alignment.BottomStart).padding(14.dp)) {
            T(title, DType.TitleS.copy(fontWeight = FontWeight.Bold))
            if (sub != null) T(sub, DType.Small, color = DColor.TextDim, maxLines = 1)
        }
    }
}

// ------------------------------------------------------------------ S08 See all · Made for you

@Composable
fun SeeAllScreen() {
    val nav = LocalNav.current
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val items = when (tab) {
        1 -> Sample.madeForYouAll.take(2)
        2 -> Sample.madeForYouAll.filter { it.title in listOf("Morning Calm", "Rainy Day Lo-fi", "Evening Bhajans") }
        3 -> Sample.madeForYouAll.filter { it.title in listOf("Desi Bass Mix", "Night Drive", "Your 90s Mix") }
        else -> Sample.madeForYouAll
    }
    ListScreen("Made for you") {
        item { T("Mixes built from what you play. They refresh every day.", DType.Meta.copy(fontSize = DType.Label.fontSize), Modifier.padding(horizontal = 16.dp), color = DColor.TextDim) }
        item { Column { Spacer(Modifier.height(16.dp)); ChipRow(listOf("All", "Daily mixes", "Moods", "Discover"), tab) { tab = it } } }
        item { Spacer(Modifier.height(20.dp)) }
        items.chunked(2).forEach { row ->
            item {
                Row(Modifier.padding(horizontal = 16.dp).padding(bottom = 20.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    row.forEach { c ->
                        Column(Modifier.weight(1f).clip(RoundedCornerShape(10.dp)).clickable { nav.go(Route.PlaylistPage(c)) }) {
                            ArtImage(c.art, Modifier.fillMaxWidth().height(171.dp), RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(8.dp))
                            T(c.title, DType.Label, maxLines = 1)
                            T(c.subtitle, DType.Small, color = DColor.TextDim, maxLines = 1)
                        }
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

// ------------------------------------------------------------------ S09 Updates

private data class Update(val icon: ImageVector?, val art: Art?, val title: String, val meta: String, val kind: Int, val invite: Boolean = false)

@Composable
fun UpdatesScreen() {
    val nav = LocalNav.current
    var tab by rememberSaveable { mutableIntStateOf(0) }
    var inviteState by rememberSaveable { mutableStateOf(0) } // 0 pending, 1 accepted, 2 declined
    val today = listOf(
        Update(null, Art.A, "Kabir Sen released a new single", "Marigold Hours · 2 h ago", 1),
        Update(Icons.Rounded.GroupAdd, null, "Aisha invited you to edit “Road Trip 2026”", "Collaborative playlist · 5 h ago", 2, invite = true),
    )
    val week = listOf(
        Update(null, Art.K, "Nila Raghavan released an EP", "Paadal Nights · Tue", 1),
        Update(Icons.Rounded.Download, null, "Downloads now use Wi-Fi only", "Change it in Settings · Mon", 0),
    )
    val earlier = listOf(
        Update(null, Art.B, "New album from Nila Raghavan", "Studio Nights · 21 Sep", 1),
        Update(Icons.AutoMirrored.Rounded.PlaylistAdd, null, "Rooftop Radio is on a new playlist", "Indie Nights · 18 Sep", 1),
    )
    fun keep(u: Update) = tab == 0 || (tab == 1 && u.kind == 1) || (tab == 2 && u.kind == 2)

    ListScreen("Updates") {
        item { ChipRow(listOf("All", "New releases", "Invites"), tab) { tab = it } }
        listOf("Today" to today, "This week" to week, "Earlier" to earlier).forEach { (label, list) ->
            val shown = list.filter(::keep)
            if (shown.isNotEmpty()) {
                item { GroupLabel(label) }
                shown.forEach { u ->
                    item {
                        Row(
                            Modifier.fillMaxWidth().clickable {
                                when {
                                    u.invite -> nav.go(Route.PlaylistPage(Sample.roadTrip))
                                    u.kind == 0 -> nav.go(Route.AudioStorage)
                                    else -> nav.go(Route.AlbumPage())
                                }
                            }.padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.Top,
                        ) {
                            if (u.art != null) ArtImage(u.art, Modifier.size(48.dp), RoundedCornerShape(6.dp))
                            else Box(Modifier.size(48.dp).clip(RoundedCornerShape(6.dp)).background(DColor.Elevated), contentAlignment = Alignment.Center) {
                                Icon(u.icon ?: Icons.Rounded.NewReleases, null, tint = DColor.Text, modifier = Modifier.size(22.dp))
                            }
                            Spacer(Modifier.width(16.dp))
                            Column(Modifier.weight(1f)) {
                                T(u.title, DType.BodyStrong)
                                T(u.meta, DType.Meta, color = DColor.TextDim, modifier = Modifier.padding(top = 2.dp))
                                if (u.invite) {
                                    Spacer(Modifier.height(10.dp))
                                    when (inviteState) {
                                        0 -> Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            PillButton("Accept", filled = true) { inviteState = 1; nav.say("You can now edit Road Trip 2026") }
                                            PillButton("Decline") { inviteState = 2 }
                                        }
                                        1 -> T("Accepted · you’re an editor", DType.Meta, color = DColor.Success)
                                        else -> T("Declined", DType.Meta, color = DColor.TextDim)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------------ S10 Recently played

@Composable
fun HistoryScreen() {
    val nav = LocalNav.current
    var tab by rememberSaveable { mutableIntStateOf(0) }
    var cleared by rememberSaveable { mutableStateOf(false) }
    val showSongs = tab == 0 || tab == 1
    val showArtists = tab == 0 || tab == 2
    val showPlaylists = tab == 0 || tab == 3
    val neonLive = Track("nbl", "Neon Bandra (Live)", "The Sea Link", Art.C, 260)

    ListScreen("Recently played") {
        item { ChipRow(listOf("All", "Songs", "Artists", "Playlists"), tab) { tab = it } }
        if (cleared) {
            item { T("Your listening history is empty.", DType.Body, Modifier.padding(32.dp), color = DColor.TextDim) }
            return@ListScreen
        }
        item { GroupLabel("Today") }
        if (showSongs) {
            item { TrackRow(Sample.noor, subtitle = "Sufi Circle · from Daily Mix 1") }
            item { TrackRow(Sample.marigoldHours, subtitle = "Kabir Sen · from Recently played") }
        }
        if (showArtists) item { LibraryRow("Kabir Sen", "Artist", Art.KabirSen, circle = true, onClick = { nav.go(Route.ArtistPage()) }) }
        item { GroupLabel("Yesterday") }
        if (showSongs) {
            item { TrackRow(Sample.bassWala, subtitle = "DJ Rudra · from Punjabi Heat") }
            item { TrackRow(Sample.chaiRain) }
            item { TrackRow(neonLive, subtitle = "Unavailable in your region", dim = true, onClick = { nav.say("This song isn’t available in your region") }) }
        }
        if (showPlaylists) item { LibraryRow("Punjabi Heat", "Playlist · Updated Friday", Art.Punjabi, onClick = { nav.go(Route.PlaylistPage()) }) }
        item { GroupLabel("Monday") }
        if (showSongs) {
            item { TrackRow(Sample.ghatKiSubah, subtitle = "Kabir Sen · from Morning Calm") }
            item { TrackRow(Sample.studioNights) }
        }
        if (showPlaylists) item { LibraryRow("Morning Calm", "Mix · Made for Vishal", Art.H, onClick = { nav.go(Route.PlaylistPage(Sample.morningCalm)) }) }
        item {
            SecondaryButton("Clear listening history", Modifier.padding(16.dp)) { cleared = true; nav.say("Listening history cleared") }
        }
    }
}
