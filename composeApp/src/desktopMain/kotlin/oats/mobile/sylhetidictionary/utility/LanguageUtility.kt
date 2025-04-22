package oats.mobile.sylhetidictionary.utility

import co.touchlab.kermit.Logger
import kotlinx.coroutines.delay
import oats.mobile.sylhetidictionary.data.settings.PreferenceKey
import oats.mobile.sylhetidictionary.data.settings.PreferencesRepository
import oats.mobile.sylhetidictionary.models.settings.Language
import java.util.Locale

actual suspend fun PreferencesRepository.refreshLanguage(logger: Logger) {
    val locale = Locale.getDefault()
    val languageCode = get(PreferenceKey.LANGUAGE)
    if (languageCode.isNullOrBlank()) {
        logger.d("LOCALE: initializing language preference from: $locale")
        setLanguage(Language.fromCode(locale.language))
    } else if (languageCode != locale.language) {
        val language = Language.fromCode(languageCode)
        setLanguage(Language.fromCode(locale.language))
        delay(10)
        logger.d("LOCALE: refreshing locale from preference: $language")
        setAppOSLanguage(language)
        setLanguage(language)
    }
}

actual fun setAppOSLanguage(language: Language) = Locale.setDefault(Locale(language.code))
