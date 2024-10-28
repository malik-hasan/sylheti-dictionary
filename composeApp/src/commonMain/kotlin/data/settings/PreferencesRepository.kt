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
import models.Language
import models.search.settings.SearchLanguage
import models.search.settings.SearchPosition
import models.search.settings.SearchScript

class PreferencesRepository(private val preferences: DataStore<Preferences>) {

    private val safePreferencesFlow: Flow<Preferences>
        get() = preferences.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else throw exception
        }

    suspend fun <T> get(key: Preferences.Key<T>): T? =
        safePreferencesFlow.first()[key]

    fun <T> flow(key: Preferences.Key<T>, default: T): Flow<T> =
        safePreferencesFlow.map { it[key] ?: default }

    fun <T> nullableFlow(key: Preferences.Key<T>): Flow<T?> =
        safePreferencesFlow.map { it[key] }

    val searchPosition: Flow<SearchPosition>
        get() = flow(PreferenceKey.SEARCH_POSITION, 0).map {
            SearchPosition.entries[it]
        }

    val searchScript: Flow<SearchScript>
        get() = flow(PreferenceKey.SEARCH_SCRIPT, 0).map {
            SearchScript.entries[it]
        }

    val searchLanguages: Flow<Map<SearchLanguage, Boolean>>
        get() {
            val languageFlows = SearchLanguage.entries.map { language ->
                flow(language.settingsKey, true).map { value ->
                    language to value
                }
            }.toTypedArray()

            return combine(*languageFlows) { it.toMap() }
        }

    val language: Flow<Language>
        get() = flow(PreferenceKey.LANGUAGE, Language.EN.code).map(Language::fromCode)

    suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        preferences.edit { it[key] = value }
    }

    suspend fun setLanguage(language: Language) = set(PreferenceKey.LANGUAGE, language.code)
}
