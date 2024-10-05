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
import utility.setAppOSLanguage

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

    val languageFlow: Flow<Language>
        get() = flow(PreferenceKey.LANGUAGE, Language.EN.code).map { code ->
            Language.fromCode(code)
        }

    suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        preferences.edit { it[key] = value }
    }

    suspend fun setLanguage(language: Language) = set(PreferenceKey.LANGUAGE, language.code)
    
    suspend fun setLanguagePrefAndOS(language: Language) {
        setAppOSLanguage(language)
        setLanguage(language)
    }
}
