package utility

import co.touchlab.kermit.Logger
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import kotlinx.coroutines.delay
import models.Language
import java.util.Locale

actual suspend fun PreferencesRepository.refreshLanguage() {
    val locale = Locale.getDefault()
    val languageCode = get(PreferenceKey.LANGUAGE)
    if (languageCode.isNullOrBlank()) {
        Logger.d("LOCALE: initializing language preference from: $locale")
        setLanguage(Language.fromCode(locale.language))
    } else if (languageCode != locale.language) {
        val language = Language.fromCode(languageCode)
        setLanguage(Language.fromCode(locale.language))
        delay(10)
        Logger.d("LOCALE: refreshing locale from preference: $language")
        setAppOSLanguage(language)
        setLanguage(language)
    }
}

actual fun setAppOSLanguage(language: Language) = Locale.setDefault(Locale(language.code))
