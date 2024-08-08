package oats.mobile.sylhetidictionary

import android.app.Application
import co.touchlab.kermit.Logger
import data.dictionary.DictionaryAsset
import data.dictionary.DictionaryAssetVersion
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import di.initKoin
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import sylhetidictionary.composeapp.generated.resources.Res
import java.io.FileOutputStream
import java.io.IOException

class SylhetiDictionaryApplication: Application() {

    private val preferences: PreferencesRepository by inject()

    @OptIn(ExperimentalResourceApi::class)
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@SylhetiDictionaryApplication)
        }

        runBlocking {
            val currentDictionaryVersion = preferences.get(PreferenceKey.CURRENT_DICTIONARY_VERSION) ?: -1
            if (DictionaryAssetVersion > currentDictionaryVersion) {

                Logger.d("INIT: copying dictionary asset to SQLite")

                var dictionaryVersion = DictionaryAssetVersion

                try {
                    val inputStream = Res.readBytes("files/$DictionaryAsset").inputStream()
                    val outputStream = FileOutputStream(getDatabasePath(DictionaryAsset).absolutePath)

                    inputStream.use { input ->
                        outputStream.use {
                            input.copyTo(it)
                        }
                    }
                    Logger.d("INIT: dictionary asset copied")
                } catch(e: IOException) {
                    dictionaryVersion = -1 // failure
                }

                preferences.put(PreferenceKey.CURRENT_DICTIONARY_VERSION, dictionaryVersion)
            }
        }
    }
}
