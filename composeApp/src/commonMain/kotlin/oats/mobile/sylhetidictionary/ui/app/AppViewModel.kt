package oats.mobile.sylhetidictionary.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import oats.mobile.sylhetidictionary.data.preferences.PreferenceKey
import oats.mobile.sylhetidictionary.data.preferences.PreferencesRepository
import oats.mobile.sylhetidictionary.data.preferences.models.settings.Language
import oats.mobile.sylhetidictionary.data.preferences.models.settings.Theme
import oats.mobile.sylhetidictionary.ui.utils.stateFlowOf
import oats.mobile.sylhetidictionary.utility.DictionaryAssetVersion
import oats.mobile.sylhetidictionary.utility.refreshLanguage

class AppViewModel(val preferences: PreferencesRepository): ViewModel() {

    val assetLoaded = stateFlowOf(null,
        preferences.nullableFlow(PreferenceKey.CURRENT_DICTIONARY_VERSION).map { version ->
            version?.let { it == DictionaryAssetVersion }
        }
    )

    private val languageLoaded = preferences.language
    val language = stateFlowOf(Language.EN, languageLoaded)

    fun refreshLanguage() {
        viewModelScope.launch {
            preferences.refreshLanguage()
        }
    }

    private val themeLoaded = preferences.theme
    val theme = stateFlowOf(Theme.Auto, themeLoaded)

    private val dynamicThemeLoaded = preferences.flow(PreferenceKey.DYNAMIC_THEME, true)
    val dynamicTheme = stateFlowOf(true, dynamicThemeLoaded)

    val appDataLoaded = stateFlowOf(false,
        combine(
            assetLoaded,
            languageLoaded,
            themeLoaded,
            dynamicThemeLoaded
        ) { assetLoaded, _, _, _ ->
            assetLoaded != null
        }
    )
}
