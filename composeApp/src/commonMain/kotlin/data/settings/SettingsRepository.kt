package data.settings

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

class SettingsRepository(private val context: Context) {
    private val Context.dataStore by preferencesDataStore("settings")
    private val settings
        get() = context.dataStore

    private val safeSettingsFlow: Flow<Preferences>
        get() = settings.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else throw exception
        }

    fun <T> readFlow(key: Preferences.Key<T>): Flow<T?> =
        safeSettingsFlow.map { it[key] }

    suspend fun <T> readValue(key: Preferences.Key<T>): T? =
        safeSettingsFlow.first()[key]

    suspend fun <T> write(key: Preferences.Key<T>, value: T) {
        settings.edit { it[key] = value }
    }
}
