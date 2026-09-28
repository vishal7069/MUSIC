package com.dafli.app.ui.brand

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.dafli.app.platform.InterFamily
import com.dafli.app.theme.DColor
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp

// ---- Easing used in the Figma Motion timeline ------------------------------------------------
private val EaseInOut = CubicBezierEasing(0.42f, 0f, 0.58f, 1f)
private val EaseOut = CubicBezierEasing(0f, 0f, 0.58f, 1f)
private val Glide = CubicBezierEasing(0.2f, 0f, 0f, 1f)
/** Figma "custom spring" (bouncy): ~11 % overshoot, settles by the end of the segment. */
private val Spring: (Float) -> Float = { p -> 1f - exp(-6.5f * p) * cos(3f * PI.toFloat() * p) }
private val Linear: (Float) -> Float = { it }

private class Kf(val t: Float, val v: Float, val ease: (Float) -> Float = EaseOut::transform)

/** Keyframe track: the easing on a keyframe shapes the segment that ends on it (same as Figma). */
private fun track(t: Float, vararg k: Kf): Float {
    if (t <= k[0].t) return k[0].v
    for (i in 1 until k.size) {
        if (t <= k[i].t) {
            val a = k[i - 1]; val b = k[i]
            val p = ((t - a.t) / (b.t - a.t)).coerceIn(0f, 1f)
            return a.v + (b.v - a.v) * b.ease(p)
        }
    }
    return k.last().v
}

/**
 * S01 · Splash. Port of the 3 s Figma Motion timeline "Da–fli":
 *  0.00–0.50  the icon's d grows from launcher size to logo size (continues the system splash)
 *  0.62–1.02  "Da" — first drum hit: squash, impact lines, soft marigold flash
 *  1.14–1.42  "fli" — second, lighter hit
 *  1.36–1.80  d glides left to make room, 1.55–2.00 "aflı" slides in
 *  1.86–2.30  the jingle drops onto the ı with a spring
 *  2.25–2.65  tagline "Music, sabka."
 *  3.00       hand-off to Welcome. Tap anywhere to skip.
 */
@Composable
fun SplashScreen(onDone: () -> Unit) {
    val time = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        time.animateTo(3f, tween(3000, easing = LinearEasing))
        onDone()
    }
    val measurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val u = density.density // 1 Figma px = 1 dp
    val g = remember(density) { measurer.lockup(120f * u, u) }
    val tagline = remember(density) {
        measurer.measure("Music, sabka.", TextStyle(fontFamily = InterFamily, fontSize = 17.sp, color = DColor.TextDim, textAlign = TextAlign.Center))
    }

    Box(
        Modifier.fillMaxSize().background(DColor.Night)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onDone() },
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val t = time.value
            val k = g.s / 120f // Figma values were authored at word size 120
            // Lockup is centred on screen (Figma: frame 70,342 in 390×844)
            val ox = (size.width - g.width) / 2f
            val oy = (size.height - g.height) / 2f - 8f * u

            val centredTx = (g.width - g.markW) / 2f // d centred on screen
            val pose = LockupPose(
                markTx = track(t, Kf(0f, centredTx), Kf(1.36f, centredTx, Linear), Kf(1.8f, 0f, Glide::transform)),
                markTy = k * u * track(
                    t, Kf(0.62f, 0f), Kf(0.7f, 6.5f), Kf(1.02f, 0f, Spring), Kf(1.14f, 0f), Kf(1.2f, 3.5f), Kf(1.42f, 0f, Spring),
                ),
                markSx = track(
                    t, Kf(0.05f, 0.375f), Kf(0.5f, 1f, EaseInOut::transform), Kf(0.62f, 1f),
                    Kf(0.7f, 1.16f), Kf(1.02f, 1f, Spring), Kf(1.14f, 1f), Kf(1.2f, 1.08f), Kf(1.42f, 1f, Spring),
                ),
                markSy = track(
                    t, Kf(0.05f, 0.375f), Kf(0.5f, 1f, EaseInOut::transform), Kf(0.62f, 1f),
                    Kf(0.7f, 0.86f), Kf(1.02f, 1f, Spring), Kf(1.14f, 1f), Kf(1.2f, 0.93f), Kf(1.42f, 1f, Spring),
                ),
                wordAlpha = track(t, Kf(1.55f, 0f), Kf(1.9f, 1f)),
                wordTx = k * u * track(t, Kf(1.55f, 24f), Kf(2.0f, 0f)),
                jingleAlpha = track(t, Kf(1.86f, 0f), Kf(1.9f, 1f)),
                jingleTy = k * u * track(t, Kf(1.86f, -80f), Kf(2.3f, 0f, Spring)),
                jingleRot = track(t, Kf(1.86f, 50f), Kf(2.3f, 0f, Spring)),
            )

            // marigold flash on each beat
            val flash = track(t, Kf(0.62f, 0f), Kf(0.7f, 0.09f), Kf(0.98f, 0f), Kf(1.14f, 0f), Kf(1.2f, 0.05f), Kf(1.42f, 0f))
            if (flash > 0f) drawRect(DColor.Marigold, alpha = flash)

            translate(ox, oy) {
                // Impact lines — "Da" (left of the drum) and "fli" (upper right)
                val markLeft = pose.markTx
                val da = track(t, Kf(0.64f, 0f), Kf(0.7f, 1f), Kf(1.0f, 0f))
                if (da > 0f) impact(
                    Offset(markLeft - 64.3f * k * u + track(t, Kf(0.64f, 6f), Kf(1f, -16f)) * k * u, g.markTop - 10f * k * u + track(t, Kf(0.64f, 0f), Kf(1f, 6f)) * k * u),
                    listOf(Triple(38f, 19f, -35f), Triple(20f, 43f, -70f), Triple(19f, 73f, -105f)), k * u, da,
                )
                val fli = track(t, Kf(1.16f, 0f), Kf(1.2f, 1f), Kf(1.46f, 0f))
                if (fli > 0f) impact(
                    Offset(markLeft - 12.3f * k * u + track(t, Kf(1.16f, -6f), Kf(1.46f, 12f)) * k * u, g.markTop - 58f * k * u + track(t, Kf(1.16f, 4f), Kf(1.46f, -10f)) * k * u),
                    listOf(Triple(115f, 47f, 75f), Triple(99f, 22f, 40f), Triple(71f, 10f, 5f)), k * u, fli,
                )
                drawLockup(g, pose)
            }

            // tagline
            val ta = track(t, Kf(2.25f, 0f), Kf(2.65f, 1f))
            if (ta > 0f) {
                val ty = track(t, Kf(2.25f, 12f), Kf(2.65f, 0f)) * u
                drawText(
                    tagline, alpha = ta,
                    topLeft = Offset((size.width - tagline.size.width) / 2f, oy + g.height + 26f * u + ty),
                )
            }
        }
    }
}

/** Three short rounded strokes (6×20) — the "sound" of the drum hit. */
private fun DrawScope.impact(origin: Offset, lines: List<Triple<Float, Float, Float>>, k: Float, alpha: Float) {
    for ((x, y, r) in lines) {
        val c = Offset(origin.x + (x + 3f) * k, origin.y + (y + 10f) * k)
        rotate(r, pivot = c) {
            drawRoundRect(
                DColor.Marigold, topLeft = Offset(c.x - 3f * k, c.y - 10f * k), size = Size(6f * k, 20f * k),
                cornerRadius = CornerRadius(3f * k), alpha = alpha,
            )
        }
    }
}
