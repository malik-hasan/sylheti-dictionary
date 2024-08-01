package di

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

@Composable
actual fun <T> StateFlow<T>.collectAsStateForPlatform() = collectAsStateWithLifecycle()

actual fun setLanguage(languageCode: String, saveToPreferences: () -> Unit) {
    Locale.setDefault(Locale(languageCode))
}
