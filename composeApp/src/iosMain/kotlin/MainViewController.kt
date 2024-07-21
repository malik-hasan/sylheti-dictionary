
import androidx.compose.ui.window.ComposeUIViewController
import co.touchlab.kermit.Logger
import data.dictionary.DictionaryAsset
import data.dictionary.DictionaryAssetVersion
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import di.initKoin
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.mp.KoinPlatform.getKoin
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
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
                val destinationDirectory = NSApplicationSupportDirectory.path.stringByAppendingPathComponent("databases")

                Logger.d("INIT: copying dictionary asset to $destinationDirectory")

                memScoped {
                    val error: ObjCObjectVar<NSError?> = alloc()

                    val createDirectoryResult = NSFileManager.defaultManager.createDirectoryAtPath(
                        path = destinationDirectory,
                        withIntermediateDirectories = true,
                        attributes = null,
                        error = error.ptr
                    )
                    Logger.d("INIT: database directory created: $createDirectoryResult")

                    val copyAssetResult = NSData.create(
                        bytes = allocArrayOf(sourceBytes),
                        length = sourceBytes.size.toULong()
                    ).writeToFile("$destinationDirectory/$DictionaryAsset", true)
                    Logger.d("INIT: dictionary asset copied: $copyAssetResult")
                }

                // TODO: enable increment
//                preferences.put(PreferenceKey.DICTIONARY_VERSION, DictionaryAssetVersion)
            }
        }
    }
) { App() }
