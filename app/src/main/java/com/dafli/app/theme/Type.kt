package com.dafli.app.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.dafli.app.platform.InterFamily

/** Type scale used in Figma (Inter). */
object DType {
    private fun s(size: Int, weight: FontWeight, line: TextUnit = (size * 1.3f).sp, ls: TextUnit = 0.sp) =
        TextStyle(fontFamily = InterFamily, fontSize = size.sp, fontWeight = weight, lineHeight = line, letterSpacing = ls, color = DColor.Text)

    val Display = s(44, FontWeight.Bold, 48.sp, (-0.02).em)
    val H1 = s(28, FontWeight.Bold, 34.sp, (-0.01).em)
    val H2 = s(26, FontWeight.Bold, 32.sp, (-0.01).em)
    val H3 = s(24, FontWeight.Bold, 30.sp, (-0.01).em)
    val Title = s(20, FontWeight.Bold, 26.sp)
    val TitleS = s(17, FontWeight.SemiBold, 22.sp)
    val Body = s(15, FontWeight.Normal, 21.sp)
    val BodyStrong = s(15, FontWeight.SemiBold, 20.sp)
    val BodyL = s(16, FontWeight.Medium, 22.sp)
    val Button = s(16, FontWeight.SemiBold, 20.sp)
    val Label = s(14, FontWeight.SemiBold, 18.sp)
    val Meta = s(13, FontWeight.Normal, 18.sp)
    val Small = s(12, FontWeight.Normal, 16.sp)
    val Overline = s(12, FontWeight.SemiBold, 16.sp, 0.06.em)
    val Tiny = s(11, FontWeight.Medium, 14.sp)
}
