package utility

import co.touchlab.kermit.Logger
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import models.Language
import java.util.Locale

actual suspend fun PreferencesRepository.refreshLanguage() {
    val languageCode = get(PreferenceKey.LANGUAGE) ?: Locale.getDefault().language
    Logger.d("LOCALE: refreshing language preference from: $languageCode")
    setLanguage(Language.fromCode(languageCode))
}

actual fun setAppOSLanguage(language: Language) = Locale.setDefault(Locale(language.code))
