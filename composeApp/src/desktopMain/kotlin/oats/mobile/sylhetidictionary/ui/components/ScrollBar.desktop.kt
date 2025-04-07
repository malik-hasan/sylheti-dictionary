package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import oats.mobile.sylhetidictionary.ui.utils.ifTrue

@Composable
actual fun ScrollBar(
    lazyListState: LazyListState,
    scrollCharIndexes: Map<Char, Int>,
    modifier: Modifier,
    density: Density,
    scrollingFromScrollBar: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isDragged by interactionSource.collectIsDraggedAsState()

    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(lazyListState),
        modifier = modifier
            .ifTrue(isHovered || isDragged) {
                background(MaterialTheme.colorScheme.surfaceContainer)
            }.padding(4.dp),
        interactionSource = interactionSource,
        style = LocalScrollbarStyle.current.copy(
            unhoverColor = MaterialTheme.colorScheme.tertiary,
            hoverColor = MaterialTheme.colorScheme.primary
        )
    )
}
