package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Density

@Composable
actual fun ScrollBar(
    lazyListState: LazyListState,
    scrollCharIndexes: Map<Char, Int>,
    density: Density,
    scrollingFromScrollBar: () -> Unit
) {
    VerticalScrollbar(ScrollbarAdapter(lazyListState))
}
