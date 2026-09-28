package com.dafli.app.platform

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.dafli.app.R
import com.dafli.app.data.Art

/** Android-specific glue. Everything else in the app is plain Compose. */
val InterFamily = FontFamily(
    Font(R.font.inter_400, FontWeight.Normal),
    Font(R.font.inter_500, FontWeight.Medium),
    Font(R.font.inter_600, FontWeight.SemiBold),
    Font(R.font.inter_700, FontWeight.Bold),
    Font(R.font.inter_800, FontWeight.ExtraBold),
)

@Composable
fun artPainter(art: Art): Painter = painterResource(
    when (art) {
        Art.A -> R.drawable.art_a
        Art.B -> R.drawable.art_b
        Art.C -> R.drawable.art_c
        Art.D -> R.drawable.art_d
        Art.E -> R.drawable.art_e
        Art.F -> R.drawable.art_f
        Art.G -> R.drawable.art_g
        Art.H -> R.drawable.art_h
        Art.I -> R.drawable.art_i
        Art.J -> R.drawable.art_j
        Art.K -> R.drawable.art_k
        Art.KabirSen -> R.drawable.art_m1
        Art.AravKapoor -> R.drawable.art_m2
        Art.IraMenon -> R.drawable.art_w1
        Art.W2 -> R.drawable.art_w2
        Art.Hero -> R.drawable.art_hero
        Art.Bollywood -> R.drawable.art_bw
        Art.Punjabi -> R.drawable.art_pj
        Art.HipHop -> R.drawable.art_hh
        Art.Devotional -> R.drawable.art_dv
        Art.Indie -> R.drawable.art_in
        Art.Workout -> R.drawable.art_wo
        Art.Retro -> R.drawable.art_retro
        Art.LoFi -> R.drawable.art_lo
    }
)

@Composable
fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) = BackHandler(enabled, onBack)

/** Network artwork (Audius covers) with the bundled art as placeholder / error image. */
@Composable
fun RemoteImage(url: String, modifier: Modifier, fallback: Painter) {
    AsyncImage(
        model = url, contentDescription = null, contentScale = ContentScale.Crop, modifier = modifier,
        placeholder = fallback, error = fallback, fallback = fallback,
    )
}
