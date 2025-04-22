
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import co.touchlab.kermit.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import oats.mobile.sylhetidictionary.data.dictionary.DictionaryAsset
import oats.mobile.sylhetidictionary.data.dictionary.DictionaryAssetVersion
import oats.mobile.sylhetidictionary.data.settings.PreferenceKey
import oats.mobile.sylhetidictionary.data.settings.PreferencesRepository
import oats.mobile.sylhetidictionary.di.initKoin
import oats.mobile.sylhetidictionary.ui.app.App
import org.koin.mp.KoinPlatform.getKoin
import sylhetidictionary.composeapp.generated.resources.Res
import java.io.FileOutputStream
import java.io.IOException

fun main() {
    initKoin()

    val koin = getKoin()
    val preferences: PreferencesRepository by koin.inject()
    val logger: Logger by koin.inject()

    runBlocking(Dispatchers.IO) {
        val currentDictionaryVersion = preferences.get(PreferenceKey.CURRENT_DICTIONARY_VERSION) ?: -1
        if (DictionaryAssetVersion > currentDictionaryVersion) {

            logger.d("INIT: copying dictionary asset to SQLite")

            var dictionaryVersion = DictionaryAssetVersion

            try {
                val inputStream = Res.readBytes("files/$DictionaryAsset").inputStream()
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
