package com.dafli.app.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.QrCode2
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Share
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dafli.app.data.Art
import com.dafli.app.data.Person
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
import com.dafli.app.ui.components.GroupLabel
import com.dafli.app.ui.components.IconBtn
import com.dafli.app.ui.components.LocalChromePad
import com.dafli.app.ui.components.LocalNav
import com.dafli.app.ui.components.LocalPlayer
import com.dafli.app.ui.components.Note
import com.dafli.app.ui.components.PrimaryButton
import com.dafli.app.ui.components.SecondaryButton
import com.dafli.app.ui.components.T
import com.dafli.app.ui.components.TextLink
import com.dafli.app.ui.components.ToggleRow
import com.dafli.app.ui.components.TopBar
import com.dafli.app.ui.components.TrackRow

// ------------------------------------------------------------------ S42 Listen Together

@Composable
fun ListenTogetherScreen() {
    val nav = LocalNav.current
    val p = LocalPlayer.current
    var guestsAdd by rememberSaveable { mutableStateOf(true) }
    val queue = listOf(Triple(Sample.bassWala, "Aisha", "A"), Triple(Sample.paadalNights, "Meera", "M"), Triple(Sample.marigoldHours, "you", "V"))
    ListScreen("Listen Together", centerTitle = true) {
        item {
            Row(
                Modifier.padding(16.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(DColor.Surface).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ArtImage(p.current.art, Modifier.size(84.dp), RoundedCornerShape(8.dp))
                Spacer(Modifier.width(16.dp))
                Column {
                    T("YOU’RE HOSTING", DType.Tiny.copy(fontWeight = FontWeight.SemiBold), color = DColor.TextDim)
                    T(p.current.title, DType.TitleS.copy(fontWeight = FontWeight.Bold), Modifier.padding(top = 4.dp))
                    T(p.current.artist, DType.Meta, color = DColor.TextDim)
                    Row(Modifier.padding(top = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(8.dp).clip(CircleShape).background(DColor.Success)); Spacer(Modifier.width(6.dp))
                        T("Playing for 4 people", DType.Small.copy(fontWeight = FontWeight.Medium), color = DColor.Success)
                    }
                }
            }
        }
        item { GroupLabel("People") }
        item {
            Row(Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                listOf(Sample.me to "You", Sample.aisha to "Aisha", Sample.meera to "Meera", Sample.dev to "Dev").forEach { (person, label) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { if (label != "You") nav.go(Route.OtherProfile(person)) }) {
                        Avatar(person, 52.dp); T(label, DType.Small.copy(fontWeight = FontWeight.Medium), Modifier.padding(top = 6.dp))
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { nav.open(Sheet.Share("Listen Together", "Session link", p.current.art)) }) {
                    Box(Modifier.size(52.dp).clip(CircleShape).border(1.5.dp, DColor.Line, CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Rounded.Add, null, tint = DColor.Text) }
                    T("Invite", DType.Small.copy(fontWeight = FontWeight.Medium), Modifier.padding(top = 6.dp))
                }
            }
        }
        item {
            Row(
                Modifier.padding(16.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(DColor.Surface).padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(Modifier.size(64.dp).clip(RoundedCornerShape(8.dp)).background(DColor.Paper), contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.QrCode2, "QR code", tint = DColor.Night, modifier = Modifier.size(52.dp))
                }
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f)) {
                    T("Scan or share the link", DType.BodyStrong)
                    T("Link works for 1 hour", DType.Small, color = DColor.TextDim)
                }
                TextLink("Copy") { nav.say("Session link copied") }
            }
        }
        item { ToggleRow("Guests can add songs", "You still control play and pause", guestsAdd) { guestsAdd = it } }
        item { GroupLabel("Shared queue") }
        queue.forEach { (t, who, initial) ->
            item { TrackRow(t, subtitle = "Added by $who · ${t.artist}", trailing = { Box(Modifier.padding(end = 16.dp)) { AddedByBadge(initial) } }) }
        }
        item { SecondaryButton("End session for everyone", Modifier.padding(16.dp), danger = true) { nav.back(); nav.say("Session ended") } }
    }
}

// ------------------------------------------------------------------ S44 Shared mix

@Composable
fun SharedMixScreen() {
    val nav = LocalNav.current
    val p = LocalPlayer.current
    val rows = listOf(Sample.chaiRain to "V", Sample.neonBandra to "A", Sample.rooftopNights to "A", Sample.studioNights to "V", Sample.bassWala to "A")
    LazyColumn(Modifier.fillMaxSize().background(DColor.Night), contentPadding = PaddingValues(bottom = LocalChromePad.current + 16.dp)) {
        item {
            Box(Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(Color(0xFF233A36), DColor.Night)))) {
                Column {
                    TopBar(null, background = Color.Transparent)
                    Box(Modifier.fillMaxWidth().height(140.dp), contentAlignment = Alignment.Center) {
                        Box(Modifier.width(160.dp).height(112.dp)) {
                            Avatar(Sample.me, 88.dp, Modifier.border(3.dp, DColor.Night, CircleShape))
                            Avatar(Sample.aisha, 88.dp, Modifier.align(Alignment.BottomEnd).border(3.dp, DColor.Night, CircleShape))
                        }
                    }
                    Column(Modifier.padding(horizontal = 16.dp)) {
                        T("SHARED MIX", DType.Tiny.copy(fontWeight = FontWeight.SemiBold), color = DColor.TextDim)
                        T("Vishal + Aisha", DType.H2)
                        T("Songs you both play, plus new ones for both of you. Updated daily.", DType.Label.copy(fontWeight = FontWeight.Normal), color = DColor.TextDim, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }
        item {
            Row(Modifier.padding(start = 4.dp, end = 16.dp, top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                IconBtn(Icons.Rounded.Share, "Share", tint = DColor.TextDim) { nav.open(Sheet.Share("Vishal + Aisha", "Shared mix", Art.J)) }
                IconBtn(Icons.Rounded.Refresh, "Refresh mix", tint = DColor.TextDim) { nav.say("Mix refreshes daily") }
                Spacer(Modifier.weight(1f))
                PlayFab("Vishal + Aisha", rows.first().first)
            }
        }
        rows.forEach { (t, who) ->
            item { TrackRow(t, onClick = { p.play(t, "Vishal + Aisha") }, trailing = { Box(Modifier.padding(end = 16.dp)) { AddedByBadge(who) } }) }
        }
        item { Note("Aisha only sees songs in this mix, not your full listening history.") }
    }
}

// ------------------------------------------------------------------ S45 Recap story

@Composable
fun RecapScreen() {
    val nav = LocalNav.current
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) { progress.animateTo(1f, tween(8000, easing = LinearEasing)) }
    Box(Modifier.fillMaxSize().background(DColor.Night)) {
        ArtImage(Art.KabirSen, Modifier.fillMaxSize(), RoundedCornerShape(0.dp))
        Box(Modifier.fillMaxSize().background(Brush.verticalGradient(0f to DColor.Night.copy(alpha = 0.7f), 0.3f to Color.Transparent, 0.55f to DColor.Night.copy(alpha = 0.6f), 1f to DColor.Night)))
        Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
            Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(5) { i ->
                    Box(Modifier.weight(1f).height(3.dp).clip(RoundedCornerShape(2.dp)).background(DColor.Text.copy(alpha = 0.3f))) {
                        val f = when { i < 1 -> 1f; i == 1 -> progress.value; else -> 0f }
                        Box(Modifier.fillMaxWidth(f).height(3.dp).background(DColor.Text))
                    }
                }
            }
            Row(Modifier.padding(start = 16.dp, end = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                T("Your September", DType.BodyStrong, Modifier.weight(1f))
                IconBtn(Icons.Rounded.Close, "Close recap") { nav.back() }
            }
            T("Your top artist", DType.BodyL, Modifier.padding(start = 24.dp, top = 40.dp), color = DColor.Text)
            Spacer(Modifier.weight(1f))
            Column(Modifier.padding(horizontal = 24.dp)) {
                T("Kabir Sen", DType.Display)
                T("You played him for 612 minutes this month.\nThat’s about 20 minutes every day.", DType.BodyL.copy(fontWeight = FontWeight.Normal), Modifier.padding(top = 8.dp), color = DColor.TextDim)
                T("Based on your listening from 1–27 Sep.", DType.Small, Modifier.padding(top = 16.dp), color = DColor.TextDim)
            }
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                PrimaryButton("Share this card", Modifier.weight(1f)) { nav.open(Sheet.Share("Your September", "Recap · Kabir Sen", Art.KabirSen)) }
                Spacer(Modifier.width(12.dp))
                TextLink("View as list", style = DType.Label) { nav.go(Route.History) }
            }
        }
    }
}

// ------------------------------------------------------------------ S46 Create with AI

@Composable
fun CreateWithAiScreen() {
    val nav = LocalNav.current
    val p = LocalPlayer.current
    var prompt by rememberSaveable { mutableStateOf("Rainy evening in Mumbai, old Hindi songs, soft and slow") }
    val songs = remember { mutableStateListOf(Sample.baarish, Sample.chaiRain, Sample.ghatKiSubah, Sample.shamDhale, Sample.noor) }
    var chip by rememberSaveable { mutableIntStateOf(-1) }
    Column(Modifier.fillMaxSize().background(DColor.Night).imePadding()) {
        TopBar("Create with AI")
        LazyColumn(Modifier.weight(1f)) {
            item {
                Column(Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(DColor.Elevated).border(1.dp, DColor.Line, RoundedCornerShape(16.dp)).padding(16.dp),
                        verticalAlignment = Alignment.Top,
                    ) {
                        Icon(Icons.Rounded.AutoAwesome, null, tint = DColor.Marigold, modifier = Modifier.size(22.dp))
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            androidx.compose.foundation.text.BasicTextField(
                                prompt, { if (it.length <= 300) prompt = it }, textStyle = DType.BodyL.copy(color = DColor.Text),
                                cursorBrush = androidx.compose.ui.graphics.SolidColor(DColor.Marigold), modifier = Modifier.fillMaxWidth(),
                            )
                            T("${prompt.length} / 300", DType.Tiny.copy(fontWeight = FontWeight.Normal), Modifier.align(Alignment.End).padding(top = 8.dp), color = DColor.TextDim)
                        }
                    }
                }
            }
            item {
                Column {
                    Spacer(Modifier.height(12.dp))
                    ChipRow(listOf("90s romance", "Sufi at night", "Gym Punjabi"), chip) {
                        chip = it; prompt = listOf("90s romance, Kumar-era duets, feel-good", "Sufi at night, qawwali and slow ghazals", "Gym Punjabi, high BPM bhangra and desi hip-hop")[it]
                    }
                }
            }
            item { GroupLabel("Your playlist") }
            item {
                Column(Modifier.padding(horizontal = 16.dp)) {
                    T("Rainy Bombay Evenings", DType.Title.copy(fontSize = 22.sp))
                    T("${songs.size * 4} songs · 1 h 18 min · all real songs from the catalogue", DType.Meta, color = DColor.TextDim)
                }
            }
            items(songs, key = { it.id }) { t ->
                TrackRow(t, onClick = { p.play(t, "Rainy Bombay Evenings") }, trailing = {
                    IconBtn(Icons.Rounded.Close, "Remove ${t.title}", tint = DColor.TextDim, size = 20.dp) { songs.remove(t) }
                })
            }
            item { Note("Remove songs you don’t want before saving.") }
        }
        Row(Modifier.padding(16.dp).navigationBarsPadding(), verticalAlignment = Alignment.CenterVertically) {
            SecondaryButton("Refine", Modifier.width(120.dp)) { nav.say("Updating the list…") }
            Spacer(Modifier.width(12.dp))
            PrimaryButton("Save playlist", Modifier.weight(1f), enabled = songs.isNotEmpty()) {
                nav.back(); nav.say("Saved “Rainy Bombay Evenings” to Your Library")
            }
        }
    }
}

// ------------------------------------------------------------------ S47 Messages

private data class Thread(val person: Person, val last: String, val time: String, val unread: Int)

@Composable
fun MessagesScreen() {
    val nav = LocalNav.current
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val threads = listOf(
        Thread(Sample.aisha, "Sent a song: Bass Wala Pyaar", "2m", 2),
        Thread(Sample.meera, "You: added 3 songs to Road Trip", "1h", 0),
        Thread(Sample.dev, "Sent a playlist: Indie Nights", "Tue", 0),
        Thread(Sample.kiran, "haha this one is perfect for Goa", "Mon", 0),
    )
    ListScreen("Messages", actions = { IconBtn(Icons.Rounded.Edit, "New message") { nav.say("Pick a friend to message") } }) {
        item { ChipRow(listOf("Chats", "Requests · 2"), tab) { tab = it } }
        item { Spacer(Modifier.height(8.dp)) }
        if (tab == 0) threads.forEach { th ->
            item {
                Row(Modifier.fillMaxWidth().clickable { nav.go(Route.Chat(th.person)) }.padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Avatar(th.person, 52.dp)
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        T(th.person.name, DType.BodyL.copy(fontWeight = if (th.unread > 0) FontWeight.Bold else FontWeight.SemiBold))
                        T(th.last, DType.Meta, color = if (th.unread > 0) DColor.Text else DColor.TextDim, maxLines = 1)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        T(th.time, DType.Small, color = DColor.TextDim)
                        if (th.unread > 0) Box(Modifier.padding(top = 4.dp).size(20.dp).clip(CircleShape).background(DColor.Info), contentAlignment = Alignment.Center) {
                            T("${th.unread}", DType.Tiny.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp), color = DColor.Night)
                        }
                    }
                }
            }
        } else {
            listOf(Person("Rohan", "@rohan.beats", "R", DColor.AvatarD), Person("Sana", "@sana", "S", DColor.AvatarM)).forEach { r ->
                item {
                    Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Avatar(r, 52.dp); Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) { T(r.name, DType.BodyL.copy(fontWeight = FontWeight.SemiBold)); T("Wants to send you messages", DType.Meta, color = DColor.TextDim) }
                        TextLink("Accept") { nav.say("You can now chat with ${r.name}") }
                    }
                }
            }
        }
        item { Note("Only people you accept can message you. You can block anyone from their profile.") }
    }
}

// ------------------------------------------------------------------ S48 Conversation

private sealed interface Msg { val mine: Boolean }
private data class TextMsg(val text: String, override val mine: Boolean) : Msg
private data class SongMsg(val track: Track, override val mine: Boolean) : Msg
private data class ListMsg(val title: String, val sub: String, val art: Art, override val mine: Boolean) : Msg

@Composable
fun ChatScreen(person: Person) {
    val nav = LocalNav.current
    val p = LocalPlayer.current
    val msgs = remember {
        mutableStateListOf<Msg>(
            TextMsg("Perfect song for the drive", false), SongMsg(Sample.bassWala, false),
            TextMsg("Adding it to the trip list right now", true), ListMsg("Road Trip 2026", "Playlist · 25 songs", Art.C, true),
            TextMsg("Goa is going to be so good", false),
        )
    }
    var draft by rememberSaveable { mutableStateOf("") }
    Column(Modifier.fillMaxSize().background(DColor.Night).imePadding()) {
        Row(Modifier.statusBarsPadding().padding(horizontal = 4.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            IconBtn(Icons.AutoMirrored.Rounded.ArrowBack, "Back") { nav.back() }
            Row(Modifier.clip(RoundedCornerShape(12.dp)).clickable { nav.go(Route.OtherProfile(person)) }.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
                Avatar(person, 36.dp); Spacer(Modifier.width(10.dp))
                Column { T(person.name, DType.BodyL.copy(fontWeight = FontWeight.SemiBold)); T(person.handle, DType.Small, color = DColor.TextDim) }
            }
        }
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item { T("Today", DType.Small.copy(fontWeight = FontWeight.Medium), Modifier.fillMaxWidth(), color = DColor.TextDim, align = androidx.compose.ui.text.style.TextAlign.Center) }
            items(msgs) { m ->
                Box(Modifier.fillMaxWidth(), contentAlignment = if (m.mine) Alignment.CenterEnd else Alignment.CenterStart) {
                    val shape = RoundedCornerShape(18.dp, 18.dp, if (m.mine) 4.dp else 18.dp, if (m.mine) 18.dp else 4.dp)
                    when (m) {
                        is TextMsg -> T(
                            m.text, DType.Body,
                            Modifier.widthIn(max = 280.dp).clip(shape).background(if (m.mine) DColor.Elevated else DColor.Surface).padding(horizontal = 14.dp, vertical = 10.dp),
                        )
                        is SongMsg -> Row(
                            Modifier.widthIn(max = 280.dp).clip(shape).background(DColor.Surface).clickable { p.play(m.track, "Chat with ${person.name}") }.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            ArtImage(m.track.art, Modifier.size(56.dp), RoundedCornerShape(6.dp)); Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f, fill = false)) { T(m.track.title, DType.BodyStrong); T("Song · ${m.track.artist}", DType.Small, color = DColor.TextDim) }
                            Spacer(Modifier.width(12.dp))
                            Icon(Icons.Rounded.MusicNote, "Play", tint = DColor.Marigold)
                        }
                        is ListMsg -> Row(
                            Modifier.widthIn(max = 280.dp).clip(shape).background(DColor.Elevated).clickable { nav.go(Route.PlaylistPage(Sample.roadTrip)) }.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            ArtImage(m.art, Modifier.size(56.dp), RoundedCornerShape(6.dp)); Spacer(Modifier.width(12.dp))
                            Column { T(m.title, DType.BodyStrong); T(m.sub, DType.Small, color = DColor.TextDim) }
                        }
                    }
                }
            }
            item { T("12:42 · Seen", DType.Tiny.copy(fontWeight = FontWeight.Normal), Modifier.fillMaxWidth(), color = DColor.TextDim, align = androidx.compose.ui.text.style.TextAlign.End) }
        }
        Row(Modifier.padding(horizontal = 12.dp, vertical = 8.dp).navigationBarsPadding(), verticalAlignment = Alignment.CenterVertically) {
            IconBtn(Icons.Rounded.MusicNote, "Share a song", tint = DColor.TextDim) { msgs.add(SongMsg(p.current, true)) }
            Box(Modifier.weight(1f).height(48.dp).clip(RoundedCornerShape(24.dp)).background(DColor.Elevated).padding(horizontal = 16.dp), contentAlignment = Alignment.CenterStart) {
                if (draft.isEmpty()) T("Message", DType.Body, color = DColor.Disabled)
                androidx.compose.foundation.text.BasicTextField(
                    draft, { draft = it }, singleLine = true, textStyle = DType.Body.copy(color = DColor.Text),
                    cursorBrush = androidx.compose.ui.graphics.SolidColor(DColor.Marigold), modifier = Modifier.fillMaxWidth(),
                )
            }
            IconBtn(Icons.AutoMirrored.Rounded.Send, "Send", tint = if (draft.isBlank()) DColor.Disabled else DColor.Marigold) {
                if (draft.isNotBlank()) { msgs.add(TextMsg(draft.trim(), true)); draft = "" }
            }
        }
    }
}

// ------------------------------------------------------------------ S53 Your taste

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun YourTasteScreen() {
    val nav = LocalNav.current
    val hidden = remember { mutableStateListOf("Chal Hun" to "Song · hidden from recommendations", "Old Bombay Orchestra" to "Artist · recommended less") }
    var bhajansIncluded by rememberSaveable { mutableStateOf(false) }
    ListScreen("Your taste", actions = { TextLink("Edit", Modifier.padding(end = 12.dp)) { nav.go(Route.Preferences) } }) {
        item { T("What we use to pick music for you.", DType.Label.copy(fontWeight = FontWeight.Normal), Modifier.padding(horizontal = 16.dp), color = DColor.TextDim) }
        item { GroupLabel("Languages") }
        item {
            FlowRow(Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Hindi", "Punjabi", "English").forEach {
                    Box(Modifier.height(36.dp).clip(RoundedCornerShape(18.dp)).background(DColor.Elevated).padding(horizontal = 16.dp), contentAlignment = Alignment.Center) { T(it, DType.Label) }
                }
            }
        }
        item { GroupLabel("Artists you picked") }
        item {
            Row(Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                listOf(Sample.kabirSen, Sample.nilaRaghavan, Sample.mcVeer).forEach { a ->
                    Column(Modifier.width(80.dp).clickable { nav.go(Route.ArtistPage(a)) }, horizontalAlignment = Alignment.CenterHorizontally) {
                        ArtImage(a.art, Modifier.size(72.dp), CircleShape)
                        T(a.name, DType.Small.copy(fontWeight = FontWeight.SemiBold), Modifier.padding(top = 6.dp), maxLines = 1)
                    }
                }
            }
        }
        item { GroupLabel("Hidden") }
        hidden.forEach { h ->
            item {
                Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    ArtImage(if (h.first == "Chal Hun") Art.Workout else Art.Retro, Modifier.size(48.dp), if (h.second.startsWith("Artist")) CircleShape else RoundedCornerShape(6.dp))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) { T(h.first, DType.BodyStrong); T(h.second, DType.Meta, color = DColor.TextDim) }
                    TextLink("Undo") { hidden.remove(h); nav.say("${h.first} can be recommended again") }
                }
            }
        }
        if (hidden.isEmpty()) item { Note("Nothing hidden.") }
        item { GroupLabel("Not used for your taste") }
        item {
            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                ArtImage(Art.Devotional, Modifier.size(48.dp), RoundedCornerShape(6.dp))
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) { T("Evening Bhajans", DType.BodyStrong); T(if (bhajansIncluded) "Playlist · included" else "Playlist · excluded", DType.Meta, color = DColor.TextDim) }
                TextLink(if (bhajansIncluded) "Exclude" else "Include") { bhajansIncluded = !bhajansIncluded }
            }
        }
        item { SecondaryButton("Reset recommendations", Modifier.padding(16.dp), danger = true) { nav.say("Recommendations reset") } }
        item { Note("Reset clears what we learned. Your library and history stay.") }
    }
}

