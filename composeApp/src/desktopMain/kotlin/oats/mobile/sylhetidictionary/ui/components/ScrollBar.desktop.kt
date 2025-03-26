package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable

@Composable
actual fun ScrollBar(lazyListState: LazyListState) {
    VerticalScrollbar(ScrollbarAdapter(lazyListState))
}
