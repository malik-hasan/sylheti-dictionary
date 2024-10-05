package data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import models.search.settings.SearchLanguage
import models.search.settings.SearchPosition

class PreferencesRepository(private val preferences: DataStore<Preferences>) {

    private val safePreferencesFlow: Flow<Preferences>
        get() = preferences.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else throw exception
        }

    fun <T> flow(key: Preferences.Key<T>, default: T): Flow<T> =
        safePreferencesFlow.map { it[key] ?: default }

    suspend fun <T> get(key: Preferences.Key<T>): T? =
        safePreferencesFlow.first()[key]

    suspend fun <T> put(key: Preferences.Key<T>, value: T) {
        preferences.edit { it[key] = value }
    }

    val searchPositionsFlow: Flow<List<Boolean>>
        get() {
            val positionFlows = SearchPosition.entries.map {
                flow(it.settingsKey, false)
            }.toTypedArray()

            return combine(*positionFlows) { it.toList() }
        }

    val searchLanguagesFlow: Flow<Map<SearchLanguage, Boolean>>
        get() {
            val languageFlows = SearchLanguage.entries.map { language ->
                flow(language.settingsKey, false).map { value ->
                    language to value
                }
            }.toTypedArray()

            return combine(*languageFlows) { it.toMap() }
        }
}
