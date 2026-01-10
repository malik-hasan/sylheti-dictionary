
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import oats.mobile.sylhetidictionary.data.preferences.PreferenceKey
import oats.mobile.sylhetidictionary.data.preferences.PreferencesRepository
import oats.mobile.sylhetidictionary.di.utils.initKoin
import oats.mobile.sylhetidictionary.di.utils.injectLogger
import oats.mobile.sylhetidictionary.ui.app.App
import oats.mobile.sylhetidictionary.utility.DictionaryAsset
import oats.mobile.sylhetidictionary.utility.DictionaryAssetVersion
import oats.mobile.sylhetidictionary.utility.readDictionaryAsset
import org.koin.mp.KoinPlatform.getKoin
import java.io.FileOutputStream

fun main() {
    initKoin()

    val koin = getKoin()
    val preferences: PreferencesRepository by koin.inject()
    val logger: Logger by koin.injectLogger()
    val scope: CoroutineScope by koin.inject()

    scope.launch(Dispatchers.IO) {
        if (DictionaryAssetVersion > (preferences.get(PreferenceKey.CURRENT_DICTIONARY_VERSION) ?: -1))
            try {
                logger.d("INIT: copying dictionary asset $DictionaryAssetVersion to SQLite")
                readDictionaryAsset().inputStream().use { input ->
                    FileOutputStream(DictionaryAsset).use { output ->
                        input.copyTo(output)
                    }
                }

                logger.d("INIT: dictionary asset copied successfully")
                preferences.set(PreferenceKey.CURRENT_DICTIONARY_VERSION, DictionaryAssetVersion)
            } catch(e: Exception) {
                logger.e("INIT: failed to copy dictionary asset: ${e.message}")
            }
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Sylheti Dictionary",
        ) {
            App()
        }
    }
}
