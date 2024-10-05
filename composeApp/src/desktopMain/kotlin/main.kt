
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import co.touchlab.kermit.Logger
import data.dictionary.DictionaryAsset
import data.dictionary.DictionaryAssetVersion
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import di.initKoin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.mp.KoinPlatform.getKoin
import sylhetidictionary.composeapp.generated.resources.Res
import java.io.FileOutputStream
import java.io.IOException

@OptIn(ExperimentalResourceApi::class)
fun main() {
    initKoin()

    val preferences: PreferencesRepository = getKoin().get()
    runBlocking(Dispatchers.IO) {

        val currentDictionaryVersion = preferences.get(PreferenceKey.CURRENT_DICTIONARY_VERSION) ?: -1
        if (DictionaryAssetVersion > currentDictionaryVersion) {

            Logger.d("INIT: copying dictionary asset to SQLite")

            var dictionaryVersion = DictionaryAssetVersion

            try {
                val inputStream = Res.readBytes("files/$DictionaryAsset").inputStream()
                val outputStream = FileOutputStream(DictionaryAsset)

                inputStream.use { input ->
                    outputStream.use {
                        input.copyTo(it)
                    }
                }
                Logger.d("INIT: dictionary asset copied")
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
