package di

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow

@Composable
actual fun <T> StateFlow<T>.collectAsStateForPlatform() = collectAsStateWithLifecycle()

actual fun setLanguage(languageCode: String, saveToPreferences: () -> Unit) {
    AppCompatDelegate.setApplicationLocales(
        LocaleListCompat.forLanguageTags(languageCode)
    )

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        saveToPreferences()
    }
}
