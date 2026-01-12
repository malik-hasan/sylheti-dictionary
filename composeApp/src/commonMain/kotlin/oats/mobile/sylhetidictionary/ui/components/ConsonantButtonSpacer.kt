package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

@Composable
fun ConsonantButtonSpacer(density: Density = LocalDensity.current,) =
    Spacer(Modifier.width(
        with(density) {
            MaterialTheme.typography.bodyLarge.lineHeight.toDp()
        }
    ))
