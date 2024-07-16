package data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PreferencesRepository(private val preferences: DataStore<Preferences>) {

    private val safeSettingsFlow: Flow<Preferences>
        get() = preferences.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else throw exception
        }

    fun <T> getFlow(key: Preferences.Key<T>): Flow<T?> =
        safeSettingsFlow.map { it[key] }

    suspend fun <T> get(key: Preferences.Key<T>): T? =
        safeSettingsFlow.first()[key]

    suspend fun <T> put(key: Preferences.Key<T>, value: T) {
        preferences.edit { it[key] = value }
    }
}
