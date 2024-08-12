package ui.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier

fun Modifier.ifTrue(condition: Boolean, modifier: Modifier.() -> Modifier) =
    if (condition) modifier() else this

val LazyListState.isScrollingUp: State<Boolean>
    @Composable
    get() = produceState(initialValue = true) {
        var lastIndex = 0
        var lastScroll = Int.MAX_VALUE
        snapshotFlow {
            firstVisibleItemIndex to firstVisibleItemScrollOffset
        }.collect { (currentIndex, currentScroll) ->
            if (currentIndex != lastIndex || currentScroll != lastScroll) {
                value = currentIndex < lastIndex ||
                        (currentIndex == lastIndex && currentScroll < lastScroll)
                lastIndex = currentIndex
                lastScroll = currentScroll
            }
        }
    }

//private object NoRippleTheme : RippleTheme {
//    @Composable
//    override fun defaultColor() = Color.Unspecified
//
//    @Composable
//    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f,0.0f,0.0f,0.0f)
//}

///**
// * Adds an inner shadow effect to the content.
// *
// * @param shape The shape of the shadow.
// * @param color The color of the shadow.
// * @param blur The blur radius of the shadow.
// * @param offsetY The shadow offset along the Y-axis.
// * @param offsetX The shadow offset along the X-axis.
// * @param spread The amount to expand the shadow beyond its size.
// *
// * @return A modified Modifier with the inner shadow effect applied.
// */
//fun Modifier.innerShadow(
//    shape: Shape,
//    color: Color = Color.Black,
//    blur: Dp = 4.dp,
//    offsetY: Dp = 2.dp,
//    offsetX: Dp = 2.dp,
//    spread: Dp = 0.dp
//) = drawWithContent {
//
//    drawContent()
//
//    drawIntoCanvas { canvas ->
//
//        val shadowSize = Size(size.width + spread.toPx(), size.height + spread.toPx())
//        val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)
//
//        val paint = Paint().apply {
//            this.color = color
//        }
//
//        canvas.saveLayer(size.toRect(), paint)
//        canvas.drawOutline(shadowOutline, paint)
//
//        paint.asFrameworkPaint().apply {
//            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
//            if (blur.toPx() > 0) {
//                maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
//            }
//        }
//
//        paint.color = Color.Black
//
//        canvas.translate(offsetX.toPx(), offsetY.toPx())
//        canvas.drawOutline(shadowOutline, paint)
//        canvas.restore()
//    }
//}
