package utility

import co.touchlab.kermit.Logger
import data.settings.PreferencesRepository
import models.Language
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.languageCode

private const val NSUserDefaultsLocalizationKey = "AppleLanguages"

actual suspend fun PreferencesRepository.refreshLanguage() {
    val locales = NSUserDefaults.standardUserDefaults.arrayForKey(NSUserDefaultsLocalizationKey)
    Logger.d("LOCALE: refreshing language preference from: $locales")
    if (!locales.isNullOrEmpty()) {
        val locale = NSLocale(locales.first().toString())
        setLanguage(Language.fromCode(locale.languageCode))
    }
}

actual fun setAppOSLanguage(language: Language) {
    NSUserDefaults.standardUserDefaults.setObject(listOf(language.code), NSUserDefaultsLocalizationKey)
}
