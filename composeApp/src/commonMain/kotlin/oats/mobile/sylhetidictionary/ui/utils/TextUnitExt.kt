package oats.mobile.sylhetidictionary.ui.utils

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

val TextUnit.isLegible
    get() = this >= 8.sp
