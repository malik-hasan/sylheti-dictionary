package di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.StateFlow

@Composable
expect fun <T> StateFlow<T>.collectAsStateForPlatform(): State<T>

expect fun setLanguage(languageCode: String, saveToPreferences: () -> Unit = {})
