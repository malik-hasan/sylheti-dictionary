package oats.mobile.sylhetidictionary.ui.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp

fun DrawScope.drawVowelChartLine(start: Offset, end: Offset, color: Color) =
    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = 1.sp.toPx()
    )

fun DrawScope.drawVoweChartDot(center: Offset, color: Color): Rect {
    val radius = 4.sp.toPx()
    drawCircle(
        color = color,
        radius = 4.sp.toPx(),
        center = center
    )
    return Rect(center, radius)
}

fun DrawScope.drawVowelButton(
    vowel: TextLayoutResult,
    center: Offset,
    buttonColor: Color,
    tappedColor: Color,
    tappedVowel: Rect?
): Rect {
    val vowelSize = vowel.size
    val radius = vowelSize.height / 2f
    val rect = Rect(center, radius)
    drawCircle(
        color = if (rect == tappedVowel) tappedColor else buttonColor,
        radius = radius,
        center = center
    )
    drawText(
        textLayoutResult = vowel,
        topLeft = center - Offset(vowelSize.width / 2f, radius)
    )
    return rect
}
