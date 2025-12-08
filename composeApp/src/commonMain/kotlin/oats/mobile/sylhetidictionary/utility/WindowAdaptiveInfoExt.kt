package oats.mobile.sylhetidictionary.utility

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.window.core.layout.WindowWidthSizeClass

val WindowAdaptiveInfo.isCompactWidth
    get() = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT
