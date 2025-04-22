package oats.mobile.sylhetidictionary

import android.app.Application
import co.touchlab.kermit.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import oats.mobile.sylhetidictionary.data.dictionary.DictionaryAsset
import oats.mobile.sylhetidictionary.data.dictionary.DictionaryAssetVersion
import oats.mobile.sylhetidictionary.data.settings.PreferenceKey
import oats.mobile.sylhetidictionary.data.settings.PreferencesRepository
import oats.mobile.sylhetidictionary.di.initKoin
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import sylhetidictionary.composeapp.generated.resources.Res
import java.io.FileOutputStream
import java.io.IOException

class SylhetiDictionaryApplication: Application() {

    private val preferences: PreferencesRepository by inject()
    private val logger: Logger by inject()

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@SylhetiDictionaryApplication)
        }

        runBlocking(Dispatchers.IO) {
            val currentDictionaryVersion = preferences.get(PreferenceKey.CURRENT_DICTIONARY_VERSION) ?: -1
            if (DictionaryAssetVersion > currentDictionaryVersion) {

                logger.d("INIT: copying dictionary asset to SQLite")

                var dictionaryVersion = DictionaryAssetVersion

                try {
                    val inputStream = Res.readBytes("files/$DictionaryAsset").inputStream()
                    val outputStream = FileOutputStream(getDatabasePath(DictionaryAsset).absolutePath)

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
    }
}
