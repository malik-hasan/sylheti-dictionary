package oats.mobile.sylhetidictionary.ui.theme

import android.os.Build
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun SDTheme(
    dynamicTheme: Boolean,
    darkTheme: Boolean,
    content: @Composable () -> Unit
) = SDTheme(
    colorScheme = when {
        // Dynamic color is available on Android 12+
        dynamicTheme && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    },
    content = content
)
