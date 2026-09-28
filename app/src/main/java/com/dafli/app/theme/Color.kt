package com.dafli.app.theme

import androidx.compose.ui.graphics.Color

/** Dafli design tokens (dark theme first, from the Figma case study). */
object DColor {
    val Night = Color(0xFF0E0D12)       // app background
    val Surface = Color(0xFF1A1820)     // cards, mini-player
    val Elevated = Color(0xFF24212C)    // sheets, chips, fields
    val Line = Color(0xFF34313D)        // dividers, outlines
    val Text = Color(0xFFF5F3F7)        // primary text
    val TextDim = Color(0xFFABA6B5)     // secondary text
    val Disabled = Color(0xFF6E6978)
    val Marigold = Color(0xFFFFB21E)    // brand accent — only for primary action & "now playing"
    val Ink = Color(0xFF1A1206)         // text on marigold
    val Success = Color(0xFF3DD68C)
    val Error = Color(0xFFFF6B6B)
    val Info = Color(0xFF6CB6FF)
    val Scrim = Color(0x99000000)
    val Paper = Color(0xFFFAF8F5)       // light surfaces (search field)
    val MarigoldDeep = Color(0xFF9A5A00)

    // Avatar tints for people (neutral, not brand)
    val AvatarV = Color(0xFF4B3F8F)
    val AvatarA = Color(0xFF2F6E62)
    val AvatarM = Color(0xFF8A4458)
    val AvatarD = Color(0xFF3F5E8C)
    val AvatarK = Color(0xFF6B5A2E)
}
