package com.dafli.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dafli.app.data.Art
import com.dafli.app.data.Collection
import com.dafli.app.data.Track
import com.dafli.app.nav.Route
import com.dafli.app.nav.Sheet
import com.dafli.app.nav.Tab
import com.dafli.app.theme.DColor
import com.dafli.app.theme.DType

/** One song row. Title turns marigold when it is the song playing now. */
@Composable
fun TrackRow(
    track: Track,
    modifier: Modifier = Modifier,
    index: Int? = null,
    subtitle: String = track.artist,
    showArt: Boolean = true,
    dim: Boolean = false,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    val player = LocalPlayer.current
    val nav = LocalNav.current
    val isCurrent = player.hasTrack && player.current.id == track.id
    Row(
        modifier.fillMaxWidth().clickable { (onClick ?: { player.play(track) })() }.padding(start = 16.dp, end = 4.dp).height(64.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (index != null) {
            T("$index", DType.BodyStrong, Modifier.width(28.dp), color = if (isCurrent) DColor.Marigold else DColor.TextDim)
        }
        if (showArt) {
            ArtImage(track, Modifier.size(48.dp), RoundedCornerShape(6.dp))
            Spacer(Modifier.width(12.dp))
        }
        Column(Modifier.weight(1f)) {
            T(track.title, DType.BodyStrong, color = when { dim -> DColor.Disabled; isCurrent -> DColor.Marigold; else -> DColor.Text }, maxLines = 1)
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                if (track.explicit) { ExplicitBadge(); Spacer(Modifier.width(6.dp)) }
                T(subtitle, DType.Meta, color = if (dim) DColor.Disabled else DColor.TextDim, maxLines = 1)
            }
        }
        if (trailing != null) trailing()
        else IconBtn(Icons.Rounded.MoreVert, "More options for ${track.title}", tint = DColor.TextDim, size = 20.dp) { nav.open(Sheet.TrackMenu(track)) }
    }
}

/** Square cover with two lines under it (rails on Home, Search, Artist…). */
@Composable
fun CoverCard(art: Art, title: String, sub: String?, size: Dp = 148.dp, circle: Boolean = false, url: String? = null, onClick: () -> Unit) {
    Column(Modifier.width(size).clip(RoundedCornerShape(10.dp)).clickable(onClick = onClick)) {
        ArtImage(art, Modifier.size(size), if (circle) CircleShape else RoundedCornerShape(10.dp), url)
        Spacer(Modifier.height(8.dp))
        T(title, DType.Label, maxLines = 1)
        if (sub != null) T(sub, DType.Small, color = DColor.TextDim, maxLines = 1)
    }
}

@Composable
fun CollectionCard(c: Collection, size: Dp = 148.dp, onClick: () -> Unit) = CoverCard(c.art, c.title, c.subtitle, size, url = c.artUrl, onClick = onClick)

@Composable
fun TrackCard(t: Track, size: Dp = 148.dp, onClick: () -> Unit) = CoverCard(t.art, t.title, t.artist, size, url = t.artUrl, onClick = onClick)

/** Library style row: 56px art, title, meta. */
@Composable
fun LibraryRow(
    title: String,
    sub: String,
    art: Art? = null,
    circle: Boolean = false,
    url: String? = null,
    leading: (@Composable () -> Unit)? = null,
    subLeading: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
    trailing: (@Composable () -> Unit)? = null,
) {
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when {
            leading != null -> leading()
            art != null -> ArtImage(art, Modifier.size(56.dp), if (circle) CircleShape else RoundedCornerShape(6.dp), url)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            T(title, DType.BodyL.copy(fontWeight = FontWeight.SemiBold), maxLines = 1)
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                if (subLeading != null) { subLeading(); Spacer(Modifier.width(4.dp)) }
                T(sub, DType.Meta, color = DColor.TextDim, maxLines = 1)
            }
        }
        trailing?.invoke()
    }
}

// ---------------------------------------------------------------- fixed chrome

@Composable
fun MiniPlayer(modifier: Modifier = Modifier) {
    val player = LocalPlayer.current
    val nav = LocalNav.current
    val t = player.current
    Box(
        modifier.padding(horizontal = 8.dp).fillMaxWidth().height(60.dp).clip(RoundedCornerShape(10.dp))
            .background(DColor.Elevated).clickable { nav.go(Route.NowPlaying) },
    ) {
        Row(Modifier.fillMaxWidth().padding(start = 8.dp, end = 4.dp).align(Alignment.CenterStart), verticalAlignment = Alignment.CenterVertically) {
            ArtImage(t, Modifier.size(44.dp), RoundedCornerShape(6.dp))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                T(t.title, DType.Label, maxLines = 1)
                T(t.artist, DType.Small, color = DColor.TextDim, maxLines = 1)
            }
            val lib = LocalLib.current
            val liked = lib.isLiked(t)
            IconBtn(if (liked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder, if (liked) "Remove from Liked Songs" else "Add to Liked Songs", tint = if (liked) DColor.Marigold else DColor.TextDim, size = 22.dp) { player.toggleLike(t) }
            IconBtn(if (player.isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, if (player.isPlaying) "Pause" else "Play", size = 28.dp) { player.toggle() }
        }
        // progress
        Box(Modifier.align(Alignment.BottomStart).padding(horizontal = 8.dp).fillMaxWidth().height(2.dp).background(DColor.Line))
        Box(Modifier.align(Alignment.BottomStart).padding(horizontal = 8.dp).fillMaxWidth(player.progress).height(2.dp).background(DColor.Marigold))
    }
}

@Composable
fun BottomNav(modifier: Modifier = Modifier) {
    val nav = LocalNav.current
    val tab = nav.tab
    Row(
        modifier.fillMaxWidth().background(DColor.Night).navigationBarsPadding().height(60.dp),
        horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically,
    ) {
        NavItem("Home", Icons.Rounded.Home, Icons.Outlined.Home, tab == Tab.Home) { nav.switchTab(Tab.Home) }
        NavItem("Search", Icons.Rounded.Search, Icons.Rounded.Search, tab == Tab.Search) { nav.switchTab(Tab.Search) }
        NavItem("Library", Icons.Rounded.LibraryMusic, Icons.Outlined.LibraryMusic, tab == Tab.Library) { nav.switchTab(Tab.Library) }
    }
}

@Composable
private fun NavItem(label: String, on: ImageVector, off: ImageVector, selected: Boolean, onClick: () -> Unit) {
    val c = if (selected) DColor.Marigold else DColor.TextDim
    Column(
        Modifier.width(96.dp).clip(RoundedCornerShape(12.dp)).clickable(onClick = onClick).padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(if (selected) on else off, null, tint = c, modifier = Modifier.size(24.dp))
        Spacer(Modifier.height(4.dp))
        T(label, DType.Tiny.copy(fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium), color = c)
    }
}
