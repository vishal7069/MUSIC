package com.dafli.app.ui.brand

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.dafli.app.platform.InterFamily
import com.dafli.app.theme.DColor

/**
 * The Dafli mark: a lowercase d. The bowl is a superellipse drum (n = 3.4), the stem has round
 * ends and the counter is a play head. Drawn on a 100 grid, bounds 60 × 88 (viewBox 20 4 60 88).
 */
const val MARK_PATH = "M80.00 62.00 L79.99 66.47 L79.95 68.71 L79.88 70.51 L79.78 72.07 L79.66 73.47 L79.51 74.75 L79.33 75.93 L79.13 77.04 L78.89 78.07 L78.63 79.05 L78.35 79.98 L78.03 80.85 L77.69 81.69 L77.31 82.48 L76.91 83.23 L76.48 83.95 L76.02 84.63 L75.54 85.27 L75.02 85.89 L74.47 86.47 L73.89 87.02 L73.27 87.54 L72.63 88.02 L71.95 88.48 L71.23 88.91 L70.48 89.31 L69.69 89.69 L68.85 90.03 L67.98 90.35 L67.05 90.63 L66.07 90.89 L65.04 91.13 L63.93 91.33 L62.75 91.51 L61.47 91.66 L60.07 91.78 L58.51 91.88 L56.71 91.95 L54.47 91.99 L50.00 92.00 L45.53 91.99 L43.29 91.95 L41.49 91.88 L39.93 91.78 L38.53 91.66 L37.25 91.51 L36.07 91.33 L34.96 91.13 L33.93 90.89 L32.95 90.63 L32.02 90.35 L31.15 90.03 L30.31 89.69 L29.52 89.31 L28.77 88.91 L28.05 88.48 L27.37 88.02 L26.73 87.54 L26.11 87.02 L25.53 86.47 L24.98 85.89 L24.46 85.27 L23.98 84.63 L23.52 83.95 L23.09 83.23 L22.69 82.48 L22.31 81.69 L21.97 80.85 L21.65 79.98 L21.37 79.05 L21.11 78.07 L20.87 77.04 L20.67 75.93 L20.49 74.75 L20.34 73.47 L20.22 72.07 L20.12 70.51 L20.05 68.71 L20.01 66.47 L20.00 62.00 L20.01 57.53 L20.05 55.29 L20.12 53.49 L20.22 51.93 L20.34 50.53 L20.49 49.25 L20.67 48.07 L20.87 46.96 L21.11 45.93 L21.37 44.95 L21.65 44.02 L21.97 43.15 L22.31 42.31 L22.69 41.52 L23.09 40.77 L23.52 40.05 L23.98 39.37 L24.46 38.73 L24.98 38.11 L25.53 37.53 L26.11 36.98 L26.73 36.46 L27.37 35.98 L28.05 35.52 L28.77 35.09 L29.52 34.69 L30.31 34.31 L31.15 33.97 L32.02 33.65 L32.95 33.37 L33.93 33.11 L34.96 32.87 L36.07 32.67 L37.25 32.49 L38.53 32.34 L39.93 32.22 L41.49 32.12 L43.29 32.05 L45.53 32.01 L50.00 32.00 L54.47 32.01 L56.71 32.05 L58.51 32.12 L60.07 32.22 L61.47 32.34 L62.75 32.49 L63.93 32.67 L65.04 32.87 L66.07 33.11 L67.05 33.37 L67.98 33.65 L68.85 33.97 L69.69 34.31 L70.48 34.69 L71.23 35.09 L71.95 35.52 L72.63 35.98 L73.27 36.46 L73.89 36.98 L74.47 37.53 L75.02 38.11 L75.54 38.73 L76.02 39.37 L76.48 40.05 L76.91 40.77 L77.31 41.52 L77.69 42.31 L78.03 43.15 L78.35 44.02 L78.63 44.95 L78.89 45.93 L79.13 46.96 L79.33 48.07 L79.51 49.25 L79.66 50.53 L79.78 51.93 L79.88 53.49 L79.95 55.29 L79.99 57.53 Z M71.0 4 H71.0 A9.0 9.0 0 0 1 80 13.0 V57.0 A9.0 9.0 0 0 1 71.0 66.0 H71.0 A9.0 9.0 0 0 1 62 57.0 V13.0 A9.0 9.0 0 0 1 71.0 4 Z M36.98 52.18 L36.98 71.82 A3.4 3.4 0 0 0 42.08 74.77 L59.17 64.95 A3.4 3.4 0 0 0 59.17 59.05 L42.08 49.23 A3.4 3.4 0 0 0 36.98 52.18 Z"

val MarkPath: Path by lazy { PathParser().parsePathString(MARK_PATH).toPath() }

/** Draws the mark so its box is [w] × [h] px at the current origin. */
fun DrawScope.drawMark(w: Float, h: Float, color: Color = DColor.Marigold) {
    withTransform({
        scale(w / 60f, h / 88f, pivot = Offset.Zero)
        translate(-20f, -4f)
    }) { drawPath(MarkPath, color) }
}

@Composable
fun DafliMark(modifier: Modifier = Modifier, color: Color = DColor.Marigold) {
    Canvas(modifier.aspectRatio(60f / 88f)) { drawMark(size.width, size.height, color) }
}

/** Animation hooks for the lockup. Defaults = the resting logo. */
data class LockupPose(
    val markTx: Float = 0f, val markTy: Float = 0f, val markSx: Float = 1f, val markSy: Float = 1f,
    val wordAlpha: Float = 1f, val wordTx: Float = 0f,
    val jingleAlpha: Float = 1f, val jingleTy: Float = 0f, val jingleRot: Float = 0f,
)

/** Geometry of the lockup "d + aflı + jingle" for a given word size [s] (px). All values in px. */
class LockupGeometry(val s: Float, val layout: TextLayoutResult) {
    val baseline = layout.firstBaseline
    val markH = s * 0.546f * 88f / 60f      // bowl height = x-height of Inter
    val markW = markH * 60f / 88f
    val gap = s * 0.0417f
    val wordX = markW + gap
    val width = wordX + layout.size.width
    val height = layout.size.height.toFloat()
    val markTop = baseline - markH
    val jingle = s * 0.155f
    /** Centre of the dotless ı, where the jingle (tiny marigold square) sits. */
    val jingleCx: Float = run {
        val b = layout.getBoundingBox(3)
        wordX + (b.left + b.right) / 2f
    }
    val jingleCy = baseline - s * 0.769f + jingle / 2f
}

fun wordStyle(sizeSp: Float) = TextStyle(
    fontFamily = InterFamily, fontWeight = FontWeight.ExtraBold, fontSize = sizeSp.sp,
    letterSpacing = (-0.02).em, color = DColor.Text,
)

fun TextMeasurer.lockup(sPx: Float, density: Float): LockupGeometry =
    LockupGeometry(sPx, measure("aflı", wordStyle(sPx / density)))

/** Draws the full lockup with its top-left at the current origin. */
fun DrawScope.drawLockup(g: LockupGeometry, pose: LockupPose = LockupPose(), wordColor: Color = DColor.Text) {
    // d
    withTransform({
        translate(pose.markTx, pose.markTy + g.markTop)
        scale(pose.markSx, pose.markSy, pivot = Offset(g.markW / 2f, g.markH / 2f))
    }) { drawMark(g.markW, g.markH) }
    // aflı
    if (pose.wordAlpha > 0f) translate(g.wordX + pose.wordTx, 0f) {
        drawText(g.layout, color = wordColor, alpha = pose.wordAlpha)
    }
    // jingle
    if (pose.jingleAlpha > 0f) {
        val c = Offset(g.jingleCx, g.jingleCy + pose.jingleTy)
        rotate(-12f + pose.jingleRot, pivot = c) {
            drawRoundRect(
                DColor.Marigold, topLeft = Offset(c.x - g.jingle / 2f, c.y - g.jingle / 2f), size = Size(g.jingle, g.jingle),
                cornerRadius = CornerRadius(g.jingle * 0.18f), alpha = pose.jingleAlpha,
            )
        }
    }
}

/** Static "dafli" logo. [size] = cap size of the word (42 on Welcome, 120 on splash). */
@Composable
fun DafliLockup(size: Dp, modifier: Modifier = Modifier) {
    val measurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val g = remember(size, density) { measurer.lockup(with(density) { size.toPx() }, density.density) }
    val w = with(density) { g.width.toDp() }
    val h = with(density) { g.height.toDp() }
    Canvas(modifier.size(w, h)) { drawLockup(g) }
}
