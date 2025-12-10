package oats.mobile.sylhetidictionary.ui.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.runtime.annotation.FrequentlyChangingValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Velocity

@OptIn(ExperimentalMaterial3Api::class)
class PinnedSearchBarScrollBehavior(
    initialContentOffset: Float = 0f
) : SearchBarScrollBehavior {

    override var scrollOffset = 0f

    override var scrollOffsetLimit by mutableFloatStateOf(-Float.MAX_VALUE)

    @get:FrequentlyChangingValue
    override var contentOffset by mutableFloatStateOf(initialContentOffset)

    override fun Modifier.searchBarScrollBehavior() =
        onSizeChanged { scrollOffsetLimit = -it.height.toFloat() }

    override val nestedScrollConnection = object : NestedScrollConnection {
        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {
            contentOffset += consumed.y
            return Offset.Zero
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            if (available.y > 0f) contentOffset = 0f
            return Velocity.Zero
        }
    }
}
