package com.dafli.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.PlaylistAdd
import androidx.compose.material.icons.automirrored.rounded.QueueMusic
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.Bluetooth
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.automirrored.rounded.PlaylistPlay
import androidx.compose.material.icons.rounded.QrCode2
import androidx.compose.material.icons.rounded.Radio
import androidx.compose.material.icons.rounded.RemoveCircleOutline
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Speaker
import androidx.compose.material.icons.rounded.Tv
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dafli.app.data.Collection
import com.dafli.app.data.Kind
import com.dafli.app.data.Sample
import com.dafli.app.data.Track
import com.dafli.app.data.asTime
import com.dafli.app.nav.Route
import com.dafli.app.nav.Sheet
import com.dafli.app.theme.DColor
import com.dafli.app.theme.DType
import com.dafli.app.ui.components.ArtImage
import com.dafli.app.ui.components.DField
import com.dafli.app.ui.components.LocalNav
import com.dafli.app.ui.components.LocalPlatform
import com.dafli.app.ui.components.LocalPlayer
import com.dafli.app.ui.components.PrimaryButton
import com.dafli.app.ui.components.T
import com.dafli.app.ui.components.TextLink

@Composable
fun SheetContent(sheet: Sheet) {
    Column(Modifier.fillMaxWidth().heightIn(max = 640.dp).verticalScroll(rememberScrollState()).padding(bottom = 12.dp)) {
        when (sheet) {
            is Sheet.TrackMenu -> TrackMenuSheet(sheet.track)
            Sheet.OutputDevice -> OutputDeviceSheet()
            Sheet.SleepTimer -> SleepTimerSheet()
            is Sheet.Share -> ShareSheet(sheet)
            is Sheet.Report -> ReportSheet(sheet)
        }
    }
}

@Composable
private fun SheetItem(icon: ImageVector, label: String, tint: Color = DColor.Text, labelColor: Color = DColor.Text, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 20.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(16.dp))
        T(label, DType.BodyL, color = labelColor)
    }
}

// ------------------------------------------------------------------ S22 Track menu

@Composable
private fun TrackMenuSheet(t: Track) {
    val nav = LocalNav.current
    val p = LocalPlayer.current
    val platform = LocalPlatform.current
    val liked = p.isLiked(t)
    Row(Modifier.padding(horizontal = 20.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        ArtImage(t, Modifier.size(56.dp), RoundedCornerShape(6.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            T(t.title, DType.BodyL.copy(fontWeight = FontWeight.SemiBold), maxLines = 1)
            T(t.artist, DType.Meta, color = DColor.TextDim, maxLines = 1)
        }
    }
    Box(Modifier.padding(horizontal = 20.dp).fillMaxWidth().padding(vertical = 4.dp).background(DColor.Line).heightIn(min = 1.dp, max = 1.dp))
    SheetItem(if (liked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder, if (liked) "Liked" else "Like", tint = if (liked) DColor.Marigold else DColor.Text) {
        p.toggleLike(t); nav.closeSheet(); nav.say(if (p.isLiked(t)) "Added to Liked Songs" else "Removed from Liked Songs")
    }
    SheetItem(Icons.AutoMirrored.Rounded.PlaylistPlay, "Play next") { p.playNext(t); nav.closeSheet(); nav.say("Plays next") }
    SheetItem(Icons.AutoMirrored.Rounded.QueueMusic, "Add to queue") { p.addToQueue(t); nav.closeSheet(); nav.say("Added to queue") }
    if (t.artistId != null) SheetItem(Icons.Rounded.Person, "Go to artist") { nav.closeSheet(); nav.go(Route.ArtistPage(com.dafli.app.data.Artist(t.artist, t.art, id = t.artistId))) }
    if (t.shareUrl != null) SheetItem(Icons.Rounded.Share, "Share") { nav.closeSheet(); platform.share(t.title, "${t.title} by ${t.artist} — ${t.shareUrl}") }
    SheetItem(Icons.Rounded.Flag, "Report", tint = DColor.TextDim) { nav.open(Sheet.Report("song", t.artist, t.title, t.shareUrl ?: t.id)) }
}

// ------------------------------------------------------------------ S30 Output device

@Composable
private fun OutputDeviceSheet() {
    val nav = LocalNav.current
    val p = LocalPlayer.current
    T("Play on", DType.TitleS.copy(fontWeight = FontWeight.Bold), Modifier.padding(horizontal = 20.dp, vertical = 12.dp))

    @Composable
    fun Device(icon: ImageVector, name: String, sub: String) {
        val on = p.output == name
        Row(
            Modifier.padding(horizontal = 12.dp).fillMaxWidth().clip(RoundedCornerShape(12.dp))
                .background(if (on) DColor.Surface else Color.Transparent)
                .clickable { p.output = name; nav.closeSheet(); if (name != "This phone") nav.say("Playing on $name") }
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icon, null, tint = if (on) DColor.Marigold else DColor.Text, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                T(name, DType.BodyStrong, color = if (on) DColor.Marigold else DColor.Text)
                T(if (on) "Playing now" else sub, DType.Small.copy(fontWeight = FontWeight.Medium), color = DColor.TextDim)
            }
            if (on) Icon(Icons.Rounded.Check, "Selected", tint = DColor.Marigold)
        }
    }
    Device(Icons.Rounded.PhoneAndroid, "This phone", "Tap to play here")
    T("BLUETOOTH", DType.Overline, Modifier.padding(start = 20.dp, top = 16.dp, bottom = 4.dp), color = DColor.TextDim)
    Device(Icons.Rounded.Bluetooth, "Vishal’s earbuds", "Connected in phone settings")
    T("ON THIS WI-FI", DType.Overline, Modifier.padding(start = 20.dp, top = 16.dp, bottom = 4.dp), color = DColor.TextDim)
    Device(Icons.Rounded.Tv, "Living room TV", "Tap to move playback")
    Device(Icons.Rounded.Speaker, "Kitchen speaker", "Tap to move playback")
    Spacer(Modifier.padding(4.dp))
    SheetItem(Icons.Rounded.Groups, "Start Listen Together") { nav.closeSheet(); nav.go(Route.ListenTogether) }
    TextLink("Don’t see your device?", Modifier.padding(horizontal = 16.dp), color = DColor.TextDim) { nav.closeSheet(); nav.go(Route.Help) }
}

// ------------------------------------------------------------------ S31 Sleep timer

@Composable
private fun SleepTimerSheet() {
    val nav = LocalNav.current
    val p = LocalPlayer.current
    T("Sleep timer", DType.TitleS.copy(fontWeight = FontWeight.Bold), Modifier.padding(start = 20.dp, top = 12.dp))
    T("Music pauses when the timer ends.", DType.Meta, Modifier.padding(horizontal = 20.dp, vertical = 4.dp), color = DColor.TextDim)
    val opts = listOf("Off" to null, "5 minutes" to 300, "15 minutes" to 900, "30 minutes" to 1800, "45 minutes" to 2700, "1 hour" to 3600, "End of this song" to -1)
    opts.forEach { (label, secs) ->
        val selected = when {
            secs == null -> p.sleepLeft == null
            secs == -1 -> p.sleepLeft == -1
            else -> false
        }
        Row(
            Modifier.fillMaxWidth().clickable { p.sleepLeft = secs; nav.closeSheet(); nav.say(if (secs == null) "Sleep timer off" else "Sleep timer set · $label") }
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            T(label, DType.BodyL.copy(fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium), Modifier.weight(1f))
            if (selected) Icon(Icons.Rounded.Check, "Selected", tint = DColor.Marigold)
        }
    }
    val left = p.sleepLeft
    if (left != null && left > 0) {
        T("Pausing in ${left.asTime()}", DType.Meta.copy(fontWeight = FontWeight.Medium), Modifier.padding(horizontal = 20.dp, vertical = 8.dp), color = DColor.Marigold)
    }
}

// ------------------------------------------------------------------ S41 Share

@Composable
private fun ShareSheet(s: Sheet.Share) {
    val nav = LocalNav.current
    var copied by remember { mutableStateOf(false) }
    if (copied) Row(
        Modifier.padding(horizontal = 20.dp, vertical = 8.dp).fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(DColor.Success.copy(alpha = 0.15f)).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Rounded.Check, null, tint = DColor.Success)
        Spacer(Modifier.width(8.dp))
        T("Link copied", DType.BodyStrong, color = DColor.Success)
    }
    Row(Modifier.padding(horizontal = 20.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        ArtImage(s.art, Modifier.size(72.dp), RoundedCornerShape(8.dp))
        Spacer(Modifier.width(16.dp))
        Column {
            T(s.title, DType.BodyL.copy(fontWeight = FontWeight.SemiBold))
            T(s.subtitle, DType.Meta, color = DColor.TextDim)
        }
    }
    Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
        ShareAction(Icons.Rounded.ContentCopy, "Copy link") { copied = true }
        ShareAction(Icons.Rounded.QrCode2, "QR code") { nav.say("QR code ready to scan") }
        ShareAction(Icons.AutoMirrored.Rounded.Send, "Send in app") { nav.closeSheet(); nav.go(Route.Messages) }
        ShareAction(Icons.Rounded.MoreHoriz, "More") { nav.say("Opens the Android share menu") }
    }
    T("Opening this link shows the ${s.subtitle.substringBefore(" ·").lowercase()} first. It never starts playing on its own.", DType.Small,
        Modifier.padding(horizontal = 24.dp, vertical = 12.dp), color = DColor.TextDim)
}

@Composable
private fun ShareAction(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(Modifier.width(80.dp).clip(RoundedCornerShape(12.dp)).clickable(onClick = onClick).padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(52.dp).clip(CircleShape).background(DColor.Surface), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = DColor.Text)
        }
        T(label, DType.Small.copy(fontWeight = FontWeight.Medium), Modifier.padding(top = 8.dp), maxLines = 1)
    }
}

// ------------------------------------------------------------------ S40 Report

@Composable
private fun ReportSheet(r: Sheet.Report) {
    val nav = LocalNav.current
    val platform = LocalPlatform.current
    val reasons = listOf("Spam or misleading", "Hateful or offensive", "Sexual or violent content", "Copyright / uses my work without permission", "Something else")
    var pick by remember { mutableIntStateOf(-1) }
    var details by remember { mutableStateOf("") }
    val what = if (r.what == "song") "song" else "playlist"
    T("Report this $what", DType.TitleS.copy(fontWeight = FontWeight.Bold), Modifier.padding(start = 20.dp, top = 12.dp))
    T("${r.title} · ${r.owner}", DType.Meta, Modifier.padding(horizontal = 20.dp, vertical = 4.dp), color = DColor.TextDim)
    reasons.forEachIndexed { i, label ->
        Row(Modifier.fillMaxWidth().clickable { pick = i }.padding(horizontal = 20.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(22.dp).clip(CircleShape).border(2.dp, if (pick == i) DColor.Marigold else DColor.TextDim, CircleShape),
                contentAlignment = Alignment.Center,
            ) { if (pick == i) Box(Modifier.size(12.dp).clip(CircleShape).background(DColor.Marigold)) }
            Spacer(Modifier.width(14.dp))
            T(label, DType.BodyL.copy(fontWeight = FontWeight.Medium))
        }
    }
    DField(null, details, { if (it.length <= 1000) details = it }, Modifier.padding(horizontal = 20.dp, vertical = 8.dp), placeholder = "Add details (optional)", singleLine = false, helper = "${details.length} / 1000")
    PrimaryButton("Send report", Modifier.padding(horizontal = 20.dp, vertical = 8.dp), enabled = pick >= 0) {
        nav.closeSheet()
        platform.sendEmail(com.dafli.app.AppConfig.SUPPORT_EMAIL, "Report: ${reasons[pick]} · ${r.title} (${r.ref})" + if (details.isNotBlank()) " · $details" else "")
    }
    T("Opens your email app with the report filled in. We review every report and pass copyright claims to ${com.dafli.app.AppConfig.MUSIC_SOURCE}.", DType.Small, Modifier.padding(horizontal = 20.dp, vertical = 4.dp), color = DColor.TextDim)
}

