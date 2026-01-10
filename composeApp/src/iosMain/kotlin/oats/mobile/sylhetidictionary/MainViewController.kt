package oats.mobile.sylhetidictionary

import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import oats.mobile.sylhetidictionary.data.preferences.PreferenceKey
import oats.mobile.sylhetidictionary.data.preferences.PreferencesRepository
import oats.mobile.sylhetidictionary.di.utils.initKoin
import oats.mobile.sylhetidictionary.di.utils.injectLogger
import oats.mobile.sylhetidictionary.ui.app.App
import oats.mobile.sylhetidictionary.utility.DictionaryAsset
import oats.mobile.sylhetidictionary.utility.DictionaryAssetVersion
import oats.mobile.sylhetidictionary.utility.path
import oats.mobile.sylhetidictionary.utility.readDictionaryAsset
import okio.IOException
import org.koin.mp.KoinPlatform.getKoin
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.create
import platform.Foundation.stringByAppendingPathComponent
import platform.Foundation.writeToFile

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@Suppress("Unused", "FunctionName")
fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()

        val koin = getKoin()
        val scope: CoroutineScope by koin.inject()
        val preferences: PreferencesRepository by koin.inject()
        val logger by koin.injectLogger(this::class.simpleName)

        scope.launch(Dispatchers.IO) {
            if (DictionaryAssetVersion > (preferences.get(PreferenceKey.CURRENT_DICTIONARY_VERSION) ?: -1))
                try {
                    logger.d("INIT: copying dictionary asset $DictionaryAssetVersion to SQLite")

                    val destinationDirectory = NSApplicationSupportDirectory.path
                        .stringByAppendingPathComponent("databases")

                    memScoped {
                        val error: ObjCObjectVar<NSError?> = alloc()
                        if (!NSFileManager.defaultManager.createDirectoryAtPath(
                            path = destinationDirectory,
                            withIntermediateDirectories = true,
                            attributes = null,
                            error = error.ptr
                        )) throw IOException("failed to create directory: ${error.value?.localizedDescription}")
                    }

                    if (!readDictionaryAsset().usePinned { pinned ->
                        NSData.create(
                            bytes = pinned.addressOf(0),
                            length = pinned.get().size.toULong()
                        )
                    }.writeToFile(
                        path = "$destinationDirectory/$DictionaryAsset",
                        atomically = true
                    )) throw IOException("failed to copy file")

                    logger.d("INIT: dictionary asset copied successfully")
                    preferences.set(PreferenceKey.CURRENT_DICTIONARY_VERSION, DictionaryAssetVersion)
                } catch (e: Exception) {
                    logger.e("INIT: failed to copy dictionary asset: ${e.message}")
                }
        }
    }
) { App() }
