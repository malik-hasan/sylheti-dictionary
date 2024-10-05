package utility

import co.touchlab.kermit.Logger
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import models.Language
import java.util.Locale

actual suspend fun PreferencesRepository.refreshLanguage() {
    if (get(PreferenceKey.LANGUAGE).isNullOrBlank()) {
        val locale = Locale.getDefault()
        Logger.d("LOCALE: initializing language preference from: $locale")
        setLanguage(Language.fromCode(locale.language))
    }
}

actual fun setAppOSLanguage(language: Language) = Locale.setDefault(Locale(language.code))
