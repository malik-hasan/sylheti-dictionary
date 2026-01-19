package oats.mobile.sylhetidictionary

import android.app.Application
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import oats.mobile.sylhetidictionary.data.preferences.PreferenceKey
import oats.mobile.sylhetidictionary.data.preferences.PreferencesRepository
import oats.mobile.sylhetidictionary.di.utils.initKoin
import oats.mobile.sylhetidictionary.utility.DictionaryAsset
import oats.mobile.sylhetidictionary.utility.DictionaryAssetVersion
import oats.mobile.sylhetidictionary.utility.readDictionaryAsset
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.parametersOf
import java.io.FileOutputStream

class SylhetiDictionaryApplication: Application() {

    private val scope: CoroutineScope by inject()
    private val preferences: PreferencesRepository by inject()
    private val logger: Logger by inject { parametersOf(this::class.simpleName) }

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@SylhetiDictionaryApplication)
        }

        scope.launch(Dispatchers.IO) {
            if (DictionaryAssetVersion > (preferences.get(PreferenceKey.CURRENT_DICTIONARY_VERSION) ?: -1)) {
                var loadedDictionaryVersion = -1
                try {
                    logger.d("INIT: copying dictionary asset $DictionaryAssetVersion to SQLite")
                    readDictionaryAsset().inputStream().use { input ->
                        FileOutputStream(getDatabasePath(DictionaryAsset).absolutePath).use { output ->
                            input.copyTo(output)
                        }
                    }

                    logger.d("INIT: dictionary asset copied successfully")
                    loadedDictionaryVersion = DictionaryAssetVersion
                } catch (e: Exception) {
                    logger.e("INIT: failed to copy dictionary asset: ${e.message}")
                }

                preferences.set(PreferenceKey.CURRENT_DICTIONARY_VERSION, loadedDictionaryVersion)
            }
        }
    }
}
