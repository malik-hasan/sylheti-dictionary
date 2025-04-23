package oats.mobile.sylhetidictionary

import androidx.compose.ui.window.ComposeUIViewController
import co.touchlab.kermit.Logger
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking
import oats.mobile.sylhetidictionary.data.dictionary.DictionaryAsset
import oats.mobile.sylhetidictionary.data.dictionary.DictionaryAssetVersion
import oats.mobile.sylhetidictionary.data.preferences.PreferenceKey
import oats.mobile.sylhetidictionary.data.preferences.PreferencesRepository
import oats.mobile.sylhetidictionary.di.initKoin
import oats.mobile.sylhetidictionary.di.injectLogger
import oats.mobile.sylhetidictionary.ui.app.App
import oats.mobile.sylhetidictionary.utility.path
import org.koin.mp.KoinPlatform.getKoin
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.create
import platform.Foundation.stringByAppendingPathComponent
import platform.Foundation.writeToFile
import sylhetidictionary.composeapp.generated.resources.Res

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@Suppress("Unused", "FunctionName")
fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()

        val koin = getKoin()
        val preferences: PreferencesRepository by koin.inject()
        val logger: Logger by koin.injectLogger()

        runBlocking(Dispatchers.IO) {
            val currentDictionaryVersion = preferences.get(PreferenceKey.CURRENT_DICTIONARY_VERSION) ?: -1
            if (DictionaryAssetVersion > currentDictionaryVersion) {

                val sourceBytes = Res.readBytes("files/$DictionaryAsset")
                val destinationDirectory = NSApplicationSupportDirectory.path.stringByAppendingPathComponent("databases")

                logger.d("INIT: copying dictionary asset to SQLite")

                memScoped {
                    val error: ObjCObjectVar<NSError?> = alloc()

                    val createDirectorySuccess = NSFileManager.defaultManager.createDirectoryAtPath(
                        path = destinationDirectory,
                        withIntermediateDirectories = true,
                        attributes = null,
                        error = error.ptr
                    )
                    logger.d("INIT: database directory created: $createDirectorySuccess")

                    val copyAssetSuccess = NSData.create(
                        bytes = allocArrayOf(sourceBytes),
                        length = sourceBytes.size.toULong()
                    ).writeToFile("$destinationDirectory/$DictionaryAsset", true)
                    logger.d("INIT: dictionary asset copied: $copyAssetSuccess")

                    val dictionaryVersion = if (copyAssetSuccess) {
                        DictionaryAssetVersion
                    } else -1 // failure

                    preferences.set(PreferenceKey.CURRENT_DICTIONARY_VERSION, dictionaryVersion)
                }
            }
        }
    }
) { App() }
