
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import co.touchlab.kermit.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
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
import java.io.IOException

fun main() {
    initKoin()

    val koin = getKoin()
    val preferences: PreferencesRepository by koin.inject()
    val logger: Logger by koin.injectLogger()

    runBlocking(Dispatchers.IO) {
        val currentDictionaryVersion = preferences.get(PreferenceKey.CURRENT_DICTIONARY_VERSION) ?: -1
        if (DictionaryAssetVersion > currentDictionaryVersion) {

            logger.d("INIT: copying dictionary asset to SQLite")

            var dictionaryVersion = DictionaryAssetVersion

            try {
                val inputStream = readDictionaryAsset().inputStream()
                val outputStream = FileOutputStream(DictionaryAsset)

                inputStream.use { input ->
                    outputStream.use {
                        input.copyTo(it)
                    }
                }
                logger.d("INIT: dictionary asset copied")
            } catch(e: IOException) {
                dictionaryVersion = -1 // failure
            }

            preferences.set(PreferenceKey.CURRENT_DICTIONARY_VERSION, dictionaryVersion)
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
