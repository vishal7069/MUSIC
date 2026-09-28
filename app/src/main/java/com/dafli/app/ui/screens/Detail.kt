package com.dafli.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DownloadForOffline
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PersonAddAlt
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Shuffle
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dafli.app.data.Art
import com.dafli.app.data.Artist
import com.dafli.app.data.Collection
import com.dafli.app.data.Kind
import com.dafli.app.data.Sample
import com.dafli.app.data.Track
import com.dafli.app.nav.Route
import com.dafli.app.nav.Sheet
import com.dafli.app.theme.DColor
import com.dafli.app.theme.DType
import com.dafli.app.ui.components.AddedByBadge
import com.dafli.app.ui.components.ArtImage
import com.dafli.app.ui.components.Avatar
import com.dafli.app.ui.components.ChipRow
import com.dafli.app.ui.components.CoverCard
import com.dafli.app.ui.components.IconBtn
import com.dafli.app.ui.components.LocalChromePad
import com.dafli.app.ui.components.LocalNav
import com.dafli.app.ui.components.LocalPlayer
import com.dafli.app.ui.components.PillButton
import com.dafli.app.ui.components.SectionHeader
import com.dafli.app.ui.components.T
import com.dafli.app.ui.components.TextLink
import com.dafli.app.ui.components.TrackRow

/** Big marigold play/pause for a list. Shows pause when this list is the one playing. */
@Composable
fun PlayFab(source: String, first: Track, size: Int = 56, list: List<Track>? = null) {
    val player = LocalPlayer.current
    val active = player.hasTrack && player.playingFrom == source && player.isPlaying
    Box(
        Modifier.size(size.dp).clip(CircleShape).background(DColor.Marigold)
            .clickable { if (player.hasTrack && player.playingFrom == source) player.toggle() else player.play(first, source, list) },
        contentAlignment = Alignment.Center,
    ) { Icon(if (active) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, if (active) "Pause $source" else "Play $source", tint = DColor.Ink, modifier = Modifier.size((size * 0.55f).dp)) }
}

@Composable
private fun ShuffleBtn(source: String, tracks: List<Track>) {
    val player = LocalPlayer.current
    IconBtn(Icons.Rounded.Shuffle, "Shuffle play", tint = if (player.shuffle && player.playingFrom == source) DColor.Marigold else DColor.TextDim) {
        player.shuffle = true; player.play(tracks.random(), source)
    }
}

/** Deterministic sample track list for any collection. */
fun tracksFor(id: String, count: Int = 8): List<Track> {
    val pool = Sample.allTracks
    val start = (id.hashCode() and 0x7fffffff) % pool.size
    return List(count) { pool[(start + it * 3) % pool.size] }.distinctBy { it.id }
}

@Composable
private fun HeaderScrim(height: Int, content: @Composable BoxScope.() -> Unit) {
    Box(Modifier.fillMaxWidth().height(height.dp)) { content() }
}

// ------------------------------------------------------------------ S15 Artist

@Composable
fun ArtistScreen(artist: Artist?) {
    val nav = LocalNav.current
    val a = artist ?: Sample.kabirSen
    val isKabir = a.name == "Kabir Sen"
    var following by rememberSaveable { mutableStateOf(false) }
    var disc by rememberSaveable { mutableIntStateOf(0) }
    var more by rememberSaveable { mutableStateOf(false) }
    val popular: List<Pair<Track, String>> = if (isKabir) listOf(
        Sample.marigoldHours to "Single", Sample.chaiRain to "with Ira Menon", Sample.localTrain to "Kabira Nights",
        Sample.ghatKiSubah to "Morning Calm", Sample.kabira to "Kabira Nights", Sample.dholRepublic.copy(title = "Dhoop", artist = "Kabir Sen", art = Art.A) to "Marigold Hours",
    ) else tracksFor(a.name, 6).map { it.copy(artist = a.name) to it.album }
    val shown = if (more) popular else popular.take(5)

    LazyColumn(Modifier.fillMaxSize().background(DColor.Night), contentPadding = PaddingValues(bottom = LocalChromePad.current + 16.dp)) {
        item {
            HeaderScrim(360) {
                ArtImage(a.art, Modifier.fillMaxSize(), RoundedCornerShape(0.dp))
                Box(Modifier.fillMaxSize().background(Brush.verticalGradient(0f to DColor.Night.copy(alpha = 0.35f), 0.4f to Color.Transparent, 1f to DColor.Night)))
                IconBtn(Icons.AutoMirrored.Rounded.ArrowBack, "Back", Modifier.statusBarsPadding().padding(4.dp)) { nav.back() }
                Column(Modifier.align(Alignment.BottomStart).padding(horizontal = 16.dp)) {
                    T(a.name, DType.Display)
                    T(if (isKabir) a.tagline else "Artist", DType.Label.copy(fontWeight = FontWeight.Normal), color = DColor.TextDim, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
        item {
            Row(Modifier.padding(horizontal = 16.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                PillButton(if (following) "Following" else "Follow", filled = following) {
                    following = !following; nav.say(if (following) "Following ${a.name}" else "Unfollowed ${a.name}")
                }
                IconBtn(Icons.Rounded.MoreVert, "More", tint = DColor.TextDim) { nav.open(Sheet.Share(a.name, "Artist", a.art)) }
                Spacer(Modifier.weight(1f))
                ShuffleBtn(a.name, popular.map { it.first })
                Spacer(Modifier.width(8.dp))
                PlayFab(a.name, popular.first().first)
            }
        }
        item { SectionHeader("Popular", action = null) }
        shown.forEachIndexed { i, (t, sub) -> item { TrackRow(t, index = i + 1, subtitle = sub) } }
        item { TextLink(if (more) "Show less" else "See more", Modifier.padding(horizontal = 12.dp, vertical = 4.dp), color = DColor.TextDim) { more = !more } }
        item { Column { Spacer(Modifier.height(24.dp)); SectionHeader("Discography") { disc = 1 } } }
        item { Column { Spacer(Modifier.height(12.dp)); ChipRow(listOf("Popular", "Albums", "Singles"), disc) { disc = it } } }
        item {
            val all = listOf(
                Collection("d1", "Marigold Hours", "2026 · Album", Art.A, Kind.Album),
                Collection("d2", "Kabira Nights", "2025 · EP", Art.KabirSen, Kind.EP),
                Collection("d3", "Local Train", "2024 · Single", Art.Hero, Kind.Single),
            )
            val list = when (disc) { 1 -> all.filter { it.kind != Kind.Single }; 2 -> all.filter { it.kind == Kind.Single }; else -> all }
            Rail(list) { nav.go(Route.AlbumPage(it)) }
        }
        item { Column { Spacer(Modifier.height(24.dp)); SectionHeader("About", action = null) } }
        item {
            var read by rememberSaveable { mutableStateOf(false) }
            Column(
                Modifier.padding(16.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(DColor.Surface).clickable { read = !read }.padding(16.dp),
            ) {
                T("12,40,318 monthly listeners", DType.BodyStrong)
                Spacer(Modifier.height(8.dp))
                T(
                    "${a.name} writes quiet Hindi songs about trains, chai and late nights in Mumbai. The 2026 album Marigold Hours was recorded in one week in a Bandra studio." +
                        if (read) " Most songs start as voice notes on the local train home, then grow into full arrangements with friends from the indie scene." else "",
                    DType.Label.copy(fontWeight = FontWeight.Normal), color = DColor.TextDim, maxLines = if (read) 20 else 3,
                )
                T(if (read) "Show less" else "Read more", DType.Label, Modifier.padding(top = 8.dp))
            }
        }
        item { Column { Spacer(Modifier.height(12.dp)); SectionHeader("Fans also like", action = null) } }
        item {
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 12.dp)) {
                items(listOf(Sample.iraMenon, Sample.nilaRaghavan, Sample.aravKapoor, Sample.rooftopRadio).filter { it.name != a.name }) { f ->
                    CoverCard(f.art, f.name, "Artist", 108.dp, circle = true) { nav.go(Route.ArtistPage(f)) }
                }
            }
        }
    }
}

// ------------------------------------------------------------------ S16 Album

@Composable
fun AlbumScreen(album: Collection?) {
    val nav = LocalNav.current
    val player = LocalPlayer.current
    val c = album ?: Sample.marigoldAlbum
    val isMarigold = c.title == "Marigold Hours"
    val artistName = c.subtitle.substringAfter("· ").substringBefore(" ·").let { if (it == c.subtitle || it.isBlank() || it.first().isDigit()) "Kabir Sen" else it }
    val tracks: List<Pair<Track, String>> = if (isMarigold) listOf(
        Sample.marigoldHours to "Kabir Sen", Sample.noor.copy(id = "noor-k", title = "Noor (Kabir’s version)", artist = "Kabir Sen", art = Art.A) to "Kabir Sen, Sufi Circle",
        Sample.chaiRain to "Kabir Sen, Ira Menon", Sample.localTrain to "Kabir Sen", Sample.ghatKiSubah to "Kabir Sen",
        Track("b2", "Bandra 2 AM", "Kabir Sen", Art.A, 221) to "Kabir Sen", Sample.kabira to "Kabir Sen", Track("dh", "Dhoop", "Kabir Sen", Art.A, 199) to "Kabir Sen",
    ) else tracksFor(c.id, 6).map { it.copy(art = c.art) to it.artist }
    var liked by rememberSaveable { mutableStateOf(isMarigold) }
    var saved by rememberSaveable { mutableStateOf(isMarigold) }

    LazyColumn(Modifier.fillMaxSize().background(DColor.Night), contentPadding = PaddingValues(bottom = LocalChromePad.current + 16.dp)) {
        item {
            Box(
                Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(Color(0xFF4A3212), DColor.Night))).statusBarsPadding(),
            ) {
                IconBtn(Icons.AutoMirrored.Rounded.ArrowBack, "Back", Modifier.padding(4.dp)) { nav.back() }
                ArtImage(c.art, Modifier.align(Alignment.Center).padding(top = 56.dp, bottom = 8.dp).size(240.dp), RoundedCornerShape(8.dp))
            }
        }
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                T(c.title, DType.H2)
                Row(Modifier.padding(top = 10.dp).clip(RoundedCornerShape(16.dp)).clickable { nav.go(Route.ArtistPage(Sample.artists.firstOrNull { it.name == artistName })) }, verticalAlignment = Alignment.CenterVertically) {
                    ArtImage(Sample.artists.firstOrNull { it.name == artistName }?.art ?: Art.KabirSen, Modifier.size(24.dp), CircleShape)
                    Spacer(Modifier.width(8.dp))
                    T(artistName, DType.Label)
                }
                T(if (isMarigold) "Album · 2026 · 10 songs, 38 min" else "${c.kind.name} · 2026 · ${tracks.size} songs", DType.Meta, color = DColor.TextDim, modifier = Modifier.padding(top = 8.dp))
            }
        }
        item {
            Row(Modifier.padding(start = 4.dp, end = 16.dp, top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                IconBtn(if (liked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder, if (liked) "Remove from Library" else "Save to Library", tint = if (liked) DColor.Marigold else DColor.TextDim) {
                    liked = !liked; nav.say(if (liked) "Saved to Your Library" else "Removed from Your Library")
                }
                IconBtn(if (saved) Icons.Rounded.CheckCircle else Icons.Rounded.DownloadForOffline, if (saved) "Downloaded" else "Download", tint = if (saved) DColor.Success else DColor.TextDim) {
                    saved = !saved; nav.say(if (saved) "Downloading ${c.title}" else "Removed download")
                }
                IconBtn(Icons.Rounded.Share, "Share", tint = DColor.TextDim) { nav.open(Sheet.Share(c.title, "Album · $artistName", c.art)) }
                IconBtn(Icons.Rounded.MoreVert, "More", tint = DColor.TextDim) { nav.open(Sheet.TrackMenu(tracks.first().first)) }
                Spacer(Modifier.weight(1f))
                ShuffleBtn(c.title, tracks.map { it.first })
                Spacer(Modifier.width(8.dp))
                PlayFab(c.title, tracks.first().first)
            }
        }
        tracks.forEachIndexed { i, (t, sub) ->
            item { TrackRow(t, index = i + 1, subtitle = sub, showArt = false, onClick = { player.play(t, c.title) }) }
        }
        item { T("28 September 2026\n℗ 2026 Marigold Records (sample label)", DType.Small, Modifier.padding(16.dp), color = DColor.TextDim) }
        item { Column { Spacer(Modifier.height(8.dp)); SectionHeader("More by $artistName", action = null) } }
        item { Rail(listOf(Collection("d2", "Kabira Nights", "2025 · EP", Art.KabirSen, Kind.EP), Collection("d3", "Local Train", "2024 · Single", Art.Hero, Kind.Single))) { nav.go(Route.AlbumPage(it)) } }
    }
}

// ------------------------------------------------------------------ S17 Playlist (collaborative)

@Composable
fun PlaylistScreen(playlist: Collection?) {
    val nav = LocalNav.current
    val player = LocalPlayer.current
    val c = playlist ?: Sample.roadTrip
    val collab = c.id == Sample.roadTrip.id
    val tracks: List<Pair<Track, String>> = if (collab) listOf(
        Sample.bassWala to "A", Sample.dholRepublic to "V", Sample.neonBandra to "M", Sample.rooftopNights to "A", Sample.gullyMein to "V", Sample.chaiRain to "V",
    ) else tracksFor(c.id).map { it to "" }
    var saved by rememberSaveable { mutableStateOf(collab) }

    LazyColumn(Modifier.fillMaxSize().background(DColor.Night), contentPadding = PaddingValues(bottom = LocalChromePad.current + 16.dp)) {
        item {
            Box(Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(Color(0xFF2D2140), DColor.Night))).statusBarsPadding()) {
                IconBtn(Icons.AutoMirrored.Rounded.ArrowBack, "Back", Modifier.padding(4.dp)) { nav.back() }
                if (collab) {
                    // 2×2 mosaic of the first four covers
                    Column(Modifier.align(Alignment.Center).padding(top = 56.dp, bottom = 8.dp).size(220.dp).clip(RoundedCornerShape(8.dp))) {
                        Row(Modifier.weight(1f)) { ArtImage(Art.I, Modifier.weight(1f).fillMaxSize(), RoundedCornerShape(0.dp)); ArtImage(Art.D, Modifier.weight(1f).fillMaxSize(), RoundedCornerShape(0.dp)) }
                        Row(Modifier.weight(1f)) { ArtImage(Art.C, Modifier.weight(1f).fillMaxSize(), RoundedCornerShape(0.dp)); ArtImage(Art.Indie, Modifier.weight(1f).fillMaxSize(), RoundedCornerShape(0.dp)) }
                    }
                } else ArtImage(c.art, Modifier.align(Alignment.Center).padding(top = 56.dp, bottom = 8.dp).size(220.dp), RoundedCornerShape(8.dp))
            }
        }
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                T(c.title, DType.H2)
                if (collab) {
                    T("Goa in December. Add your songs before Friday!", DType.Label.copy(fontWeight = FontWeight.Normal), color = DColor.TextDim, modifier = Modifier.padding(top = 4.dp))
                    Row(Modifier.padding(top = 10.dp).clip(RoundedCornerShape(16.dp)).clickable { nav.go(Route.Collaborators) }, verticalAlignment = Alignment.CenterVertically) {
                        Avatar(Sample.aisha, 24.dp)
                        Spacer(Modifier.width(8.dp))
                        T("Aisha  ·  with Vishal and 2 others", DType.Label)
                    }
                    T("Public  ·  24 songs, 1 h 32 min", DType.Meta, color = DColor.TextDim, modifier = Modifier.padding(top = 8.dp))
                } else {
                    T(c.subtitle, DType.Label.copy(fontWeight = FontWeight.Normal), color = DColor.TextDim, modifier = Modifier.padding(top = 4.dp))
                    T("Made for Vishal · ${tracks.size} songs", DType.Meta, color = DColor.TextDim, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
        item {
            Row(Modifier.padding(start = 4.dp, end = 16.dp, top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                if (collab) IconBtn(Icons.Rounded.PersonAddAlt, "Invite collaborators", tint = DColor.TextDim) { nav.go(Route.Collaborators) }
                else IconBtn(if (saved) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder, "Save", tint = if (saved) DColor.Marigold else DColor.TextDim) { saved = !saved }
                IconBtn(Icons.Rounded.DownloadForOffline, "Download", tint = DColor.TextDim) { nav.say("Downloading ${c.title}") }
                IconBtn(Icons.Rounded.Share, "Share", tint = DColor.TextDim) { nav.open(Sheet.Share(c.title, "Playlist", c.art)) }
                IconBtn(Icons.Rounded.MoreVert, "More", tint = DColor.TextDim) { if (collab) nav.go(Route.EditPlaylist) else nav.open(Sheet.Report(c.title, "Dafli")) }
                Spacer(Modifier.weight(1f))
                ShuffleBtn(c.title, tracks.map { it.first })
                Spacer(Modifier.width(8.dp))
                PlayFab(c.title, tracks.first().first)
            }
        }
        if (collab) item {
            Row(
                Modifier.fillMaxWidth().clickable { nav.go(Route.SearchTyping) }.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(Modifier.size(48.dp).clip(RoundedCornerShape(6.dp)).background(DColor.Elevated), contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.Add, null, tint = DColor.Text)
                }
                Spacer(Modifier.width(12.dp))
                T("Add songs", DType.BodyStrong)
            }
        }
        tracks.forEach { (t, by) ->
            item {
                TrackRow(t, onClick = { player.play(t, c.title) }, trailing = if (by.isEmpty()) null else ({
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AddedByBadge(by)
                        IconBtn(Icons.Rounded.MoreVert, "More options for ${t.title}", tint = DColor.TextDim, size = 20.dp) { nav.open(Sheet.TrackMenu(t)) }
                    }
                }))
            }
        }
        if (collab) item { T("Letters show who added each song.", DType.Small.copy(fontSize = 12.sp), Modifier.padding(16.dp), color = DColor.TextDim) }
    }
}
