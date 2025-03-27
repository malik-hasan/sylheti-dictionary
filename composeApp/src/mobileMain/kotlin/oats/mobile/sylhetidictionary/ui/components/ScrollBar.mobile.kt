package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import co.touchlab.kermit.Logger
import kotlinx.coroutines.delay
import oats.mobile.sylhetidictionary.ui.theme.latinDisplayFontFamily
import oats.mobile.sylhetidictionary.ui.utils.isLegible
import kotlin.math.floor

@Composable
actual fun ScrollBar(
    lazyListState: LazyListState,
    scrollCharIndexes: Map<Char, Int>,
    density: Density,
    scrollingFromScrollBar: () -> Unit
) {
    val scrollChars = remember(scrollCharIndexes) { scrollCharIndexes.keys.toList() }

    var scrollBarDragOffset by remember { mutableStateOf<Float?>(null) }

    var scrollBarHeight by remember { mutableIntStateOf(0) }

    val scrollCharHeight by remember {
        derivedStateOf { scrollBarHeight / scrollChars.size }
    }

    val touchedChar by remember {
        derivedStateOf {
            scrollBarDragOffset?.takeIf { 0 <= it && it < scrollBarHeight }?.let {
                val charIndex = floor(it / scrollCharHeight).toInt()
                scrollChars[charIndex]
            }
        }
    }

    LaunchedEffect(touchedChar) {
        scrollCharIndexes[touchedChar]?.let { itemIndex ->
            scrollingFromScrollBar()
            lazyListState.scrollToItem(itemIndex)
        }
    }

    var scrollCharsMeasured by remember(scrollBarHeight) { mutableStateOf(false) }

    val labelLarge = MaterialTheme.typography.labelLarge
    var scrollCharStyle by remember(scrollBarHeight) { mutableStateOf(labelLarge) }

    val surfaceContainerColor = MaterialTheme.colorScheme.surfaceContainer
    val scrollBarBackgroundColor by remember(scrollBarHeight) {
        derivedStateOf {
            if (scrollBarDragOffset != null || !scrollCharStyle.fontSize.isLegible) {
                surfaceContainerColor
            } else Color.Unspecified
        }
    }

    Column(
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .widthIn(24.dp)
            .background(scrollBarBackgroundColor)
            .padding(vertical = 4.dp)
            .onSizeChanged {
                scrollBarHeight = it.height
            }.draggable(
                state = rememberDraggableState { delta ->
                    scrollBarDragOffset = scrollBarDragOffset?.plus(delta)
                },
                startDragImmediately = true,
                orientation = Orientation.Vertical,
                onDragStarted = { scrollBarDragOffset = it.y },
                onDragStopped = {
                    delay(400)
                    scrollBarDragOffset = null
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        scrollChars.forEach { char ->
            Text(
                text = char.toString(),
                textAlign = TextAlign.Center,
                style = scrollCharStyle,
                softWrap = false,
                fontFamily = latinDisplayFontFamily,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .weight(1f, false)
                    .fillMaxWidth()
                    .drawWithContent {
                        if (scrollCharsMeasured) drawContent()
                    },
                onTextLayout = { textLayoutResult ->
                    if (!scrollCharsMeasured) {
                        scrollCharsMeasured = if (textLayoutResult.didOverflowHeight && scrollCharStyle.fontSize.isLegible) {
                            val scaledDownFontSize = (scrollCharStyle.fontSize * 0.9f).takeIf { it.isLegible } ?: 1.sp
                            Logger.d("SEARCH: scroll char scaledDownFontSize: $scaledDownFontSize")
                            scrollCharStyle = scrollCharStyle.copy(fontSize = scaledDownFontSize)
                            false
                        } else true
                    }
                }
            )
        }

        val indicatorOffsetAdjustment = remember(density) { with(density) { 14.dp.toPx() } }
        scrollBarDragOffset?.let { dragOffset ->
            touchedChar?.let { char ->
                Popup(offset = Offset(0f, dragOffset - indicatorOffsetAdjustment).round()) {
                    Box(
                        modifier = Modifier
                            .offset(x = 12.dp)
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.tertiary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char.toString(),
                            color = MaterialTheme.colorScheme.onTertiary,
                            fontFamily = latinDisplayFontFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
