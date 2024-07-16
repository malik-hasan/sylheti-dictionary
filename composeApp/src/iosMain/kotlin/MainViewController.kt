
import androidx.compose.ui.window.ComposeUIViewController
import co.touchlab.kermit.Logger
import data.dictionary.DictionaryAsset
import data.dictionary.DictionaryAssetVersion
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import di.initKoin
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.mp.KoinPlatform.getKoin
import platform.Foundation.NSData
import platform.Foundation.create
import platform.Foundation.stringByAppendingPathComponent
import platform.Foundation.writeToFile
import sylhetidictionary.composeapp.generated.resources.Res

@OptIn(ExperimentalResourceApi::class, ExperimentalForeignApi::class, BetaInteropApi::class)
fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()

        val preferences = getKoin().get<PreferencesRepository>()
        MainScope().launch {
            val currentDictionaryVersion = preferences.get(PreferenceKey.CURRENT_DICTIONARY_VERSION) ?: -1
            if (DictionaryAssetVersion > currentDictionaryVersion) {

                val sourceBytes = Res.readBytes("files/$DictionaryAsset")
                val destination = documentsDirectoryPath.stringByAppendingPathComponent("databases/$DictionaryAsset")

                Logger.d("INIT: copying dictionary asset to SQLite")

                memScoped {
                    val data = NSData.create(
                        bytes = allocArrayOf(sourceBytes),
                        length = sourceBytes.size.toULong()
                    )
                    data.writeToFile(destination, true)
                }

                // TODO: enable increment
//                preferences.put(PreferenceKey.DICTIONARY_VERSION, DictionaryAssetVersion)
            }
        }
    }
) { App() }
