package oats.mobile.sylhetidictionary

import android.app.Application
import co.touchlab.kermit.Logger
import data.dictionary.DictionaryAsset
import data.dictionary.DictionaryAssetVersion
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import di.initKoin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import java.io.FileOutputStream

class AndroidDictionaryApp: Application() {
    private val preferences: PreferencesRepository by inject()


    companion object {
        var appScope = MainScope()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        appScope.cancel("onLowMemory() called by system")
        appScope = MainScope()
    }

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@AndroidDictionaryApp)
        }

        appScope.launch(Dispatchers.IO) {
            val currentDictionaryVersion = preferences.get(PreferenceKey.CURRENT_DICTIONARY_VERSION) ?: -1
            if (DictionaryAssetVersion > currentDictionaryVersion) {

                Logger.d("INIT: copying dictionary asset to SQLite")
                val database = getDatabasePath(DictionaryAsset)

                val inputStream = assets.open(DictionaryAsset)
                val outputStream = FileOutputStream(database.absolutePath)

                inputStream.use { input ->
                    outputStream.use {
                        input.copyTo(it)
                    }
                }

                // TODO: enable increment
//                preferences.put(PreferenceKey.DICTIONARY_VERSION, DictionaryAssetVersion)
            }
        }
    }
}
