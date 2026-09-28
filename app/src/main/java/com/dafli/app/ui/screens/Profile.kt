package com.dafli.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Chat
import androidx.compose.material.icons.automirrored.rounded.HelpOutline
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import com.dafli.app.data.Art
import com.dafli.app.data.Collection
import com.dafli.app.data.Person
import com.dafli.app.data.Sample
import com.dafli.app.nav.Route
import com.dafli.app.nav.Sheet
import com.dafli.app.theme.DColor
import com.dafli.app.theme.DType
import com.dafli.app.ui.components.Avatar
import com.dafli.app.ui.components.Divider
import com.dafli.app.ui.components.GroupLabel
import com.dafli.app.ui.components.IconBtn
import com.dafli.app.ui.components.LibraryRow
import com.dafli.app.ui.components.LocalNav
import com.dafli.app.ui.components.LocalPlayer
import com.dafli.app.ui.components.Note
import com.dafli.app.ui.components.PillButton
import com.dafli.app.ui.components.SearchField
import com.dafli.app.ui.components.SecondaryButton
import com.dafli.app.ui.components.SettingsRow
import com.dafli.app.ui.components.T
import com.dafli.app.ui.components.TextLink
import com.dafli.app.ui.components.ToggleRow

@Composable
private fun ProfileHeader(p: Person, big: String, followers: Int, following: Int, action: @Composable () -> Unit) {
    Column(Modifier.padding(horizontal = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Avatar(p, 100.dp)
            Spacer(Modifier.width(20.dp))
            Column(Modifier.weight(1f)) {
                T(big, DType.H2.copy(fontSize = if (big.length > 12) 26.sp else 32.sp), maxLines = 2)
                T(p.handle, DType.Label.copy(fontWeight = FontWeight.Normal), color = DColor.TextDim)
            }
        }
        Row(Modifier.padding(top = 20.dp), verticalAlignment = Alignment.CenterVertically) {
            T("$followers", DType.BodyStrong.copy(fontWeight = FontWeight.Bold)); T(" followers", DType.Label.copy(fontWeight = FontWeight.Normal), color = DColor.TextDim)
            Spacer(Modifier.width(20.dp))
            T("$following", DType.BodyStrong.copy(fontWeight = FontWeight.Bold)); T(" following", DType.Label.copy(fontWeight = FontWeight.Normal), color = DColor.TextDim)
        }
        Spacer(Modifier.height(16.dp))
        action()
    }
}

// ------------------------------------------------------------------ S32 My profile

@Composable
fun MyProfileScreen() {
    val nav = LocalNav.current
    ListScreen(null, actions = { IconBtn(Icons.Rounded.Settings, "Settings") { nav.go(Route.Settings) } }) {
        item { ProfileHeader(Sample.me, Sample.me.name, 12, 30) { PillButton("Edit profile") { nav.say("Edit name and photo") } } }
        item { Spacer(Modifier.height(20.dp)) }
        item { SettingsRow("Recently played", "Songs and artists you played", icon = Icons.Rounded.History, onClick = { nav.go(Route.History) }) }
        item {
            SettingsRow("Messages", "2 new", icon = Icons.AutoMirrored.Rounded.Chat, onClick = { nav.go(Route.Messages) }) {
                Box(Modifier.size(22.dp).clip(CircleShape).background(DColor.Info), contentAlignment = Alignment.Center) {
                    T("2", DType.Tiny.copy(fontWeight = FontWeight.Bold), color = DColor.Night)
                }
            }
        }
        item { SettingsRow("Your September recap", "Ready", icon = Icons.Rounded.PlayCircle, onClick = { nav.go(Route.Recap) }) }
        item { SettingsRow("Your taste", "Languages, artists, hidden songs", icon = Icons.Rounded.Tune, onClick = { nav.go(Route.YourTaste) }) }
        item { Column { Spacer(Modifier.height(16.dp)); T("Public playlists", DType.Title, Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) } }
        listOf(
            Collection("rt", "Road Trip 2026", "24 songs · 3 saves", Art.C),
            Collection("f1", "Rainy Day Lo-fi", "41 songs", Art.LoFi),
            Collection("s90", "90s Sunday", "55 songs", Art.Retro),
        ).forEach { c -> item { LibraryRow(c.title, c.subtitle, c.art, onClick = { nav.go(Route.PlaylistPage(if (c.id == "rt") Sample.roadTrip else c)) }) } }
        item { Note("Only public playlists appear here. Your history and private playlists are never shown.") }
    }
}

// ------------------------------------------------------------------ S33 Other profile

@Composable
fun OtherProfileScreen(person: Person) {
    val nav = LocalNav.current
    var follow by rememberSaveable { mutableStateOf(false) }
    ListScreen(null, actions = {
        IconBtn(Icons.AutoMirrored.Rounded.Chat, "Message ${person.name}") { nav.go(Route.Chat(person)) }
        IconBtn(Icons.Rounded.MoreVert, "More") { nav.open(Sheet.Report("Road Trip 2026", person.name)) }
    }) {
        item {
            ProfileHeader(person, person.name, 84 + if (follow) 1 else 0, 61) {
                PillButton(if (follow) "Following" else "Follow", filled = follow) { follow = !follow }
            }
        }
        item { Column { Spacer(Modifier.height(24.dp)); T("Public playlists", DType.Title, Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) } }
        listOf(
            Collection("rt", "Road Trip 2026", "24 songs · you are an editor", Art.C),
            Collection("kf", "Kabir & friends", "30 songs", Art.E),
            Collection("in", "Indie Nights", "48 songs", Art.Indie),
            Collection("mr", "Morning Run", "26 songs", Art.Workout),
        ).forEach { c -> item { LibraryRow(c.title, c.subtitle, c.art, onClick = { nav.go(Route.PlaylistPage(if (c.id == "rt") Sample.roadTrip else c)) }) } }
        item {
            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PillButton("Report", icon = Icons.Rounded.Flag) { nav.open(Sheet.Report("Road Trip 2026", person.name)) }
                PillButton("Block", icon = Icons.Rounded.Block) { nav.say("${person.name} blocked") }
            }
        }
    }
}

// ------------------------------------------------------------------ S34 Settings

@Composable
fun SettingsScreen() {
    val nav = LocalNav.current
    ListScreen("Settings") {
        item {
            Row(
                Modifier.padding(16.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(DColor.Surface)
                    .clickable { nav.go(Route.MyProfile) }.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Avatar(Sample.me, 52.dp)
                Spacer(Modifier.width(14.dp))
                Column {
                    T(Sample.me.name, DType.BodyL.copy(fontWeight = FontWeight.SemiBold))
                    T("${Sample.email} · Free, no ads", DType.Meta, color = DColor.TextDim, maxLines = 1)
                }
            }
        }
        item { GroupLabel("Listening") }
        item { SettingsRow("Playback", "Autoplay on · Crossfade 4 s", icon = Icons.Rounded.PlayCircle, onClick = { nav.go(Route.Playback) }) }
        item { SettingsRow("Audio, downloads & storage", "High on Wi-Fi · 2.1 GB used", icon = Icons.Rounded.Download, onClick = { nav.go(Route.AudioStorage) }) }
        item { SettingsRow("Notifications", "New releases, invites", icon = Icons.Rounded.Notifications, onClick = { nav.go(Route.Updates) }) }
        item { GroupLabel("You") }
        item { SettingsRow("Privacy & social", "Profile public · Sharing off", icon = Icons.Rounded.Lock, onClick = { nav.go(Route.Privacy) }) }
        item { SettingsRow("Language & appearance", "English · System theme", icon = Icons.Rounded.Language, onClick = { nav.say("Choose app language and theme") }) }
        item { SettingsRow("Account & security", "Email, login, devices", icon = Icons.Rounded.Security, onClick = { nav.go(Route.Account) }) }
        item { GroupLabel("Support") }
        item { SettingsRow("Help & feedback", icon = Icons.AutoMirrored.Rounded.HelpOutline, onClick = { nav.go(Route.Help) }) }
        item { SettingsRow("About", value = "Version 1.0.0", icon = Icons.Rounded.Info, chevron = false) }
        item { SecondaryButton("Log out", Modifier.padding(16.dp), danger = true) { nav.resetTo(Route.Welcome, forward = false) } }
    }
}

// ------------------------------------------------------------------ S35 Playback

@Composable
fun PlaybackScreen() {
    val p = LocalPlayer.current
    var gapless by rememberSaveable { mutableStateOf(true) }
    var fade by rememberSaveable { mutableFloatStateOf(4f) }
    var normalise by rememberSaveable { mutableStateOf(true) }
    var explicit by rememberSaveable { mutableStateOf(true) }
    var motion by rememberSaveable { mutableStateOf(true) }
    val nav = LocalNav.current
    ListScreen("Playback") {
        item { GroupLabel("Continuous play") }
        item { val lib = com.dafli.app.ui.components.LocalLib.current; ToggleRow("Autoplay similar songs", "When your music ends", lib.autoplay) { lib.setAutoplayPref(it) } }
        item { ToggleRow("Gapless playback", "No silence between songs", gapless) { gapless = it } }
        item { GroupLabel("Crossfade") }
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                Row { T("Crossfade", DType.BodyStrong, Modifier.weight(1f)); T("${fade.toInt()} s", DType.BodyStrong, color = DColor.TextDim) }
                SeekBar(fade / 12f, { fade = (it * 12f).roundToInt().toFloat() }, Modifier.padding(vertical = 8.dp))
                Row { T("0 s", DType.Tiny.copy(fontWeight = FontWeight.Normal), Modifier.weight(1f), color = DColor.TextDim); T("12 s", DType.Tiny.copy(fontWeight = FontWeight.Normal), color = DColor.TextDim) }
            }
        }
        item { GroupLabel("Sound") }
        item { ToggleRow("Normalise volume", "Keeps songs at a similar loudness", normalise) { normalise = it } }
        item { SettingsRow("Equaliser", "Opens your phone’s equaliser", onClick = { nav.say("Opens system equaliser") }) }
        item { GroupLabel("Content") }
        item { ToggleRow("Allow explicit content", "Songs marked E", explicit) { explicit = it } }
        item { ToggleRow("Artwork motion", "Off when Reduce Motion is on", motion) { motion = it } }
        item { Note("Changes apply from the next song.") }
    }
}

// ------------------------------------------------------------------ S36 Audio, downloads & storage

@Composable
fun AudioStorageScreen() {
    val nav = LocalNav.current
    var wifi by rememberSaveable { mutableIntStateOf(1) }
    var mobile by rememberSaveable { mutableIntStateOf(1) }
    var wifiOnly by rememberSaveable { mutableStateOf(true) }
    var cache by rememberSaveable { mutableIntStateOf(300) }
    ListScreen("Audio & storage") {
        item { GroupLabel("Streaming on Wi-Fi") }
        listOf("Normal" to "About 96 kbit/s", "High" to "About 160 kbit/s", "Very high" to "About 320 kbit/s").forEachIndexed { i, (a, b) ->
            item { RadioRow(a, b, wifi == i) { wifi = i } }
        }
        item { GroupLabel("Streaming on mobile data") }
        listOf("Data saver" to "Lowest data use", "Normal" to "About 96 kbit/s", "High" to "Uses more data").forEachIndexed { i, (a, b) ->
            item { RadioRow(a, b, mobile == i) { mobile = i } }
        }
        item { GroupLabel("Downloads") }
        item { SettingsRow("Download quality", value = "High", onClick = { nav.say("Normal · High · Very high") }) }
        item { ToggleRow("Download on Wi-Fi only", null, wifiOnly) { wifiOnly = it } }
        item { GroupLabel("Storage") }
        item {
            Column(Modifier.padding(horizontal = 16.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(DColor.Surface).padding(16.dp)) {
                Row(Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)).background(DColor.Elevated)) {
                    Box(Modifier.height(8.dp).weight(1.8f).background(DColor.Marigold))
                    Box(Modifier.height(8.dp).weight(0.3f + cache / 10000f).background(DColor.Info))
                    Box(Modifier.height(8.dp).weight(18.4f))
                }
                Spacer(Modifier.height(12.dp))
                listOf(Triple(DColor.Marigold, "Downloads", "1.8 GB"), Triple(DColor.Info, "Cache", "$cache MB"), Triple(DColor.Elevated, "Free", "18.4 GB")).forEach { (c, a, b) ->
                    Row(Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(10.dp).clip(RoundedCornerShape(3.dp)).background(c)); Spacer(Modifier.width(10.dp))
                        T(a, DType.Meta, Modifier.weight(1f), color = DColor.TextDim); T(b, DType.Meta, color = DColor.TextDim)
                    }
                }
            }
        }
        item { SecondaryButton("Clear cache", Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) { cache = 0; nav.say("Cache cleared") } }
        item { SecondaryButton("Remove all downloads", Modifier.padding(16.dp), danger = true) { nav.say("Downloads removed") } }
        item { Note("Clearing the cache never removes your library or downloads.") }
    }
}

@Composable
private fun RadioRow(title: String, sub: String, selected: Boolean, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) { T(title, DType.BodyStrong); T(sub, DType.Small, color = DColor.TextDim) }
        Box(
            Modifier.size(22.dp).clip(CircleShape).background(if (selected) DColor.Marigold else DColor.Elevated),
            contentAlignment = Alignment.Center,
        ) { Box(Modifier.size(if (selected) 8.dp else 16.dp).clip(CircleShape).background(if (selected) DColor.Ink else DColor.Night)) }
    }
}

// ------------------------------------------------------------------ S37 Privacy & social

@Composable
fun PrivacyScreen() {
    val nav = LocalNav.current
    var share by rememberSaveable { mutableStateOf(false) }
    var privateSession by rememberSaveable { mutableStateOf(false) }
    ListScreen("Privacy & social") {
        item { GroupLabel("Profile") }
        item { SettingsRow("Who can see my profile", value = "Everyone", onClick = { nav.say("Everyone · Followers · Only me") }) }
        item { SettingsRow("New playlists are", value = "Private", onClick = { nav.say("Private · Public") }) }
        item { GroupLabel("Listening activity") }
        item { ToggleRow("Share what I listen to", "Friends see your recent songs", share) { share = it } }
        item { ToggleRow("Private session", "Pauses history and sharing until you end it", privateSession) { privateSession = it } }
        item {
            Row(Modifier.padding(16.dp).fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(DColor.Surface).padding(14.dp)) {
                Icon(Icons.Rounded.Info, null, tint = DColor.Info, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(10.dp))
                T("A private session still shapes your recommendations unless you turn that off in Your taste.", DType.Small, color = DColor.TextDim)
            }
        }
        item { GroupLabel("Safety") }
        item { SettingsRow("Blocked accounts", value = "2", onClick = { nav.say("2 blocked accounts") }) }
        item { SettingsRow("Your taste & recommendations", "Languages, hidden songs, reset", onClick = { nav.go(Route.YourTaste) }) }
        item { Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) { TextLink("Preview my public profile", style = DType.BodyStrong) { nav.go(Route.OtherProfile(Sample.me)) } } }
    }
}

// ------------------------------------------------------------------ S38 Account & security

@Composable
fun AccountScreen() {
    val nav = LocalNav.current
    ListScreen("Account & security") {
        item { GroupLabel("Login") }
        item { SettingsRow("Email", "${Sample.email} · only you see this", onClick = { nav.say("Change email") }) }
        item { SettingsRow("Password", "Changed 3 months ago", onClick = { nav.go(Route.ResetPassword) }) }
        item { SettingsRow("Google", "Connected", chevron = false) }
        item { GroupLabel("Region") }
        item { SettingsRow("Country", value = "India", chevron = false) }
        item { GroupLabel("Devices") }
        item { SettingsRow("Signed-in devices", "This phone, Chrome on Windows, Living room TV", onClick = { nav.say("3 devices signed in") }) }
        item { SettingsRow("Log out everywhere", "You stay logged in on this phone", onClick = { nav.say("Logged out on other devices") }) }
        item { GroupLabel("Your data") }
        item { SettingsRow("Download your data", "We email you a copy", onClick = { nav.say("We’ll email ${Sample.email}") }) }
        item { Divider(Modifier.padding(vertical = 8.dp)) }
        item { SettingsRow("Delete account", "Removes your playlists, likes and downloads. You will be asked to confirm.", titleColor = DColor.Error, onClick = { nav.say("You’ll be asked to confirm") }) }
    }
}

// ------------------------------------------------------------------ S39 Help & feedback

@Composable
fun HelpScreen() {
    val nav = LocalNav.current
    var q by rememberSaveable { mutableStateOf("") }
    var open by rememberSaveable { mutableIntStateOf(-1) }
    val faqs = listOf(
        "Why did a song stop playing?" to "Check your connection, then the sleep timer. If a song is unavailable in your region it is greyed out.",
        "Why can’t I download on mobile data?" to "Download on Wi-Fi only is on. Turn it off in Audio, downloads & storage.",
        "How do I share a playlist?" to "Open the playlist, tap Share, then Copy link or Send in app.",
        "How do I change my password?" to "Settings → Account & security → Password. We send a reset link to your email.",
    )
    ListScreen("Help") {
        item { SearchField(q, { q = it }, "Search help articles", Icons.Rounded.Search, Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) }
        item { GroupLabel("Topics") }
        item {
            Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf(listOf("Playback", "Downloads"), listOf("Account & login", "Playlists")).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        row.forEach { t ->
                            Box(
                                Modifier.weight(1f).height(80.dp).clip(RoundedCornerShape(12.dp)).background(DColor.Surface).clickable { q = t }.padding(16.dp),
                                contentAlignment = Alignment.BottomStart,
                            ) { T(t, DType.BodyStrong) }
                        }
                    }
                }
            }
        }
        item { GroupLabel("Common questions") }
        faqs.filter { q.isBlank() || it.first.contains(q, true) || it.second.contains(q, true) }.forEachIndexed { i, (question, answer) ->
            item {
                Column(Modifier.fillMaxWidth().clickable { open = if (open == i) -1 else i }.padding(horizontal = 16.dp, vertical = 14.dp)) {
                    T(question, DType.BodyStrong)
                    if (open == i) T(answer, DType.Meta, Modifier.padding(top = 6.dp), color = DColor.TextDim)
                }
            }
        }
        item {
            Column(Modifier.padding(16.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(DColor.Surface).padding(16.dp)) {
                T("Still stuck?", DType.BodyL.copy(fontWeight = FontWeight.SemiBold))
                T("Send us a message. You can attach a screenshot.", DType.Meta, color = DColor.TextDim)
                Spacer(Modifier.height(12.dp))
                PillButton("Contact us", icon = Icons.AutoMirrored.Rounded.Chat) { nav.say("Opening a support chat") }
            }
        }
        item { Note("Version 1.0.0 (100)  ·  Terms  ·  Privacy  ·  Licences") }
    }
}
