package di

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import platform.Foundation.NSUserDefaults

@Composable
actual fun <T> StateFlow<T>.collectAsStateForPlatform() = collectAsStateWithLifecycle()

actual fun setLanguage(languageCode: String, saveToPreferences: () -> Unit) {
    NSUserDefaults.standardUserDefaults.setObject(arrayListOf(languageCode), "AppleLanguages")
    saveToPreferences()
}
