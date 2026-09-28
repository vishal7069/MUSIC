package com.dafli.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dafli.app.data.Art
import com.dafli.app.data.Person
import com.dafli.app.platform.RemoteImage
import com.dafli.app.platform.artPainter
import com.dafli.app.theme.DColor
import com.dafli.app.theme.DType

// ---------------------------------------------------------------- text

@Composable
fun T(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    align: TextAlign? = null,
) = Text(
    text = text, style = style, modifier = modifier, color = if (color == Color.Unspecified) style.color else color,
    maxLines = maxLines, overflow = TextOverflow.Ellipsis, textAlign = align,
)

// ---------------------------------------------------------------- images

@Composable
fun ArtImage(art: Art, modifier: Modifier = Modifier, shape: Shape = RoundedCornerShape(8.dp), url: String? = null) {
    val m = modifier.clip(shape).background(DColor.Elevated)
    if (url != null) RemoteImage(url, m, artPainter(art))
    else Image(painter = artPainter(art), contentDescription = null, contentScale = ContentScale.Crop, modifier = m)
}

@Composable
fun ArtImage(t: com.dafli.app.data.Track, modifier: Modifier = Modifier, shape: Shape = RoundedCornerShape(8.dp)) = ArtImage(t.art, modifier, shape, t.artUrl)

@Composable
fun ArtImage(c: com.dafli.app.data.Collection, modifier: Modifier = Modifier, shape: Shape = RoundedCornerShape(8.dp)) = ArtImage(c.art, modifier, shape, c.artUrl)

@Composable
fun ArtImage(a: com.dafli.app.data.Artist, modifier: Modifier = Modifier, shape: Shape = CircleShape) = ArtImage(a.art, modifier, shape, a.artUrl)

@Composable
fun Avatar(initial: String, tint: Color, size: Dp, modifier: Modifier = Modifier) {
    Box(modifier.size(size).clip(CircleShape).background(tint), contentAlignment = Alignment.Center) {
        Text(initial, style = DType.Label.copy(fontSize = (size.value * 0.42f).sp, lineHeight = (size.value * 0.5f).sp), color = DColor.Text)
    }
}

@Composable
fun Avatar(p: Person, size: Dp, modifier: Modifier = Modifier) = Avatar(p.initial, p.tint, size, modifier)

/** Solid tile with an icon — Liked Songs, Downloads, folders, local files. */
@Composable
fun IconTile(icon: ImageVector, bg: Color, fg: Color, size: Dp = 56.dp, radius: Dp = 6.dp) {
    Box(Modifier.size(size).clip(RoundedCornerShape(radius)).background(bg), contentAlignment = Alignment.Center) {
        Icon(icon, null, tint = fg, modifier = Modifier.size(size * 0.45f))
    }
}

@Composable
fun ExplicitBadge(modifier: Modifier = Modifier) {
    Box(
        modifier.size(16.dp).clip(RoundedCornerShape(3.dp)).background(DColor.Line),
        contentAlignment = Alignment.Center,
    ) { Text("E", style = DType.Tiny.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold, lineHeight = 12.sp), color = DColor.TextDim) }
}

/** Small letter badge showing who added a song (collaborative lists). */
@Composable
fun AddedByBadge(initial: String) {
    Box(Modifier.size(20.dp).clip(CircleShape).background(DColor.Elevated), contentAlignment = Alignment.Center) {
        Text(initial, style = DType.Tiny.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold), color = DColor.TextDim)
    }
}

// ---------------------------------------------------------------- buttons

@Composable
fun IconBtn(icon: ImageVector, desc: String?, modifier: Modifier = Modifier, tint: Color = DColor.Text, size: Dp = 24.dp, onClick: () -> Unit) {
    Box(modifier.size(48.dp).clip(CircleShape).clickable(onClick = onClick), contentAlignment = Alignment.Center) {
        Icon(icon, desc, tint = tint, modifier = Modifier.size(size))
    }
}

@Composable
fun PrimaryButton(text: String, modifier: Modifier = Modifier, enabled: Boolean = true, onClick: () -> Unit) {
    Box(
        modifier.fillMaxWidth().height(52.dp).clip(RoundedCornerShape(26.dp))
            .background(if (enabled) DColor.Marigold else DColor.Line)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) { T(text, DType.Button, color = if (enabled) DColor.Ink else DColor.Disabled) }
}

@Composable
fun SecondaryButton(text: String, modifier: Modifier = Modifier, icon: ImageVector? = null, danger: Boolean = false, onClick: () -> Unit) {
    Row(
        modifier.fillMaxWidth().height(52.dp).clip(RoundedCornerShape(26.dp)).background(DColor.Surface)
            .border(1.dp, DColor.Line, RoundedCornerShape(26.dp)).clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) { Icon(icon, null, tint = DColor.Text, modifier = Modifier.size(20.dp)); Spacer(Modifier.width(10.dp)) }
        T(text, DType.Button, color = if (danger) DColor.Error else DColor.Text)
    }
}

@Composable
fun PillButton(text: String, modifier: Modifier = Modifier, filled: Boolean = false, icon: ImageVector? = null, onClick: () -> Unit) {
    Row(
        modifier.height(36.dp).clip(RoundedCornerShape(18.dp))
            .then(if (filled) Modifier.background(DColor.Text) else Modifier.border(1.dp, DColor.Line, RoundedCornerShape(18.dp)))
            .clickable(onClick = onClick).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) { Icon(icon, null, tint = if (filled) DColor.Night else DColor.Text, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)) }
        T(text, DType.Label, color = if (filled) DColor.Night else DColor.Text)
    }
}

@Composable
fun TextLink(text: String, modifier: Modifier = Modifier, color: Color = DColor.Text, style: TextStyle = DType.Label, onClick: () -> Unit) {
    T(text, style, color = color, modifier = modifier.clip(RoundedCornerShape(6.dp)).clickable(onClick = onClick).padding(4.dp))
}

// ---------------------------------------------------------------- chips

@Composable
fun Chip(label: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier.height(36.dp).clip(RoundedCornerShape(18.dp))
            .background(if (selected) DColor.Text else DColor.Elevated)
            .clickable(onClick = onClick).padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) { T(label, DType.Label, color = if (selected) DColor.Night else DColor.Text) }
}

@Composable
fun ChipRow(options: List<String>, selected: Int, modifier: Modifier = Modifier, onSelect: (Int) -> Unit) {
    LazyRow(
        modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        itemsIndexed(options) { i, o -> Chip(o, i == selected) { onSelect(i) } }
    }
}

// ---------------------------------------------------------------- bars & headers

@Composable
fun TopBar(
    title: String? = null,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    backIcon: ImageVector = Icons.AutoMirrored.Rounded.ArrowBack,
    centerTitle: Boolean = false,
    background: Color = DColor.Night,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val nav = LocalNav.current
    Box(modifier.fillMaxWidth().background(background).statusBarsPadding().height(56.dp)) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 4.dp).align(Alignment.Center), verticalAlignment = Alignment.CenterVertically) {
            IconBtn(backIcon, "Back") { (onBack ?: nav::back)() }
            if (!centerTitle && title != null) T(title, DType.Title, Modifier.weight(1f).padding(start = 4.dp), maxLines = 1)
            else Spacer(Modifier.weight(1f))
            actions()
        }
        if (centerTitle && title != null) T(title, DType.TitleS, Modifier.align(Alignment.Center), maxLines = 1)
    }
}

@Composable
fun SectionHeader(title: String, modifier: Modifier = Modifier, sub: String? = null, action: String? = "See all", onAction: (() -> Unit)? = null) {
    Column(modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            T(title, DType.Title, Modifier.weight(1f), maxLines = 1)
            if (onAction != null && action != null) TextLink(action, color = DColor.TextDim, style = DType.Meta.copy(fontWeight = FontWeight.SemiBold), onClick = onAction)
        }
        if (sub != null) T(sub, DType.Meta, color = DColor.TextDim, modifier = Modifier.padding(top = 2.dp))
    }
}

@Composable
fun GroupLabel(text: String, modifier: Modifier = Modifier) {
    T(text.uppercase(), DType.Overline, modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp), color = DColor.TextDim)
}

@Composable
fun Note(text: String, modifier: Modifier = Modifier, align: TextAlign? = null) {
    T(text, DType.Small, modifier.padding(horizontal = 16.dp, vertical = 8.dp), color = DColor.TextDim, align = align)
}

// ---------------------------------------------------------------- settings rows

@Composable
fun DSwitch(checked: Boolean, onChange: (Boolean) -> Unit) {
    Switch(
        checked = checked, onCheckedChange = onChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = DColor.Ink, checkedTrackColor = DColor.Marigold, checkedBorderColor = DColor.Marigold,
            uncheckedThumbColor = DColor.TextDim, uncheckedTrackColor = DColor.Elevated, uncheckedBorderColor = DColor.Line,
        ),
    )
}

@Composable
fun SettingsRow(
    title: String,
    sub: String? = null,
    value: String? = null,
    icon: ImageVector? = null,
    titleColor: Color = DColor.Text,
    chevron: Boolean = true,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    Row(
        Modifier.fillMaxWidth().then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 16.dp, vertical = 12.dp).heightIn(min = 44.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Box(Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(DColor.Elevated), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = DColor.Text, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(16.dp))
        }
        Column(Modifier.weight(1f)) {
            T(title, DType.BodyStrong, color = titleColor)
            if (sub != null) T(sub, DType.Meta, color = DColor.TextDim, modifier = Modifier.padding(top = 2.dp))
        }
        if (value != null) T(value, DType.Label.copy(fontWeight = FontWeight.Normal), color = DColor.TextDim, modifier = Modifier.padding(start = 12.dp))
        when {
            trailing != null -> { Spacer(Modifier.width(12.dp)); trailing() }
            chevron && onClick != null -> Icon(Icons.Rounded.ChevronRight, null, tint = DColor.TextDim, modifier = Modifier.padding(start = 8.dp).size(22.dp))
        }
    }
}


@Composable
fun ToggleRow(title: String, sub: String? = null, checked: Boolean, onChange: (Boolean) -> Unit) =
    SettingsRow(title, sub, chevron = false, onClick = { onChange(!checked) }) { DSwitch(checked, onChange) }

@Composable
fun Divider(modifier: Modifier = Modifier, inset: Dp = 16.dp) {
    Box(modifier.padding(horizontal = inset).fillMaxWidth().height(1.dp).background(DColor.Line))
}

@Composable
fun Card(modifier: Modifier = Modifier, padding: PaddingValues = PaddingValues(16.dp), content: @Composable () -> Unit) {
    Box(modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(DColor.Surface).border(BorderStroke(1.dp, DColor.Line), RoundedCornerShape(16.dp)).padding(padding)) { content() }
}
