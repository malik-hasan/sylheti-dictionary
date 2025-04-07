package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

@Composable
expect fun ScrollBar(
    lazyListState: LazyListState,
    scrollCharIndexes: Map<Char, Int>,
    modifier: Modifier = Modifier,
    density: Density = LocalDensity.current,
    scrollingFromScrollBar: () -> Unit
)
