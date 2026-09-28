package com.dafli.app.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.NorthWest
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dafli.app.data.Art
import com.dafli.app.data.Collection
import com.dafli.app.data.Kind
import com.dafli.app.data.Sample
import com.dafli.app.data.Track
import com.dafli.app.data.asTime
import com.dafli.app.nav.Route
import com.dafli.app.theme.DColor
import com.dafli.app.theme.DType
import com.dafli.app.ui.components.ArtImage
import com.dafli.app.ui.components.ChipRow
import com.dafli.app.ui.components.CollectionCard
import com.dafli.app.ui.components.ExplicitBadge
import com.dafli.app.ui.components.IconBtn
import com.dafli.app.ui.components.LocalChromePad
import com.dafli.app.ui.components.LocalNav
import com.dafli.app.ui.components.LocalPlayer
import com.dafli.app.ui.components.SearchField
import com.dafli.app.ui.components.SectionHeader
import com.dafli.app.ui.components.T
import com.dafli.app.ui.components.TextLink
import com.dafli.app.ui.components.TrackRow

// ------------------------------------------------------------------ S11 Search · Browse

private data class Recent(val title: String, val meta: String, val art: Art?, val circle: Boolean = false)

@Composable
fun SearchScreen() {
    val nav = LocalNav.current
    val recents = remember {
        mutableStateListOf(
            Recent("Kabir Sen", "Artist", Art.KabirSen, circle = true),
            Recent("Noor", "Song · Sufi Circle", Art.J),
            Recent("purane gaane 90s", "Search", null),
            Recent("Neon Bandra", "Album · The Sea Link", Art.C),
        )
    }
    LazyColumn(Modifier.fillMaxSize().background(DColor.Night), contentPadding = PaddingValues(bottom = LocalChromePad.current + 16.dp)) {
        item {
            Column(Modifier.statusBarsPadding().padding(horizontal = 16.dp).padding(top = 8.dp)) {
                T("Search", DType.H1)
                Spacer(Modifier.height(16.dp))
                SearchField("", {}, "Songs, artists, albums or playlists", Icons.Rounded.Search, light = true, readOnlyClick = { nav.go(Route.SearchTyping) })
                T("Type in English, Hindi or your own language", DType.Small, Modifier.padding(top = 8.dp, start = 2.dp), color = DColor.TextDim)
            }
        }
        if (recents.isNotEmpty()) {
            item { Column { Spacer(Modifier.height(24.dp)); SectionHeader("Recent searches", action = "Clear all") { recents.clear() } } }
            items(recents, key = { it.title }) { r ->
                Row(
                    Modifier.fillMaxWidth().clickable {
                        when {
                            r.meta == "Artist" -> nav.go(Route.ArtistPage())
                            r.art == null -> nav.go(Route.SearchResults(r.title))
                            r.meta.startsWith("Album") -> nav.go(Route.AlbumPage(Collection("nb", "Neon Bandra", "Album · The Sea Link", Art.C, Kind.Album)))
                            else -> nav.go(Route.SearchResults(r.title))
                        }
                    }.padding(start = 16.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (r.art != null) ArtImage(r.art, Modifier.size(48.dp), if (r.circle) CircleShape else RoundedCornerShape(6.dp))
                    else Box(Modifier.size(48.dp).clip(CircleShape).background(DColor.Elevated), contentAlignment = Alignment.Center) {
                        Icon(Icons.Rounded.Search, null, tint = DColor.TextDim)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        T(r.title, DType.BodyStrong, maxLines = 1)
                        T(r.meta, DType.Meta, color = DColor.TextDim, maxLines = 1)
                    }
                    IconBtn(Icons.Rounded.Close, "Remove ${r.title}", tint = DColor.TextDim, size = 20.dp) { recents.remove(r) }
                }
            }
        }
        item { Column { Spacer(Modifier.height(24.dp)); SectionHeader("Browse by language", sub = "Hindi, English and regional music", action = null) } }
        item {
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 12.dp)) {
                items(Sample.languages) { l ->
                    Column(
                        Modifier.width(108.dp).height(84.dp).clip(RoundedCornerShape(12.dp)).background(DColor.Surface)
                            .clickable { nav.go(Route.Genre(l.name)) }.padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                    ) {
                        T(l.script, DType.H3.copy(fontWeight = FontWeight.SemiBold, fontFamily = null), maxLines = 1)
                        T(l.name, DType.Small.copy(fontWeight = FontWeight.Medium), color = DColor.TextDim)
                    }
                }
            }
        }
        item { Column { Spacer(Modifier.height(28.dp)); SectionHeader("Charts", sub = "Updated every Monday · Week of 28 Sep", action = null) } }
        item {
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 12.dp)) {
                item {
                    ChartCard("Top 50 · India", "Most played this week", listOf(
                        Triple(Sample.bassWala, "2", true), Triple(Sample.neonBandra, "NEW", null), Triple(Sample.noor, "1", false),
                    ))
                }
                item {
                    ChartCard("Viral 50 · India", "Most shared this week", listOf(
                        Triple(Sample.dholRepublic, "5", true), Triple(Sample.paadalNights, "NEW", null), Triple(Sample.marigoldHours, "1", false),
                    ))
                }
            }
        }
        item { Column { Spacer(Modifier.height(28.dp)); SectionHeader("Browse all", action = null) } }
        item {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Sample.browseAll.chunked(2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        row.forEach { g -> MoodTile(g.name, null, g.art, Modifier.weight(1f), height = 108) { nav.go(Route.Genre(g.name)) } }
                    }
                }
            }
        }
    }
}

/** Weekly chart card. Movement: true = up by n, false = down by n, null = new entry. */
@Composable
private fun ChartCard(title: String, sub: String, rows: List<Triple<Track, String, Boolean?>>) {
    val nav = LocalNav.current
    val player = LocalPlayer.current
    Column(
        Modifier.width(300.dp).clip(RoundedCornerShape(16.dp)).background(DColor.Surface)
            .clickable { nav.go(Route.PlaylistPage(Collection(title, title, sub, rows.first().first.art))) }.padding(vertical = 16.dp),
    ) {
        T(title, DType.TitleS.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold), Modifier.padding(horizontal = 20.dp))
        T(sub, DType.Small, Modifier.padding(horizontal = 20.dp), color = DColor.TextDim)
        Spacer(Modifier.height(8.dp))
        rows.forEachIndexed { i, (t, move, up) ->
            Row(
                Modifier.fillMaxWidth().clickable { player.play(t, title) }.padding(horizontal = 20.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                T("${i + 1}", DType.BodyL.copy(fontWeight = FontWeight.Bold), Modifier.width(24.dp))
                ArtImage(t.art, Modifier.size(48.dp), RoundedCornerShape(6.dp))
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    T(t.title, DType.Label, maxLines = 1)
                    T(t.artist, DType.Small, color = DColor.TextDim, maxLines = 1)
                }
                when (up) {
                    null -> Box(Modifier.clip(RoundedCornerShape(4.dp)).background(DColor.Elevated).padding(horizontal = 6.dp, vertical = 2.dp)) {
                        T("NEW", DType.Tiny.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold), color = DColor.Info)
                    }
                    else -> Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(if (up) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown, if (up) "Up $move" else "Down $move", tint = if (up) DColor.Success else DColor.Error, modifier = Modifier.size(20.dp))
                        T(move, DType.Small.copy(fontWeight = FontWeight.SemiBold), color = DColor.TextDim)
                    }
                }
            }
        }
        TextLink("See full chart", Modifier.padding(start = 16.dp, top = 8.dp), style = DType.Meta.copy(fontWeight = FontWeight.SemiBold)) { nav.go(Route.PlaylistPage(Collection(title, title, sub, rows.first().first.art))) }
    }
}

// ------------------------------------------------------------------ S12 Search · Typing

private data class Suggestion(val title: String, val meta: String, val art: Art?, val icon: ImageVector?, val circle: Boolean = false, val go: Route)

@Composable
fun SearchTypingScreen() {
    val nav = LocalNav.current
    var q by rememberSaveable { mutableStateOf("kabir") }
    val focus = remember { FocusRequester() }
    LaunchedEffect(Unit) { runCatching { focus.requestFocus() } }
    val all = listOf(
        Suggestion("Kabir Sen", "Artist", Art.KabirSen, null, circle = true, go = Route.ArtistPage()),
        Suggestion("kabir sen marigold hours", "Search", null, Icons.Rounded.Search, go = Route.SearchResults("kabir sen marigold hours")),
        Suggestion("Marigold Hours", "Song · Kabir Sen", Art.A, null, go = Route.AlbumPage()),
        Suggestion("Kabir Sen Radio", "Playlist · Made for you", Art.F, null, go = Route.PlaylistPage(Collection("ksr", "Kabir Sen Radio", "Playlist · Made for you", Art.F))),
        Suggestion("Kabira — film songs", "Film · 12 songs", null, Icons.Rounded.Movie, go = Route.PlaylistPage(Collection("kfs", "Kabira — film songs", "Film · 12 songs", Art.KabirSen))),
        Suggestion("kabira lofi", "Search", null, Icons.Rounded.Search, go = Route.SearchResults("kabira lofi")),
        Suggestion("Nila Raghavan", "Artist", Art.B, null, circle = true, go = Route.ArtistPage(Sample.nilaRaghavan)),
        Suggestion("Noor", "Song · Sufi Circle", Art.J, null, go = Route.SearchResults("noor")),
        Suggestion("Bass Wala Pyaar", "Song · DJ Rudra", Art.I, null, go = Route.SearchResults("bass wala pyaar")),
    )
    val list = if (q.isBlank()) all.take(4) else all.filter { it.title.contains(q.trim(), ignoreCase = true) || it.meta.contains(q.trim(), ignoreCase = true) }

    Column(Modifier.fillMaxSize().background(DColor.Night).imePadding()) {
        Row(Modifier.statusBarsPadding().padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            SearchField(
                q, { q = it }, "Songs, artists, albums or playlists", Icons.Rounded.Search, Modifier.weight(1f), focus = focus,
                onSearch = { if (q.isNotBlank()) nav.go(Route.SearchResults(q.trim())) },
                trailing = {
                    if (q.isNotEmpty()) Icon(Icons.Rounded.Close, "Clear", tint = DColor.TextDim, modifier = Modifier.size(20.dp).clip(CircleShape).clickable { q = "" })
                    else Icon(Icons.Rounded.Mic, "Search by voice", tint = DColor.TextDim, modifier = Modifier.size(22.dp).clip(CircleShape).clickable { nav.go(Route.VoiceSearch) })
                },
            )
            TextLink("Cancel", Modifier.padding(start = 8.dp), style = DType.BodyStrong) { nav.back() }
        }
        LazyColumn(Modifier.weight(1f)) {
            items(list) { s ->
                Row(
                    Modifier.fillMaxWidth().clickable { nav.go(s.go) }.padding(start = 16.dp, end = 8.dp, top = 6.dp, bottom = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (s.art != null) ArtImage(s.art, Modifier.size(48.dp), if (s.circle) CircleShape else RoundedCornerShape(6.dp))
                    else Box(Modifier.size(48.dp).clip(CircleShape).background(DColor.Elevated), contentAlignment = Alignment.Center) {
                        Icon(s.icon ?: Icons.Rounded.Search, null, tint = DColor.TextDim)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        T(s.title, if (s.art == null) DType.BodyStrong.copy(fontWeight = FontWeight.Medium) else DType.BodyStrong, maxLines = 1)
                        T(s.meta, DType.Meta, color = DColor.TextDim)
                    }
                    if (s.meta == "Search") IconBtn(Icons.Rounded.NorthWest, "Use ${s.title}", tint = DColor.TextDim, size = 20.dp) { q = s.title }
                }
            }
            if (list.isEmpty()) item { T("No matches for “$q”. Try another spelling or language.", DType.Body, Modifier.padding(24.dp), color = DColor.TextDim) }
        }
    }
}

// ------------------------------------------------------------------ S13 Search · Results

@Composable
fun SearchResultsScreen(query: String) {
    val nav = LocalNav.current
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val songs = listOf(
        Sample.marigoldHours to "Kabir Sen · 3:48", Sample.chaiRain to "Kabir Sen, Ira Menon · 4:02",
        Sample.localTrain to "Kabir Sen · ${Sample.localTrain.durationSec.asTime()}", Sample.ghatKiSubah to "Kabir Sen · 5:10",
    )
    val albums = listOf(
        Collection("x1", "Marigold Hours", "Album · 2026", Art.A, Kind.Album),
        Collection("x2", "Kabira Nights", "EP · 2025", Art.KabirSen, Kind.EP),
        Collection("x3", "Rooftop Sessions", "Album · 2024", Art.Indie, Kind.Album),
    )
    val playlists = listOf(
        Collection("p1", "This is Kabir Sen", "By Dafli", Art.F),
        Collection("p2", "Indie Nights", "By Dafli", Art.Indie),
        Collection("p3", "Kabir & friends", "By Aisha", Art.E),
    )
    Column(Modifier.fillMaxSize().background(DColor.Night)) {
        Row(Modifier.statusBarsPadding().padding(start = 4.dp, end = 16.dp, top = 4.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconBtn(Icons.AutoMirrored.Rounded.ArrowBack, "Back") { nav.back() }
            SearchField(query, {}, "", Icons.Rounded.Search, Modifier.weight(1f), readOnlyClick = { nav.back() })
        }
        ChipRow(listOf("All", "Songs", "Artists", "Albums", "Playlists"), tab) { tab = it }
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(top = 16.dp, bottom = LocalChromePad.current + 16.dp)) {
            if (tab == 0 || tab == 2) item { Column {
                T("TOP RESULT", DType.Tiny.copy(fontWeight = FontWeight.SemiBold), Modifier.padding(horizontal = 16.dp), color = DColor.TextDim)
                Row(
                    Modifier.padding(16.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(DColor.Surface)
                        .clickable { nav.go(Route.ArtistPage()) }.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ArtImage(Art.KabirSen, Modifier.size(76.dp), CircleShape)
                    Spacer(Modifier.width(16.dp))
                    Column {
                        T("Kabir Sen", DType.Title.copy(fontSize = 22.sp))
                        T("Artist · Hindi indie pop", DType.Meta, color = DColor.TextDim)
                    }
                }
            } }
            if (tab == 0 || tab == 1) {
                item { SectionHeader("Songs", onAction = if (tab == 0) ({ tab = 1 }) else null) }
                songs.forEach { (t, sub) -> item { TrackRow(t, subtitle = sub) } }
            }
            if (tab == 0 || tab == 3) {
                item { Column { Spacer(Modifier.height(20.dp)); SectionHeader("Albums", onAction = if (tab == 0) ({ tab = 3 }) else null) } }
                item { Rail(albums) { nav.go(Route.AlbumPage(it)) } }
            }
            if (tab == 0 || tab == 4) {
                item { Column { Spacer(Modifier.height(20.dp)); SectionHeader("Playlists", onAction = if (tab == 0) ({ tab = 4 }) else null) } }
                item { Rail(playlists) { nav.go(Route.PlaylistPage(it)) } }
            }
            if (tab == 2) item { Column {
                listOf(Sample.iraMenon, Sample.nilaRaghavan).forEach { a ->
                    com.dafli.app.ui.components.LibraryRow(a.name, "Artist", a.art, circle = true, onClick = { nav.go(Route.ArtistPage(a)) })
                }
            } }
        }
    }
}

@Composable
fun Rail(items: List<Collection>, size: Int = 140, onClick: (Collection) -> Unit) {
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 12.dp)) {
        items(items) { c -> CollectionCard(c, size.dp) { onClick(c) } }
    }
}

// ------------------------------------------------------------------ S14 Genre / language page

@Composable
fun GenreScreen(name: String) {
    val nav = LocalNav.current
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val art = Sample.browseAll.firstOrNull { it.name == name }?.art
        ?: Sample.moods.firstOrNull { it.name == name }?.art ?: Art.Punjabi
    val sub = when (name) {
        "Punjabi" -> "Bhangra, Punjabi pop and desi hip-hop"
        "Hindi", "Bollywood" -> "Film hits, indie and classics"
        "Devotional" -> "Bhajan, sufi, kirtan"
        "Chill", "Lo-fi" -> "Slow evenings, soft beats"
        "Workout" -> "High BPM to keep you moving"
        else -> "Popular songs and playlists in $name"
    }
    val playlists = listOf(
        Collection("g1", "$name Heat".replace("Punjabi Heat", "Punjabi Heat"), "Updated Friday", art),
        Collection("g2", if (name == "Punjabi") "Bhangra Beats" else "$name Classics", "Wedding favourites", Art.D),
        Collection("g3", if (name == "Punjabi") "Gym Jatt" else "$name Workout", "High BPM", Art.Workout),
    )
    val top = listOf(Sample.dholRepublic, Sample.bassWala, Sample.gullyMein, Sample.sarsonNights, Sample.chalHun)
    LazyColumn(Modifier.fillMaxSize().background(DColor.Night), contentPadding = PaddingValues(bottom = LocalChromePad.current + 16.dp)) {
        item {
            Box(Modifier.fillMaxWidth().height(340.dp)) {
                ArtImage(art, Modifier.fillMaxSize(), RoundedCornerShape(0.dp))
                Box(Modifier.fillMaxSize().background(Brush.verticalGradient(0f to DColor.Night.copy(alpha = 0.4f), 0.4f to DColor.Night.copy(alpha = 0.1f), 1f to DColor.Night)))
                IconBtn(Icons.AutoMirrored.Rounded.ArrowBack, "Back", Modifier.statusBarsPadding().padding(4.dp)) { nav.back() }
                Column(Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                    T("LANGUAGE · GENRE", DType.Tiny.copy(fontWeight = FontWeight.SemiBold), color = DColor.TextDim)
                    T(name, DType.Display.copy(fontSize = 40.sp))
                    T(sub, DType.Label.copy(fontWeight = FontWeight.Normal), color = DColor.TextDim)
                }
            }
        }
        item { ChipRow(listOf("Top picks", "New", "Old is gold", "Workout"), tab) { tab = it } }
        item { Column { Spacer(Modifier.height(24.dp)); SectionHeader("Popular playlists") { nav.go(Route.SeeAllMadeForYou) } } }
        item { Rail(playlists, 160) { nav.go(Route.PlaylistPage(it)) } }
        item { Column { Spacer(Modifier.height(24.dp)); SectionHeader("Top songs this week", sub = "Chart · plays in India · week of 28 Sep") { nav.go(Route.PlaylistPage()) } } }
        top.forEachIndexed { i, t -> item { TrackRow(t, index = i + 1) } }
        item { Column { Spacer(Modifier.height(20.dp)); SectionHeader("New releases") { nav.go(Route.Updates) } } }
        item {
            Rail(listOf(
                Collection("n1", "Gully Mein", "Single · MC Veer", Art.HipHop, Kind.Single),
                Collection("n2", "Sarson Nights", "EP · Gurnoor", Art.Punjabi, Kind.EP),
                Collection("n3", "Bass Wala Pyaar", "Single · DJ Rudra", Art.I, Kind.Single),
            )) { nav.go(Route.AlbumPage()) }
        }
    }
}

// ------------------------------------------------------------------ S54 Voice search

@Composable
fun VoiceSearchScreen() {
    val nav = LocalNav.current
    val phrase = "play kabir sen\nmarigold hours"
    var shown by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        // Simulated speech-to-text: reveal the words, then show results.
        while (shown < phrase.length) { kotlinx.coroutines.delay(55); shown++ }
        kotlinx.coroutines.delay(900)
        nav.back(); nav.go(Route.SearchResults("kabir sen marigold hours"))
    }
    val wave = rememberInfiniteTransition(label = "wave")
    val phase by wave.animateFloat(0f, 1f, infiniteRepeatable(tween(900), RepeatMode.Reverse), label = "phase")
    Column(Modifier.fillMaxSize().background(DColor.Night).statusBarsPadding().navigationBarsPadding()) {
        Row(Modifier.fillMaxWidth().padding(4.dp), horizontalArrangement = Arrangement.End) {
            IconBtn(Icons.Rounded.Close, "Close voice search") { nav.back() }
        }
        Column(Modifier.padding(horizontal = 24.dp).padding(top = 40.dp)) {
            T("Listening…", DType.BodyL, color = DColor.TextDim)
            Spacer(Modifier.height(12.dp))
            T(phrase.take(shown), DType.H1.copy(fontSize = 36.sp, lineHeight = 44.sp))
        }
        Spacer(Modifier.weight(1f))
        // live level meter
        Row(Modifier.fillMaxWidth().height(80.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            val base = listOf(0.35f, 0.6f, 0.9f, 0.55f, 1f, 0.7f, 0.45f, 0.8f, 0.5f, 0.3f)
            base.forEachIndexed { i, b ->
                val h = 16f + 56f * (if (i % 2 == 0) b * (0.5f + phase / 2) else b * (1f - phase / 2))
                Box(Modifier.padding(horizontal = 3.dp).width(6.dp).height(h.dp).clip(RoundedCornerShape(3.dp)).background(DColor.Marigold))
            }
        }
        Spacer(Modifier.height(40.dp))
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            TextLink("Type instead", style = DType.BodyStrong) { nav.back(); nav.go(Route.SearchTyping) }
        }
        T(
            "The mic is only on while this screen is open. See Privacy for how voice is handled.", DType.Small,
            Modifier.fillMaxWidth().padding(horizontal = 40.dp, vertical = 24.dp), color = DColor.TextDim, align = TextAlign.Center,
        )
    }
}
