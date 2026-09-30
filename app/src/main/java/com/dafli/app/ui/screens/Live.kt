package com.dafli.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.HelpOutline
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.Tune
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dafli.app.AppConfig
import com.dafli.app.data.Art
import com.dafli.app.data.Artist
import com.dafli.app.data.Collection
import com.dafli.app.data.Genres
import com.dafli.app.data.Kind
import com.dafli.app.data.SearchResults
import com.dafli.app.data.Track
import com.dafli.app.nav.Route
import com.dafli.app.nav.Sheet
import com.dafli.app.nav.Tab
import com.dafli.app.theme.DColor
import com.dafli.app.theme.DType
import com.dafli.app.ui.brand.DafliLockup
import com.dafli.app.ui.components.ArtImage
import com.dafli.app.ui.components.Avatar
import com.dafli.app.ui.components.ChipRow
import com.dafli.app.ui.components.CollectionCard
import com.dafli.app.ui.components.CoverCard
import com.dafli.app.ui.components.DField
import com.dafli.app.ui.components.GroupLabel
import com.dafli.app.ui.components.IconBtn
import com.dafli.app.ui.components.IconTile
import com.dafli.app.ui.components.LibraryRow
import com.dafli.app.ui.components.LocalChromePad
import com.dafli.app.ui.components.LocalLib
import com.dafli.app.ui.components.LocalNav
import com.dafli.app.ui.components.LocalPlatform
import com.dafli.app.ui.components.LocalPlayer
import com.dafli.app.ui.components.LocalRepo
import com.dafli.app.ui.components.Note
import com.dafli.app.ui.components.PillButton
import com.dafli.app.ui.components.PrimaryButton
import com.dafli.app.ui.components.SearchField
import com.dafli.app.ui.components.SecondaryButton
import com.dafli.app.ui.components.SectionHeader
import com.dafli.app.ui.components.SettingsRow
import com.dafli.app.ui.components.T
import com.dafli.app.ui.components.TextLink
import com.dafli.app.ui.components.ToggleRow
import com.dafli.app.ui.components.TopBar
import com.dafli.app.ui.components.TrackCard
import com.dafli.app.ui.components.TrackRow
import kotlinx.coroutines.delay
import java.time.LocalTime

// =================================================================== Onboarding

/** S02 · Welcome (live). No fake login: the app works without an account. */
@Composable
fun WelcomeLive() {
    val nav = LocalNav.current
    Box(Modifier.fillMaxSize().background(DColor.Night)) {
        ArtImage(Art.Hero, Modifier.fillMaxWidth().height(560.dp), RoundedCornerShape(0.dp))
        Box(Modifier.fillMaxWidth().height(560.dp).background(Brush.verticalGradient(0f to DColor.Night.copy(alpha = 0.35f), 0.45f to DColor.Night.copy(alpha = 0.15f), 1f to DColor.Night)))
        Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(horizontal = 24.dp), verticalArrangement = Arrangement.Bottom) {
            DafliLockup(42.dp)
            Spacer(Modifier.height(16.dp))
            T("Hindi music.\nMade for you.", DType.H1.copy(fontSize = 36.sp, lineHeight = 42.sp))
            Spacer(Modifier.height(12.dp))
            T("Discover Hindi and Bollywood songs, romantic favourites and classics. No account or API key needed.", DType.Body, color = DColor.TextDim)
            Spacer(Modifier.height(28.dp))
            PrimaryButton("Get started") { nav.go(Route.Preferences) }
            Spacer(Modifier.height(16.dp))
            T(
                "Music is streamed from ${AppConfig.MUSIC_SOURCE}. By continuing you agree to our Privacy Policy.", DType.Tiny.copy(fontWeight = FontWeight.Normal),
                Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp)).clickable { nav.go(Route.PrivacyPolicy) }.padding(8.dp), color = DColor.TextDim, align = TextAlign.Center,
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

/** S06 · What do you listen to (live). Also used from Settings as "Your taste". */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PreferencesLive(editing: Boolean) {
    val nav = LocalNav.current
    val lib = LocalLib.current
    val picked = remember { mutableStateListOf<String>().apply { addAll(lib.genres) } }
    var name by rememberSaveable { mutableStateOf(lib.name) }
    fun done() {
        lib.setGenres(picked.toList())
        if (editing) { lib.rename(name); nav.back(); nav.say("Saved") } else { lib.finishOnboarding(name); nav.resetTo(Route.Home) }
    }
    Column(Modifier.fillMaxSize().background(DColor.Night).imePadding()) {
        TopBar(if (editing) "Your taste" else null) {
            if (!editing) TextLink("Skip", Modifier.padding(end = 12.dp), color = DColor.TextDim, style = DType.BodyStrong) { lib.finishOnboarding(""); nav.resetTo(Route.Home) }
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
            if (!editing) {
                Spacer(Modifier.height(8.dp))
                T("What do you listen to?", DType.H1)
                T("Pick a few. Your Home is built from these first.", DType.Body, color = DColor.TextDim, modifier = Modifier.padding(top = 6.dp))
            }
            Spacer(Modifier.height(20.dp))
            DField("Your name (optional)", name, { if (it.length <= 30) name = it }, placeholder = "What should we call you?", helper = "Only saved on this phone.")
            T("GENRES", DType.Overline, color = DColor.TextDim, modifier = Modifier.padding(top = 28.dp, bottom = 12.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Genres.forEach { g ->
                    val on = g.api in picked
                    Row(
                        Modifier.height(40.dp).clip(RoundedCornerShape(20.dp)).background(if (on) DColor.Text else DColor.Elevated)
                            .clickable { if (on) picked.remove(g.api) else picked.add(g.api) }.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (on) { Icon(Icons.Rounded.Check, null, tint = DColor.Night, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)) }
                        T(g.name, DType.Label, color = if (on) DColor.Night else DColor.Text)
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
        Row(Modifier.fillMaxWidth().background(DColor.Surface).navigationBarsPadding().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                T(if (picked.isEmpty()) "Pick at least one" else "${picked.size} picked", DType.BodyStrong)
                T("You can change this any time", DType.Small, color = DColor.TextDim)
            }
            PrimaryButton(if (editing) "Save" else "Continue", Modifier.width(160.dp), enabled = picked.isNotEmpty()) { done() }
        }
    }
}

// =================================================================== Home

@Composable
fun HomeLive() {
    val nav = LocalNav.current
    val lib = LocalLib.current
    val repo = LocalRepo.current
    val player = LocalPlayer.current
    val greeting = when (LocalTime.now().hour) { in 5..11 -> "Good morning"; in 12..16 -> "Good afternoon"; else -> "Good evening" }
    val picks = lib.genres.take(2).mapNotNull { api -> Genres.firstOrNull { it.api == api } }
    val trending = rememberLoad("trending") { repo.trending(limit = 20) }
    val playlists = rememberLoad("playlists") { repo.trendingPlaylists() }

    LazyColumn(
        Modifier.fillMaxSize().background(Brush.verticalGradient(0f to Color(0xFF2A1F0C), 0.25f to DColor.Night, 1f to DColor.Night)),
        contentPadding = PaddingValues(bottom = LocalChromePad.current + 16.dp),
    ) {
        item {
            Row(Modifier.statusBarsPadding().fillMaxWidth().padding(start = 16.dp, end = 4.dp, top = 8.dp, bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Avatar(lib.name.firstOrNull()?.uppercase() ?: "D", DColor.AvatarV, 36.dp, Modifier.clip(CircleShape).clickable { nav.go(Route.MyProfile) })
                Spacer(Modifier.width(12.dp))
                T(greeting, DType.H3, Modifier.weight(1f), maxLines = 1)
                IconBtn(Icons.Rounded.Settings, "Settings") { nav.go(Route.Settings) }
            }
        }
        // Hero: resume last song, else the top trending song
        item {
            val last = lib.recent.firstOrNull()
            val heroTrack = last ?: (trending.state as? Load.Ok)?.value?.firstOrNull()
            if (heroTrack != null) HeroCard(
                label = if (last != null) "JUMP BACK IN" else "HINDI MUSIC", t = heroTrack,
                onOpen = { player.play(heroTrack, if (last != null) "Recently played" else "Explore Hindi songs", if (last != null) lib.recent.toList() else (trending.state as? Load.Ok)?.value); nav.go(Route.NowPlaying) },
            ) else LoadBox(trending, 210) { }
        }
        if (lib.recent.isNotEmpty()) {
            item { Column { Spacer(Modifier.height(28.dp)); SectionHeader("Recently played") { nav.go(Route.History) } } }
            item { TrackRail(lib.recent.take(10), "Recently played") }
        }
        item { Column { Spacer(Modifier.height(28.dp)); SectionHeader("Explore Hindi songs", sub = "Browse the Hindi catalogue") { nav.go(Route.Genre("All Hindi songs")) } } }
        item { LoadBox(trending, 180) { TrackRail(it.take(12), "Explore Hindi songs") } }
        picks.forEach { g ->
            item { Column { Spacer(Modifier.height(28.dp)); SectionHeader("For you · ${g.name}") { nav.go(Route.Genre(g.name)) } } }
            item { GenreRail(g.api, "For you · ${g.name}") }
        }
        item { Column { Spacer(Modifier.height(28.dp)); SectionHeader("Hindi playlists", action = null) } }
        item {
            LoadBox(playlists, 180) { list ->
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 12.dp)) {
                    items(list) { c -> CollectionCard(c, 160.dp) { nav.go(Route.PlaylistPage(c)) } }
                }
            }
        }
        item {
            val artists = (trending.state as? Load.Ok)?.value.orEmpty().filter { it.artistId != null }.distinctBy { it.artistId }.take(10)
            if (artists.isNotEmpty()) Column {
                Spacer(Modifier.height(28.dp)); SectionHeader("Artists to know", action = null)
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 12.dp)) {
                    items(artists) { t -> CoverCard(t.art, t.artist, "Artist", 108.dp, circle = true, url = t.artUrl) { nav.go(Route.ArtistPage(Artist(t.artist, t.art, id = t.artistId))) } }
                }
            }
        }
        item { Column { Spacer(Modifier.height(28.dp)); SectionHeader("Browse Hindi music") { nav.switchTab(Tab.Search) } } }
        item { GenreGrid(Genres.take(6)) }
    }
}

@Composable
private fun HeroCard(label: String, t: Track, onOpen: () -> Unit) {
    val player = LocalPlayer.current
    val playingThis = player.hasTrack && player.current.id == t.id && player.isPlaying
    Box(Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(210.dp).clip(RoundedCornerShape(20.dp)).clickable(onClick = onOpen)) {
        ArtImage(t, Modifier.fillMaxSize(), RoundedCornerShape(20.dp))
        Box(Modifier.fillMaxSize().background(Brush.verticalGradient(0f to DColor.Night.copy(alpha = 0.35f), 0.45f to DColor.Night.copy(alpha = 0.1f), 1f to DColor.Night.copy(alpha = 0.92f))))
        T(label, DType.Tiny.copy(fontWeight = FontWeight.SemiBold), Modifier.padding(16.dp))
        Column(Modifier.align(Alignment.BottomStart).padding(16.dp).padding(end = 72.dp)) {
            T(t.title, DType.Title.copy(fontSize = 22.sp, lineHeight = 28.sp), maxLines = 2)
            T(t.artist, DType.Meta, color = DColor.TextDim, maxLines = 1)
        }
        Box(
            Modifier.align(Alignment.BottomEnd).padding(16.dp).size(52.dp).clip(CircleShape).background(DColor.Marigold)
                .clickable { if (playingThis) player.toggle() else onOpen() },
            contentAlignment = Alignment.Center,
        ) { Icon(if (playingThis) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, if (playingThis) "Pause" else "Play ${t.title}", tint = DColor.Ink, modifier = Modifier.size(30.dp)) }
    }
}

@Composable
fun TrackRail(list: List<Track>, source: String) {
    val player = LocalPlayer.current
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 12.dp)) {
        items(list, key = { it.id }) { t -> TrackCard(t, 132.dp) { player.play(t, source, list) } }
    }
}

@Composable
private fun GenreRail(genreApi: String, source: String) {
    val repo = LocalRepo.current
    val l = rememberLoad(genreApi) { repo.trending(genreApi, 12) }
    LoadBox(l, 180) { TrackRail(it, source) }
}

@Composable
fun GenreGrid(list: List<com.dafli.app.data.Genre>) {
    val nav = LocalNav.current
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        list.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { g -> MoodTile(g.name, null, g.art, Modifier.weight(1f), height = 96) { nav.go(Route.Genre(g.name)) } }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

// =================================================================== Search

@Composable
fun SearchLive() {
    val nav = LocalNav.current
    val lib = LocalLib.current
    val repo = LocalRepo.current
    val player = LocalPlayer.current
    val chart = rememberLoad("chart") { repo.trending(limit = 5) }
    LazyColumn(Modifier.fillMaxSize().background(DColor.Night), contentPadding = PaddingValues(bottom = LocalChromePad.current + 16.dp)) {
        item {
            Column(Modifier.statusBarsPadding().padding(horizontal = 16.dp).padding(top = 8.dp)) {
                T("Search", DType.H1)
                Spacer(Modifier.height(16.dp))
                SearchField("", {}, "Songs, artists or playlists", Icons.Rounded.Search, light = true, readOnlyClick = { nav.go(Route.SearchTyping) })
            }
        }
        if (lib.searches.isNotEmpty()) {
            item { Column { Spacer(Modifier.height(24.dp)); SectionHeader("Recent searches", action = "Clear all") { lib.clearSearches() } } }
            items(lib.searches.toList(), key = { it }) { q ->
                Row(Modifier.fillMaxWidth().clickable { nav.go(Route.SearchResults(q)) }.padding(start = 16.dp, end = 4.dp, top = 4.dp, bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(40.dp).clip(CircleShape).background(DColor.Elevated), contentAlignment = Alignment.Center) { Icon(Icons.Rounded.History, null, tint = DColor.TextDim, modifier = Modifier.size(20.dp)) }
                    Spacer(Modifier.width(12.dp))
                    T(q, DType.BodyStrong, Modifier.weight(1f), maxLines = 1)
                    IconBtn(Icons.Rounded.Close, "Remove $q", tint = DColor.TextDim, size = 20.dp) { lib.removeSearch(q) }
                }
            }
        }
        item { Column { Spacer(Modifier.height(24.dp)); SectionHeader("Hindi picks", sub = "Discover Hindi favourites", action = "See all") { nav.go(Route.Genre("All Hindi songs")) } } }
        item {
            LoadBox(chart, 300) { list ->
                Column(Modifier.padding(top = 8.dp)) { list.forEachIndexed { i, t -> TrackRow(t, index = i + 1, onClick = { player.play(t, "Hindi picks", list) }) } }
            }
        }
        item { Column { Spacer(Modifier.height(20.dp)); SectionHeader("Browse Hindi music", action = null) } }
        item { GenreGrid(Genres) }
    }
}

@Composable
fun SearchTypingLive() {
    val nav = LocalNav.current
    val lib = LocalLib.current
    val repo = LocalRepo.current
    val player = LocalPlayer.current
    var q by rememberSaveable { mutableStateOf("") }
    var debounced by remember { mutableStateOf("") }
    val focus = remember { FocusRequester() }
    LaunchedEffect(Unit) { runCatching { focus.requestFocus() } }
    LaunchedEffect(q) { delay(350); debounced = q.trim() }
    val results = rememberLoad(debounced) { if (debounced.length < 2) null else repo.search(debounced) }
    fun submit() { if (q.isNotBlank()) { lib.addSearch(q); nav.go(Route.SearchResults(q.trim())) } }

    Column(Modifier.fillMaxSize().background(DColor.Night).imePadding()) {
        Row(Modifier.statusBarsPadding().padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            SearchField(q, { q = it }, "Songs, artists or playlists", Icons.Rounded.Search, Modifier.weight(1f), focus = focus, onSearch = ::submit,
                trailing = { if (q.isNotEmpty()) Icon(Icons.Rounded.Close, "Clear", tint = DColor.TextDim, modifier = Modifier.size(20.dp).clip(CircleShape).clickable { q = "" }) })
            TextLink("Cancel", Modifier.padding(start = 8.dp), style = DType.BodyStrong) { nav.back() }
        }
        LazyColumn(Modifier.weight(1f)) {
            when (val s = results.state) {
                is Load.Ok -> s.value?.let { r ->
                    items(r.artists.take(3)) { a -> LibraryRow(a.name, "Artist", a.art, circle = true, url = a.artUrl, onClick = { lib.addSearch(a.name); nav.go(Route.ArtistPage(a)) }) }
                    items(r.tracks.take(8)) { t -> TrackRow(t, onClick = { lib.addSearch(q); player.play(t, "Search", r.tracks) }) }
                    items(r.playlists.take(3)) { c -> LibraryRow(c.title, "Playlist · ${c.subtitle}", c.art, url = c.artUrl, onClick = { nav.go(Route.PlaylistPage(c)) }) }
                    if (r.tracks.isEmpty() && r.artists.isEmpty()) item { T("No results for “$q”. Try another spelling.", DType.Body, Modifier.padding(24.dp), color = DColor.TextDim) }
                } ?: item { T("Type at least 2 letters", DType.Meta, Modifier.padding(24.dp), color = DColor.TextDim) }
                Load.Loading -> item { Note("Searching…") }
                is Load.Failed -> item { ErrorCard(s.message, results.retry) }
            }
        }
    }
}

@Composable
fun SearchResultsLive(query: String) {
    val nav = LocalNav.current
    val repo = LocalRepo.current
    val player = LocalPlayer.current
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val music = rememberPagedMusic(query) { page -> repo.searchPage(query, page) }
    val res = music.loaded
    Column(Modifier.fillMaxSize().background(DColor.Night)) {
        Row(Modifier.statusBarsPadding().padding(start = 4.dp, end = 16.dp, top = 4.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconBtn(Icons.AutoMirrored.Rounded.ArrowBack, "Back") { nav.back() }
            SearchField(query, {}, "", Icons.Rounded.Search, Modifier.weight(1f), readOnlyClick = { nav.back(); nav.go(Route.SearchTyping) })
        }
        ChipRow(listOf("All", "Songs", "Artists", "Playlists"), tab) { tab = it }
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(top = 12.dp, bottom = LocalChromePad.current + 16.dp)) {
            when (val s = res.state) {
                Load.Loading -> item { LoadBox(res, 400) {} }
                is Load.Failed -> item { ErrorCard(s.message, res.retry) }
                is Load.Ok -> {
                    resultsList(s.value, tab, { tab = it }, player = { t -> player.play(t, "Search: $query", s.value.tracks) }, nav = nav)
                    if (tab == 0 && s.value.tracks.isNotEmpty()) item {
                        SecondaryButton("See all Hindi songs", Modifier.padding(16.dp)) { tab = 1 }
                    }
                    if (tab == 1) item { MoreSongs(music) }
                }
            }
        }
    }
}

private fun androidx.compose.foundation.lazy.LazyListScope.resultsList(
    r: SearchResults, tab: Int, setTab: (Int) -> Unit, player: (Track) -> Unit, nav: com.dafli.app.nav.Navigator,
) {
    val top = r.artists.firstOrNull()
    if (tab == 0 && top != null) item {
        Column {
            T("TOP RESULT", DType.Tiny.copy(fontWeight = FontWeight.SemiBold), Modifier.padding(horizontal = 16.dp), color = DColor.TextDim)
            Row(
                Modifier.padding(16.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(DColor.Surface).clickable { nav.go(Route.ArtistPage(top)) }.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ArtImage(top, Modifier.size(76.dp))
                Spacer(Modifier.width(16.dp))
                Column { T(top.name, DType.Title.copy(fontSize = 22.sp), maxLines = 1); T("Artist · ${top.followers} followers", DType.Meta, color = DColor.TextDim) }
            }
        }
    }
    if (tab == 0 || tab == 1) {
        if (tab == 0) item { SectionHeader("Songs", onAction = { setTab(1) }) }
        val list = if (tab == 0) r.tracks.take(5) else r.tracks
        items(list, key = { "t" + it.id }) { t -> TrackRow(t, onClick = { player(t) }) }
    }
    if (tab == 0 || tab == 2) {
        if (tab == 0) item { Column { Spacer(Modifier.height(16.dp)); SectionHeader("Artists", onAction = { setTab(2) }) } }
        items(if (tab == 0) r.artists.take(3) else r.artists, key = { "a" + it.id }) { a -> LibraryRow(a.name, "Artist · ${a.followers} followers", a.art, circle = true, url = a.artUrl, onClick = { nav.go(Route.ArtistPage(a)) }) }
    }
    if (tab == 0 || tab == 3) {
        if (tab == 0) item { Column { Spacer(Modifier.height(16.dp)); SectionHeader("Playlists", onAction = { setTab(3) }) } }
        items(if (tab == 0) r.playlists.take(3) else r.playlists, key = { "p" + it.id }) { c -> LibraryRow(c.title, c.subtitle, c.art, url = c.artUrl, onClick = { nav.go(Route.PlaylistPage(c)) }) }
    }
    if (r.tracks.isEmpty() && r.artists.isEmpty() && r.playlists.isEmpty()) item { T("Nothing found. Try another spelling.", DType.Body, Modifier.padding(24.dp), color = DColor.TextDim) }
}

// =================================================================== Genre

@Composable
fun GenreLive(name: String) {
    val nav = LocalNav.current
    val repo = LocalRepo.current
    val player = LocalPlayer.current
    val g = Genres.firstOrNull { it.name == name } ?: Genres.first()
    val music = rememberPagedMusic(g.api) { page -> repo.browse(g.api, page) }
    val tracks = music.loaded
    val pls = rememberLoad("pl:" + g.name) { repo.search(g.api).playlists }
    LazyColumn(Modifier.fillMaxSize().background(DColor.Night), contentPadding = PaddingValues(bottom = LocalChromePad.current + 16.dp)) {
        item {
            Box(Modifier.fillMaxWidth().height(300.dp)) {
                ArtImage(g.art, Modifier.fillMaxSize(), RoundedCornerShape(0.dp))
                Box(Modifier.fillMaxSize().background(Brush.verticalGradient(0f to DColor.Night.copy(alpha = 0.4f), 0.4f to Color.Transparent, 1f to DColor.Night)))
                IconBtn(Icons.AutoMirrored.Rounded.ArrowBack, "Back", Modifier.statusBarsPadding().padding(4.dp)) { nav.back() }
                Column(Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                    T("GENRE", DType.Tiny.copy(fontWeight = FontWeight.SemiBold), color = DColor.TextDim)
                    T(g.name, DType.Display.copy(fontSize = 40.sp))
                }
            }
        }
        item {
            Row(Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                T("Hindi songs", DType.Title, Modifier.weight(1f))
                (tracks.state as? Load.Ok)?.value?.tracks?.firstOrNull()?.let { first -> PlayFab(g.name, first, list = (tracks.state as Load.Ok).value.tracks) }
            }
        }
        when (val s = tracks.state) {
            is Load.Ok -> {
                itemsIndexed(s.value.tracks, key = { _, t -> t.id }) { i, t -> TrackRow(t, index = i + 1, onClick = { player.play(t, g.name, s.value.tracks) }) }
                item { MoreSongs(music) }
            }
            Load.Loading -> item { LoadBox(tracks, 400) {} }
            is Load.Failed -> item { ErrorCard(s.message, tracks.retry) }
        }
        item {
            LoadBox(pls, 180) { list ->
                if (list.isNotEmpty()) Column {
                    Spacer(Modifier.height(24.dp)); SectionHeader("${g.name} playlists", action = null)
                    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 12.dp)) {
                        items(list) { c -> CollectionCard(c, 150.dp) { nav.go(Route.PlaylistPage(c)) } }
                    }
                }
            }
        }
    }
}

// =================================================================== Artist & playlist

@Composable
fun ArtistLive(seed: Artist) {
    val nav = LocalNav.current
    val repo = LocalRepo.current
    val player = LocalPlayer.current
    val id = seed.id ?: ""
    val artist = rememberLoad("a" + id) { repo.artist(id) }
    val music = rememberPagedMusic("at" + id) { page -> repo.artistSongsPage(id, page) }
    val tracks = music.loaded
    val a = (artist.state as? Load.Ok)?.value ?: seed
    var more by rememberSaveable { mutableStateOf(false) }
    LazyColumn(Modifier.fillMaxSize().background(DColor.Night), contentPadding = PaddingValues(bottom = LocalChromePad.current + 16.dp)) {
        item {
            Box(Modifier.fillMaxWidth().height(340.dp)) {
                ArtImage(a.art, Modifier.fillMaxSize(), RoundedCornerShape(0.dp), a.artUrl)
                Box(Modifier.fillMaxSize().background(Brush.verticalGradient(0f to DColor.Night.copy(alpha = 0.35f), 0.4f to Color.Transparent, 1f to DColor.Night)))
                IconBtn(Icons.AutoMirrored.Rounded.ArrowBack, "Back", Modifier.statusBarsPadding().padding(4.dp)) { nav.back() }
                Column(Modifier.align(Alignment.BottomStart).padding(horizontal = 16.dp)) {
                    T(a.name, DType.Display, maxLines = 2)
                    T(if (a.followers > 0) "${a.followers} followers on ${AppConfig.MUSIC_SOURCE}" else "Artist", DType.Label.copy(fontWeight = FontWeight.Normal), color = DColor.TextDim, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
        val list = (tracks.state as? Load.Ok)?.value?.tracks.orEmpty()
        item {
            Row(Modifier.padding(horizontal = 16.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Spacer(Modifier.weight(1f))
                if (list.isNotEmpty()) {
                    IconBtn(Icons.Rounded.Shuffle, "Shuffle play", tint = DColor.TextDim) { player.shuffle = true; player.play(list.random(), a.name, list) }
                    Spacer(Modifier.width(8.dp))
                    PlayFab(a.name, list.first(), list = list)
                }
            }
        }
        item { SectionHeader("Popular", action = null) }
        when (val s = tracks.state) {
            is Load.Ok -> {
                val shown = if (more) s.value.tracks else s.value.tracks.take(5)
                itemsIndexed(shown, key = { _, t -> t.id }) { i, t -> TrackRow(t, index = i + 1, subtitle = t.genre ?: t.artist, onClick = { player.play(t, a.name, s.value.tracks) }) }
                if (s.value.tracks.size > 5) item { TextLink(if (more) "Show less" else "See more", Modifier.padding(horizontal = 12.dp, vertical = 4.dp), color = DColor.TextDim) { more = !more } }
                if (more || s.value.tracks.size <= 5) item { MoreSongs(music) }
                if (s.value.tracks.isEmpty()) item { Note("No playable Hindi songs on this page.") }
            }
            Load.Loading -> item { LoadBox(tracks, 300) {} }
            is Load.Failed -> item { ErrorCard(s.message, tracks.retry) }
        }
        a.bio?.let { bio ->
            item {
                Column(Modifier.padding(16.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(DColor.Surface).padding(16.dp)) {
                    T("About", DType.BodyStrong)
                    Spacer(Modifier.height(8.dp))
                    T(bio, DType.Label.copy(fontWeight = FontWeight.Normal), color = DColor.TextDim)
                }
            }
        }
    }
}

@Composable
fun PlaylistLive(c: Collection) {
    val nav = LocalNav.current
    val repo = LocalRepo.current
    val player = LocalPlayer.current
    val platform = LocalPlatform.current
    val music = rememberPagedMusic(c.id) { page -> repo.playlistSongsPage(c, page) }
    val tracks = music.loaded
    LazyColumn(Modifier.fillMaxSize().background(DColor.Night), contentPadding = PaddingValues(bottom = LocalChromePad.current + 16.dp)) {
        item {
            Box(Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(Color(0xFF2D2140), DColor.Night))).statusBarsPadding()) {
                IconBtn(Icons.AutoMirrored.Rounded.ArrowBack, "Back", Modifier.padding(4.dp)) { nav.back() }
                ArtImage(c, Modifier.align(Alignment.Center).padding(top = 56.dp, bottom = 8.dp).size(220.dp))
            }
        }
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                T(c.title, DType.H2, maxLines = 2)
                T(c.subtitle, DType.Label.copy(fontWeight = FontWeight.Normal), color = DColor.TextDim, modifier = Modifier.padding(top = 4.dp))
            }
        }
        val list = (tracks.state as? Load.Ok)?.value?.tracks.orEmpty()
        item {
            Row(Modifier.padding(start = 4.dp, end = 16.dp, top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                if (c.shareUrl != null || c.remoteId != null) IconBtn(Icons.Rounded.Share, "Share", tint = DColor.TextDim) { platform.share(c.title, c.shareUrl ?: "${c.title} — listen on ${AppConfig.MUSIC_SOURCE}: ${AppConfig.MUSIC_SOURCE_URL}") }
                Spacer(Modifier.weight(1f))
                if (list.isNotEmpty()) {
                    IconBtn(Icons.Rounded.Shuffle, "Shuffle play", tint = DColor.TextDim) { player.shuffle = true; player.play(list.random(), c.title, list) }
                    Spacer(Modifier.width(8.dp))
                    PlayFab(c.title, list.first(), list = list)
                }
            }
        }
        when (val s = tracks.state) {
            is Load.Ok -> {
                items(s.value.tracks, key = { it.id }) { t -> TrackRow(t, onClick = { player.play(t, c.title, s.value.tracks) }) }
                item { MoreSongs(music) }
                if (s.value.tracks.isEmpty()) item { Note("No playable Hindi songs on this page.") }
            }
            Load.Loading -> item { LoadBox(tracks, 400) {} }
            is Load.Failed -> item { ErrorCard(s.message, tracks.retry) }
        }
    }
}

// =================================================================== Library

@Composable
fun LibraryLive() {
    val nav = LocalNav.current
    val lib = LocalLib.current
    val player = LocalPlayer.current
    Column(Modifier.fillMaxSize().background(DColor.Night)) {
        Row(Modifier.statusBarsPadding().padding(start = 16.dp, end = 4.dp, top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Avatar(lib.name.firstOrNull()?.uppercase() ?: "D", DColor.AvatarV, 36.dp, Modifier.clip(CircleShape).clickable { nav.go(Route.MyProfile) })
            Spacer(Modifier.width(12.dp))
            T("Your Library", DType.H3, Modifier.weight(1f))
            IconBtn(Icons.Rounded.Search, "Search") { nav.switchTab(Tab.Search) }
        }
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(bottom = LocalChromePad.current + 16.dp)) {
            item { LibraryRow("Liked Songs", "Playlist · ${lib.liked.size} songs", leading = { IconTile(Icons.Rounded.Favorite, Color(0xFF4B3F8F), DColor.Text) }, onClick = { nav.go(Route.LikedSongs) }) }
            item { LibraryRow("Recently played", "${lib.recent.size} songs", leading = { IconTile(Icons.Rounded.History, DColor.Elevated, DColor.TextDim) }, onClick = { nav.go(Route.History) }) }
            if (lib.recent.isNotEmpty()) {
                item { GroupLabel("Jump back in") }
                items(lib.recent.take(15), key = { it.id }) { t -> TrackRow(t, onClick = { player.play(t, "Recently played", lib.recent.toList()) }) }
            } else item {
                Column(Modifier.padding(24.dp)) {
                    T("Your library is empty", DType.Title)
                    T("Tap the heart on any song to save it here. Songs you play show up in Recently played.", DType.Body, color = DColor.TextDim, modifier = Modifier.padding(top = 8.dp))
                    Spacer(Modifier.height(16.dp))
                    PillButton("Find music", filled = true) { nav.switchTab(Tab.Search) }
                }
            }
        }
    }
}

@Composable
fun LikedLive() {
    val lib = LocalLib.current
    val player = LocalPlayer.current
    var q by rememberSaveable { mutableStateOf("") }
    val shown = lib.liked.filter { q.isBlank() || it.title.contains(q, true) || it.artist.contains(q, true) }
    LazyColumn(Modifier.fillMaxSize().background(DColor.Night), contentPadding = PaddingValues(bottom = LocalChromePad.current + 16.dp)) {
        item {
            Box(Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(Color(0xFF4B3F8F), DColor.Night)))) {
                Column {
                    TopBar(null, background = Color.Transparent)
                    Row(Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.Bottom) {
                        Column(Modifier.weight(1f)) {
                            T("Liked Songs", DType.H1.copy(fontSize = 34.sp, lineHeight = 40.sp))
                            T("${lib.liked.size} songs · saved on this phone", DType.Label.copy(fontWeight = FontWeight.Normal), color = DColor.TextDim)
                        }
                        if (lib.liked.isNotEmpty()) {
                            IconBtn(Icons.Rounded.Shuffle, "Shuffle", tint = DColor.TextDim) { player.shuffle = true; player.play(lib.liked.random(), "Liked Songs", lib.liked.toList()) }
                            PlayFab("Liked Songs", lib.liked.first(), list = lib.liked.toList())
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
        if (lib.liked.size > 5) item { SearchField(q, { q = it }, "Find in Liked Songs", Icons.Rounded.Search, Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) }
        items(shown, key = { it.id }) { t -> TrackRow(t, onClick = { player.play(t, "Liked Songs", lib.liked.toList()) }) }
        if (lib.liked.isEmpty()) item { Note("Songs you like will appear here. Tap the heart on the player or in a song’s menu.") }
    }
}

@Composable
fun HistoryLive() {
    val lib = LocalLib.current
    val player = LocalPlayer.current
    val nav = LocalNav.current
    ListScreen("Recently played") {
        items(lib.recent.toList(), key = { it.id }) { t -> TrackRow(t, onClick = { player.play(t, "Recently played", lib.recent.toList()) }) }
        if (lib.recent.isEmpty()) item { Note("Nothing here yet. Songs you play will show up here.") }
        else item { SecondaryButton("Clear listening history", Modifier.padding(16.dp)) { lib.clearRecent(); nav.say("Listening history cleared") } }
    }
}

// =================================================================== Profile & settings

@Composable
fun ProfileLive() {
    val nav = LocalNav.current
    val lib = LocalLib.current
    ListScreen(null, actions = { IconBtn(Icons.Rounded.Settings, "Settings") { nav.go(Route.Settings) } }) {
        item {
            Row(Modifier.padding(horizontal = 24.dp), verticalAlignment = Alignment.CenterVertically) {
                Avatar(lib.name.firstOrNull()?.uppercase() ?: "D", DColor.AvatarV, 96.dp)
                Spacer(Modifier.width(20.dp))
                Column(Modifier.weight(1f)) {
                    T(lib.name.ifBlank { "Music lover" }, DType.H2, maxLines = 2)
                    T("${lib.liked.size} liked · ${lib.recent.size} played", DType.Label.copy(fontWeight = FontWeight.Normal), color = DColor.TextDim)
                }
            }
        }
        item { Row(Modifier.padding(start = 24.dp, top = 16.dp, bottom = 16.dp)) { PillButton("Edit name & taste") { nav.go(Route.EditTaste) } } }
        item { SettingsRow("Liked Songs", "${lib.liked.size} songs", icon = Icons.Rounded.Favorite, onClick = { nav.go(Route.LikedSongs) }) }
        item { SettingsRow("Recently played", "Songs you played", icon = Icons.Rounded.History, onClick = { nav.go(Route.History) }) }
        item { SettingsRow("Your taste", lib.genres.mapNotNull { a -> Genres.firstOrNull { it.api == a }?.name }.joinToString(", ").ifBlank { "Pick genres" }, icon = Icons.Rounded.Tune, onClick = { nav.go(Route.EditTaste) }) }
        item { Note("Your profile lives only on this phone. Dafli has no accounts and never uploads your data.") }
    }
}

@Composable
fun SettingsLive() {
    val nav = LocalNav.current
    val lib = LocalLib.current
    val platform = LocalPlatform.current
    var confirmReset by remember { mutableStateOf(false) }
    ListScreen("Settings") {
        item { GroupLabel("Listening") }
        item { ToggleRow("Autoplay similar songs", "Keeps music going when a list ends", lib.autoplay) { lib.setAutoplayPref(it) } }
        item { SettingsRow("Your taste", "Hindi categories used for Home", icon = Icons.Rounded.Tune, onClick = { nav.go(Route.EditTaste) }) }
        item { GroupLabel("Support") }
        item { SettingsRow("Help & feedback", icon = Icons.AutoMirrored.Rounded.HelpOutline, onClick = { nav.go(Route.Help) }) }
        item { SettingsRow("Privacy policy", icon = Icons.Rounded.Lock, onClick = { nav.go(Route.PrivacyPolicy) }) }
        item { SettingsRow("Music from ${AppConfig.MUSIC_SOURCE}", "Hindi songs via a free community API; service availability may vary", icon = Icons.Rounded.Info, onClick = { platform.openUrl(AppConfig.MUSIC_SOURCE_URL) }) }
        item { SettingsRow("About", value = "Version ${platform.appVersion}", chevron = false) }
        item { GroupLabel("Data") }
        item {
            if (!confirmReset) SettingsRow("Reset app", "Clears likes, history and taste on this phone", icon = Icons.Rounded.RestartAlt, titleColor = DColor.Error, onClick = { confirmReset = true })
            else Column(Modifier.padding(16.dp).fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(DColor.Surface).padding(16.dp)) {
                T("Reset everything?", DType.BodyStrong)
                T("Your liked songs, history and taste will be deleted from this phone.", DType.Meta, color = DColor.TextDim)
                Row(Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PillButton("Reset", filled = true) { lib.resetAll(); nav.resetTo(Route.Welcome, forward = false) }
                    PillButton("Cancel") { confirmReset = false }
                }
            }
        }
    }
}

@Composable
fun HelpLive() {
    val platform = LocalPlatform.current
    var open by rememberSaveable { mutableIntStateOf(-1) }
    val faqs = listOf(
        "Is Dafli free?" to "The app has no subscription or ads. It uses a free, unofficial JioSaavn community API. Availability can change.",
        "Why can’t I find a Bollywood song?" to "Search by song title or artist name, in Hindi or English. Only Hindi songs with a playable link are shown; catalogue and regional availability may vary.",
        "A song stopped playing" to "Check your internet connection. Some songs are removed by their artists; skip to the next one.",
        "Where are my liked songs stored?" to "Only on this phone. Reinstalling the app or using Settings → Reset app clears them.",
        "How do I report a song?" to "Open the song’s ⋮ menu and tap Report. We look at every report.",
    )
    ListScreen("Help") {
        item { GroupLabel("Common questions") }
        faqs.forEachIndexed { i, (q, a) ->
            item {
                Column(Modifier.fillMaxWidth().clickable { open = if (open == i) -1 else i }.padding(horizontal = 16.dp, vertical = 14.dp)) {
                    T(q, DType.BodyStrong)
                    if (open == i) T(a, DType.Meta, Modifier.padding(top = 6.dp), color = DColor.TextDim)
                }
            }
        }
        item {
            Column(Modifier.padding(16.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(DColor.Surface).padding(16.dp)) {
                T("Still stuck?", DType.BodyL.copy(fontWeight = FontWeight.SemiBold))
                T("Email us and we’ll get back to you.", DType.Meta, color = DColor.TextDim)
                Spacer(Modifier.height(12.dp))
                PillButton("Contact us") { platform.sendEmail(AppConfig.SUPPORT_EMAIL, "Dafli feedback (v${platform.appVersion})") }
            }
        }
    }
}

@Composable
fun PrivacyPolicyScreen() {
    ListScreen("Privacy policy") {
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                PRIVACY_TEXT.forEach { (h, body) ->
                    if (h.isNotEmpty()) T(h, DType.BodyStrong, Modifier.padding(top = 16.dp, bottom = 4.dp))
                    T(body, DType.Meta, color = DColor.TextDim)
                }
            }
        }
    }
}

/** Same text as docs/privacy.html (the public page linked from Play Console). */
val PRIVACY_TEXT = listOf(
    "" to "Dafli (“the app”) is a free music player. This policy explains what the app does with your information. Last updated: 30 September 2026.",
    "Information we collect" to "None. Dafli has no accounts, no analytics and no ads. We do not collect, store or sell personal data.",
    "Stored on your phone" to "Your name (if you type one), liked songs, recently played songs, recent searches and genre choices are saved only on your device. They are deleted when you use Settings → Reset app or uninstall the app.",
    "Music streaming" to "To play and search music, the app sends requests to the public ${AppConfig.MUSIC_SOURCE} API (saavn.sumit.co, JioSaavn and saavncdn.com content servers). Like any website, those servers receive your IP address and the song or search you request. See ${AppConfig.MUSIC_SOURCE}’s privacy policy at www.jiosaavn.com/corporate/privacy.",
    "Permissions" to "Internet (to stream music) and a media playback service (so music keeps playing with the screen off). No location, contacts, camera or microphone access.",
    "Children" to "Dafli is not directed at children under 13.",
    "Contact" to "Questions? Email ${AppConfig.SUPPORT_EMAIL}.",
)
