package ui.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun LazyListState.rememberIsScrollingUp(): State<Boolean> {
    var lastIndex by remember { mutableStateOf(0) }
    var lastScroll by remember { mutableStateOf(Int.MAX_VALUE) }

    return remember {
        derivedStateOf {
            val isScrollingUp = (firstVisibleItemIndex == lastIndex && firstVisibleItemScrollOffset < lastScroll)
                    || firstVisibleItemIndex < lastIndex

            lastIndex = firstVisibleItemIndex
            lastScroll = firstVisibleItemScrollOffset

            isScrollingUp
        }
    }
}
