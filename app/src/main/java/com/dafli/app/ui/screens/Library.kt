package com.dafli.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.DragHandle
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material.icons.rounded.RemoveCircle
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.UploadFile
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dafli.app.data.Art
import com.dafli.app.data.Collection
import com.dafli.app.data.Kind
import com.dafli.app.data.Sample
import com.dafli.app.nav.Route
import com.dafli.app.theme.DColor
import com.dafli.app.theme.DType
import com.dafli.app.ui.components.ArtImage
import com.dafli.app.ui.components.Avatar
import com.dafli.app.ui.components.ChipRow
import com.dafli.app.ui.components.DField
import com.dafli.app.ui.components.DSwitch
import com.dafli.app.ui.components.GroupLabel
import com.dafli.app.ui.components.IconBtn
import com.dafli.app.ui.components.IconTile
import com.dafli.app.ui.components.LibraryRow
import com.dafli.app.ui.components.LocalChromePad
import com.dafli.app.ui.components.LocalLib
import com.dafli.app.ui.components.LocalNav
import com.dafli.app.ui.components.LocalPlayer
import com.dafli.app.ui.components.Note
import com.dafli.app.ui.components.PrimaryButton
import com.dafli.app.ui.components.SearchField
import com.dafli.app.ui.components.SecondaryButton
import com.dafli.app.ui.components.SettingsRow
import com.dafli.app.ui.components.T
import com.dafli.app.ui.components.TextLink
import com.dafli.app.ui.components.TopBar
import com.dafli.app.ui.components.TrackRow

private val LikedTint = Color(0xFF4B3F8F)
private val DownloadTint = Color(0xFF1F3B2E)

@Composable
private fun Pin() = Icon(Icons.Rounded.PushPin, "Pinned", tint = DColor.TextDim, modifier = Modifier.size(13.dp))

@Composable
private fun Saved() = Icon(Icons.Rounded.Download, "Downloaded", tint = DColor.Success, modifier = Modifier.size(14.dp))

// ------------------------------------------------------------------ S23 Your Library

@Composable
fun LibraryScreen() {
    val nav = LocalNav.current
    val p = LocalPlayer.current
    var filter by rememberSaveable { mutableIntStateOf(-1) }
    var grid by rememberSaveable { mutableStateOf(false) }
    data class Item(val title: String, val sub: String, val art: Art?, val kind: Int, val go: Route, val circle: Boolean = false, val leading: (@Composable () -> Unit)? = null, val subLeading: (@Composable () -> Unit)? = null)
    val items = listOf(
        Item("Liked Songs", "Playlist · ${LocalLib.current.liked.size} songs", null, 0, Route.LikedSongs, leading = { IconTile(Icons.Rounded.Favorite, LikedTint, DColor.Text) }, subLeading = { Pin() }),
        Item("Downloads", "86 songs · 2.1 GB", null, 3, Route.Downloads, leading = { IconTile(Icons.Rounded.Download, DownloadTint, DColor.Success) }, subLeading = { Pin() }),
        Item("Road Trip 2026", "Playlist · Aisha · collaborative", Art.C, 0, Route.PlaylistPage(Sample.roadTrip)),
        Item("Kabir Sen", "Artist", Art.KabirSen, 1, Route.ArtistPage(Sample.kabirSen), circle = true),
        Item("Marigold Hours", "Album · Kabir Sen", Art.A, 2, Route.AlbumPage(Sample.marigoldAlbum), subLeading = { Saved() }),
        Item("Chill Evenings", "Folder · 4 playlists", null, 0, Route.Folder, leading = { IconTile(Icons.Rounded.Folder, DColor.Elevated, DColor.TextDim) }),
        Item("Vishal + Aisha", "Shared mix · updated daily", Art.J, 0, Route.SharedMix),
        Item("Local files", "Songs saved on this phone", null, 3, Route.LocalFiles, leading = { IconTile(Icons.Rounded.PhoneAndroid, DColor.Elevated, DColor.TextDim) }),
        Item("Paadal Nights", "EP · Nila Raghavan", Art.K, 2, Route.AlbumPage(Collection("pn", "Paadal Nights", "EP · Nila Raghavan", Art.K, Kind.EP))),
        Item("Ira Menon", "Artist", Art.IraMenon, 1, Route.ArtistPage(Sample.iraMenon), circle = true),
        Item("Morning Calm", "Playlist · Made for Vishal", Art.H, 0, Route.PlaylistPage(Sample.morningCalm)),
    )
    val shown = if (filter < 0) items else items.filter { it.kind == filter }

    Column(Modifier.fillMaxSize().background(DColor.Night)) {
        Row(Modifier.statusBarsPadding().padding(start = 16.dp, end = 4.dp, top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Avatar(Sample.me, 36.dp, Modifier.clip(CircleShape).clickable { nav.go(Route.MyProfile) })
            Spacer(Modifier.width(12.dp))
            T("Your Library", DType.H3, Modifier.weight(1f))
            IconBtn(Icons.Rounded.Search, "Search Your Library") { nav.go(Route.LikedSongs) }
            IconBtn(Icons.Rounded.Add, "Create playlist") { nav.go(Route.CreatePlaylist) }
        }
        ChipRow(listOf("Playlists", "Artists", "Albums", "Downloaded"), filter) { filter = if (filter == it) -1 else it }
        Row(Modifier.padding(start = 16.dp, end = 4.dp, top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Rounded.Sort, null, tint = DColor.Text, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            T("Recently played", DType.Label, Modifier.weight(1f))
            IconBtn(Icons.Rounded.GridView, if (grid) "Show as list" else "Show as grid", tint = if (grid) DColor.Text else DColor.TextDim, size = 20.dp) { grid = !grid }
        }
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(bottom = LocalChromePad.current + 16.dp)) {
            if (grid) {
                shown.chunked(3).forEach { row ->
                    item {
                        Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            row.forEach { it2 ->
                                Column(Modifier.weight(1f).clip(RoundedCornerShape(8.dp)).clickable { nav.go(it2.go) }) {
                                    Box(Modifier.fillMaxWidth().height(106.dp)) {
                                        if (it2.art != null) ArtImage(it2.art, Modifier.fillMaxSize(), if (it2.circle) CircleShape else RoundedCornerShape(6.dp))
                                        else Box(Modifier.fillMaxSize().clip(RoundedCornerShape(6.dp)).background(DColor.Elevated))
                                    }
                                    T(it2.title, DType.Meta.copy(fontWeight = FontWeight.SemiBold), Modifier.padding(top = 6.dp), maxLines = 1)
                                    T(it2.sub, DType.Small, color = DColor.TextDim, maxLines = 1)
                                }
                            }
                            repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
                        }
                    }
                }
            } else {
                shown.forEach { i ->
                    item { LibraryRow(i.title, i.sub, i.art, i.circle, leading = i.leading, subLeading = i.subLeading, onClick = { nav.go(i.go) }) }
                }
            }
        }
    }
}

// ------------------------------------------------------------------ S24 Liked Songs

@Composable
fun LikedSongsScreen() {
    val p = LocalPlayer.current
    var q by rememberSaveable { mutableStateOf("") }
    var tab by rememberSaveable { mutableIntStateOf(-1) }
    val tracks = Sample.allTracks.filter { p.isLiked(it) }
    val langOf = mapOf("noor" to 0, "mh" to 0, "cr" to 0, "sn" to 2, "nb" to 2, "bwp" to 1, "gm" to 1)
    val shown = tracks.filter { (q.isBlank() || it.title.contains(q, true) || it.artist.contains(q, true)) && (tab < 0 || langOf[it.id] == tab || (tab == 3 && it.id == "cr")) }
    LazyColumn(Modifier.fillMaxSize().background(DColor.Night), contentPadding = PaddingValues(bottom = LocalChromePad.current + 16.dp)) {
        item {
            Box(Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(LikedTint, DColor.Night)))) {
                Column {
                    TopBar(null, background = Color.Transparent)
                    Row(Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.Bottom) {
                        Column(Modifier.weight(1f)) {
                            T("Liked Songs", DType.H1.copy(fontSize = 34.sp, lineHeight = 40.sp))
                            T("${LocalLib.current.liked.size} songs · Vishal", DType.Label.copy(fontWeight = FontWeight.Normal), color = DColor.TextDim)
                        }
                        IconBtn(Icons.Rounded.Shuffle, "Shuffle", tint = DColor.TextDim) { p.shuffle = true; tracks.randomOrNull()?.let { p.play(it, "Liked Songs") } }
                        PlayFab("Liked Songs", tracks.firstOrNull() ?: Sample.noor)
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
        item { SearchField(q, { q = it }, "Find in Liked Songs", Icons.Rounded.Search, Modifier.padding(horizontal = 16.dp)) }
        item { Column { Spacer(Modifier.height(12.dp)); ChipRow(listOf("Hindi", "Punjabi", "Indie", "Lo-fi"), tab) { tab = if (tab == it) -1 else it } } }
        item { Spacer(Modifier.height(8.dp)) }
        shown.forEach { t -> item { TrackRow(t, onClick = { p.play(t, "Liked Songs") }) } }
        if (shown.isEmpty()) item { T("No liked songs match.", DType.Body, Modifier.padding(24.dp), color = DColor.TextDim) }
    }
}

// ------------------------------------------------------------------ S25 Create playlist

@Composable
fun CreatePlaylistScreen() {
    val nav = LocalNav.current
    var name by rememberSaveable { mutableStateOf("Sunday Chai") }
    var desc by rememberSaveable { mutableStateOf("") }
    var public by rememberSaveable { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().background(DColor.Night).imePadding()) {
        TopBar("New playlist", centerTitle = true)
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(16.dp))
            Column(
                Modifier.align(Alignment.CenterHorizontally).size(160.dp).clip(RoundedCornerShape(12.dp)).background(DColor.Elevated)
                    .border(1.dp, DColor.Line, RoundedCornerShape(12.dp)).clickable { nav.say("Opens your photos") },
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
            ) {
                Icon(Icons.Rounded.AddPhotoAlternate, null, tint = DColor.TextDim, modifier = Modifier.size(32.dp))
                T("Add cover", DType.Meta.copy(fontWeight = FontWeight.SemiBold), Modifier.padding(top = 8.dp))
                T("Optional", DType.Small, color = DColor.TextDim)
            }
            Spacer(Modifier.height(24.dp))
            DField("Name", name, { if (it.length <= 100) name = it }, helper = "${name.length} / 100")
            Spacer(Modifier.height(16.dp))
            DField("Description (optional)", desc, { desc = it }, placeholder = "What is this playlist for?")
            T("Who can see it", DType.Meta.copy(fontWeight = FontWeight.Medium), Modifier.padding(top = 20.dp, bottom = 8.dp), color = DColor.TextDim)
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(DColor.Elevated).padding(4.dp)) {
                Segment(Icons.Rounded.Lock, "Private", !public, Modifier.weight(1f)) { public = false }
                Segment(Icons.Rounded.Public, "Public", public, Modifier.weight(1f)) { public = true }
            }
            T(if (public) "Anyone can find and play this playlist." else "Only you can see this playlist. You can change this later.", DType.Small, Modifier.padding(top = 8.dp), color = DColor.TextDim)
            Spacer(Modifier.height(20.dp))
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).border(1.dp, DColor.Line, RoundedCornerShape(12.dp))
                    .clickable { nav.go(Route.CreateWithAi) }.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Rounded.AutoAwesome, null, tint = DColor.Text)
                Spacer(Modifier.width(12.dp))
                T("Or describe it and let AI pick songs", DType.Label)
            }
            Spacer(Modifier.height(24.dp))
        }
        PrimaryButton("Create playlist", Modifier.padding(16.dp).navigationBarsPadding(), enabled = name.isNotBlank()) {
            nav.back(); nav.go(Route.PlaylistPage(Collection("new-$name", name, if (public) "Public playlist · Vishal" else "Private playlist · Vishal", Art.E)))
            nav.say("Playlist created")
        }
    }
}

@Composable
private fun Segment(icon: ImageVector, label: String, on: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Row(
        modifier.height(44.dp).clip(RoundedCornerShape(10.dp)).background(if (on) DColor.Text else Color.Transparent).clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, null, tint = if (on) DColor.Night else DColor.TextDim, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        T(label, DType.BodyStrong, color = if (on) DColor.Night else DColor.Text)
    }
}

// ------------------------------------------------------------------ S26 Edit playlist

@Composable
fun EditPlaylistScreen() {
    val nav = LocalNav.current
    var name by rememberSaveable { mutableStateOf("Road Trip 2026") }
    var desc by rememberSaveable { mutableStateOf("Goa in December. Add your songs before Friday!") }
    val songs = remember { mutableStateListOf(Sample.bassWala, Sample.dholRepublic, Sample.neonBandra, Sample.rooftopNights) }
    Column(Modifier.fillMaxSize().background(DColor.Night).imePadding()) {
        TopBar("Edit playlist", centerTitle = true) { TextLink("Save", Modifier.padding(end = 12.dp), style = DType.BodyL.copy(fontWeight = FontWeight.Bold)) { nav.back(); nav.say("Playlist saved") } }
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(bottom = 32.dp)) {
            item {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(Modifier.height(8.dp))
                    ArtImage(Art.C, Modifier.size(140.dp), RoundedCornerShape(10.dp))
                    TextLink("Change", Modifier.padding(top = 4.dp)) { nav.say("Opens your photos") }
                }
            }
            item {
                Column(Modifier.padding(16.dp)) {
                    DField("Name", name, { name = it })
                    Spacer(Modifier.height(16.dp))
                    DField("Description", desc, { desc = it }, singleLine = false)
                }
            }
            item { SettingsRow("Who can see it", value = "Public", onClick = { nav.say("Only the owner can change privacy") }) }
            item { GroupLabel("Songs · drag to reorder") }
            itemsIndexed(songs, key = { _, t -> t.id }) { i, t ->
                Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp).height(64.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconBtn(Icons.Rounded.RemoveCircle, "Remove ${t.title}", tint = DColor.Error, size = 22.dp) { songs.remove(t) }
                    ArtImage(t.art, Modifier.size(44.dp), RoundedCornerShape(6.dp))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        T(t.title, DType.BodyStrong, maxLines = 1)
                        T(t.artist, DType.Meta, color = DColor.TextDim, maxLines = 1)
                    }
                    // Tap the handle to move a song up (a simple, accessible alternative to drag).
                    IconBtn(Icons.Rounded.DragHandle, "Move ${t.title} up", tint = DColor.TextDim) { if (i > 0) { songs.removeAt(i); songs.add(i - 1, t) } }
                }
            }
            item {
                Row(Modifier.fillMaxWidth().clickable { nav.go(Route.SearchTyping) }.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Add, null, tint = DColor.Text)
                    Spacer(Modifier.width(12.dp))
                    T("Add songs", DType.BodyStrong)
                }
            }
            item { SecondaryButton("Delete playlist", Modifier.padding(16.dp), danger = true) { nav.resetTo(com.dafli.app.nav.Route.Library, forward = false); nav.say("Playlist deleted") } }
        }
    }
}

// ------------------------------------------------------------------ S27 Collaborators

@Composable
fun CollaboratorsScreen() {
    val nav = LocalNav.current
    data class Member(val p: com.dafli.app.data.Person, val role: String, val action: String?)
    val people = remember {
        mutableStateListOf(
            Member(Sample.aisha, "Owner", null), Member(Sample.me.copy(name = "Vishal (you)"), "Editor", "Leave"),
            Member(Sample.meera, "Editor", "Remove"), Member(Sample.dev, "Invited · waiting", "Revoke"),
        )
    }
    ListScreen("Collaborators") {
        item {
            Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                ArtImage(Art.C, Modifier.size(56.dp), RoundedCornerShape(6.dp))
                Spacer(Modifier.width(16.dp))
                Column { T("Road Trip 2026", DType.BodyL.copy(fontWeight = FontWeight.SemiBold)); T("Public · 24 songs", DType.Meta, color = DColor.TextDim) }
            }
        }
        item { GroupLabel("Invite link") }
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(DColor.Elevated).padding(start = 16.dp, end = 4.dp).height(52.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Rounded.Link, null, tint = DColor.TextDim, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(10.dp))
                    T("dafli.link/i/rt26-x8k2", DType.BodyL.copy(fontWeight = FontWeight.Medium, fontSize = 15.sp), Modifier.weight(1f), maxLines = 1)
                    TextLink("Copy", Modifier.padding(end = 8.dp)) { nav.say("Invite link copied") }
                }
                T("Anyone with this link can join as an editor. Expires on 5 Oct.", DType.Small, Modifier.padding(top = 8.dp), color = DColor.TextDim)
            }
        }
        item { GroupLabel("People · ${people.size}") }
        people.forEach { m ->
            item {
                Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Avatar(m.p, 44.dp)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) { T(m.p.name, DType.BodyStrong); T(m.role, DType.Meta, color = DColor.TextDim) }
                    if (m.action != null) TextLink(m.action, color = if (m.action == "Leave") DColor.Error else DColor.Text) {
                        if (m.action == "Leave") { nav.back(); nav.say("You left Road Trip 2026") } else { people.remove(m); nav.say("${m.p.name} removed") }
                    }
                }
            }
        }
        item {
            Row(Modifier.padding(16.dp).fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(DColor.Surface).padding(16.dp)) {
                Icon(Icons.Rounded.Lock, null, tint = DColor.TextDim, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(10.dp))
                T("Editors can add, remove and reorder songs. Only the owner can change privacy, members or delete the playlist.", DType.Small, color = DColor.TextDim)
            }
        }
    }
}

// ------------------------------------------------------------------ S28 Folder

@Composable
fun FolderScreen() {
    val nav = LocalNav.current
    ListScreen(null, actions = { IconBtn(Icons.Rounded.MoreVert, "Folder options") { nav.say("Rename or delete folder") } }) {
        item {
            Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                IconTile(Icons.Rounded.Folder, DColor.Elevated, DColor.TextDim, 72.dp, 10.dp)
                Spacer(Modifier.width(16.dp))
                Column { T("Chill Evenings", DType.H3); T("Folder · 4 playlists", DType.Meta, color = DColor.TextDim) }
            }
        }
        item { Spacer(Modifier.height(12.dp)) }
        listOf(
            Collection("f1", "Rainy Day Lo-fi", "Playlist · Vishal", Art.LoFi),
            Collection("f2", "Morning Calm", "Playlist · Made for Vishal", Art.H),
            Collection("f3", "Evening Bhajans", "Playlist · Vishal", Art.Devotional),
            Collection("f4", "Nila on repeat", "Playlist · Vishal", Art.B),
        ).forEach { c -> item { LibraryRow(c.title, c.subtitle, c.art, onClick = { nav.go(Route.PlaylistPage(c)) }) } }
        item {
            Row(Modifier.fillMaxWidth().clickable { nav.say("Pick playlists to move here") }.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                IconTile(Icons.Rounded.Add, DColor.Surface, DColor.Text)
                Spacer(Modifier.width(12.dp))
                T("Move playlists here", DType.BodyStrong)
            }
        }
        item { Note("Deleting a folder keeps its playlists in your Library.") }
    }
}

// ------------------------------------------------------------------ S29 Downloads

@Composable
fun DownloadsScreen() {
    val nav = LocalNav.current
    var wifiOnly by rememberSaveable { mutableStateOf(true) }
    var retried by rememberSaveable { mutableStateOf(false) }
    ListScreen("Downloads") {
        item {
            Column(Modifier.padding(16.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(DColor.Surface).padding(16.dp)) {
                T("2.1 GB used by downloads", DType.BodyStrong)
                T("18.4 GB free on this phone", DType.Meta, color = DColor.TextDim)
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)).background(DColor.Elevated)) {
                    Box(Modifier.fillMaxHeight().weight(0.08f).background(DColor.Marigold))
                    Box(Modifier.fillMaxHeight().weight(0.6f).background(DColor.TextDim))
                    Box(Modifier.fillMaxHeight().weight(0.32f))
                }
                Row(Modifier.padding(top = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(8.dp).clip(CircleShape).background(DColor.Marigold)); T("  Dafli", DType.Tiny.copy(fontWeight = FontWeight.Normal), color = DColor.TextDim)
                    Spacer(Modifier.width(16.dp))
                    Box(Modifier.size(8.dp).clip(CircleShape).background(DColor.TextDim)); T("  Other apps", DType.Tiny.copy(fontWeight = FontWeight.Normal), color = DColor.TextDim)
                }
            }
        }
        item { com.dafli.app.ui.components.ToggleRow("Download on Wi-Fi only", "Saves your mobile data", wifiOnly) { wifiOnly = it } }
        item { GroupLabel("In progress · 2") }
        item { DlRow(Art.C, "Road Trip 2026", "14 of 24 songs · downloading", 0.58f) { nav.go(Route.PlaylistPage(Sample.roadTrip)) } }
        item { DlRow(Art.B, "Studio Nights", if (wifiOnly) "Paused · waiting for Wi-Fi" else "Downloading", if (wifiOnly) null else 0.1f) { nav.go(Route.AlbumPage(Collection("sn", "Studio Nights", "Album · Nila Raghavan", Art.B, Kind.Album))) } }
        if (!retried) {
            item { GroupLabel("Needs attention") }
            item {
                Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    ArtImage(Art.C, Modifier.size(52.dp), RoundedCornerShape(6.dp))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        T("Neon Bandra", DType.BodyStrong)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.ErrorOutline, null, tint = DColor.Error, modifier = Modifier.size(14.dp)); Spacer(Modifier.width(4.dp))
                            T("2 songs failed · partly ready", DType.Small, color = DColor.TextDim)
                        }
                    }
                    TextLink("Retry") { retried = true; nav.say("Retrying 2 songs") }
                }
            }
        }
        item { GroupLabel("Ready to play offline") }
        item { DlDone(Art.A, "Marigold Hours", "Album · 10 songs · 92 MB") { nav.go(Route.AlbumPage(Sample.marigoldAlbum)) } }
        item { DlDone(Art.J, "Noor", "Song · 9 MB") { nav.go(Route.NowPlaying) } }
        item { DlDone(Art.E, "Chai & Rain", "Song · 8 MB") { nav.go(Route.NowPlaying) } }
    }
}

@Composable
private fun DlRow(art: Art, title: String, sub: String, progress: Float?, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        ArtImage(art, Modifier.size(52.dp), RoundedCornerShape(6.dp))
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            T(title, DType.BodyStrong)
            T(sub, DType.Small, color = DColor.TextDim)
            if (progress != null) Box(Modifier.padding(top = 6.dp).fillMaxWidth().height(3.dp).clip(RoundedCornerShape(2.dp)).background(DColor.Elevated)) {
                Box(Modifier.fillMaxWidth(progress).height(3.dp).background(DColor.Marigold))
            }
        }
        Spacer(Modifier.width(8.dp))
        Icon(if (progress == null) Icons.Rounded.CloudOff else Icons.Rounded.Pause, null, tint = DColor.TextDim)
    }
}

@Composable
private fun DlDone(art: Art, title: String, sub: String, onClick: () -> Unit) {
    LibraryRow(title, sub, art, subLeading = { Icon(Icons.Rounded.CheckCircle, "Downloaded", tint = DColor.Success, modifier = Modifier.size(14.dp)) }, onClick = onClick)
}

// ------------------------------------------------------------------ S43 Local files

@Composable
fun LocalFilesScreen() {
    val nav = LocalNav.current
    val p = LocalPlayer.current
    var allowed by rememberSaveable { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().background(DColor.Night)) {
        TopBar("Local files")
        if (allowed) {
            LazyColumn(Modifier.weight(1f)) {
                item { Note("3 songs found on this phone") }
                listOf(Sample.baarish, Sample.shamDhale, Sample.kabira).forEach { t -> item { TrackRow(t.copy(artist = "${t.artist} · MP3"), onClick = { p.play(t, "Local files") }) } }
            }
        } else {
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp)) {
                Spacer(Modifier.height(48.dp))
                Box(Modifier.size(88.dp).clip(RoundedCornerShape(20.dp)).background(DColor.Surface).align(Alignment.CenterHorizontally), contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.UploadFile, null, tint = DColor.Text, modifier = Modifier.size(40.dp))
                }
                Spacer(Modifier.height(32.dp))
                T("Play music saved on your phone", DType.H3, Modifier.fillMaxWidth(), align = TextAlign.Center)
                T("Allow access to audio files and they will show up here, next to your playlists.", DType.Body, Modifier.fillMaxWidth().padding(top = 12.dp, start = 16.dp, end = 16.dp), color = DColor.TextDim, align = TextAlign.Center)
                Spacer(Modifier.height(28.dp))
                Bullet(Icons.Rounded.PhoneAndroid, "Files stay on this phone. We never upload them.")
                Bullet(Icons.Rounded.VisibilityOff, "Songs from local files are not shared with your friends.")
                Bullet(Icons.Rounded.RemoveCircle, "Removing a file here does not delete it from your phone.")
            }
            Column(Modifier.padding(16.dp).navigationBarsPadding()) {
                PrimaryButton("Allow access") { allowed = true; nav.say("Access allowed") }
                Spacer(Modifier.height(12.dp))
                SecondaryButton("Not now") { nav.back() }
            }
        }
    }
}

@Composable
private fun Bullet(icon: ImageVector, text: String) {
    Row(Modifier.padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = DColor.TextDim, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(16.dp))
        T(text, DType.Meta, color = DColor.TextDim)
    }
}
