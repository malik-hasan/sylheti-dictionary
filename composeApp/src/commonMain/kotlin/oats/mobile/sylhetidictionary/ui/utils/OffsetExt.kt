package oats.mobile.sylhetidictionary.ui.utils

import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun Offset.rotate(degrees: Float, pivot: Offset = Offset.Zero): Offset {
    val dx = x - pivot.x
    val dy = y - pivot.y

    val radians = PI / 180 * degrees
    val cosTheta = cos(radians)
    val sinTheta = sin(radians)

    val newDx = dx * cosTheta - dy * sinTheta
    val newDy = dx * sinTheta + dy * cosTheta

    val rotatedX = newDx + pivot.x
    val rotatedY = newDy + pivot.y

    return Offset(rotatedX.toFloat(), rotatedY.toFloat())
}
