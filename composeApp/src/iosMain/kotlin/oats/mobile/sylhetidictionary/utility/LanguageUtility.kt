package oats.mobile.sylhetidictionary.utility

import co.touchlab.kermit.Logger
import oats.mobile.sylhetidictionary.data.settings.PreferencesRepository
import oats.mobile.sylhetidictionary.models.settings.Language
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.languageCode

private const val NSUserDefaultsLocalizationKey = "AppleLanguages"

actual suspend fun PreferencesRepository.refreshLanguage() {
    val locales = NSUserDefaults.standardUserDefaults.arrayForKey(NSUserDefaultsLocalizationKey)
    logger.d("LOCALE: refreshing language preference from: $locales")
    if (!locales.isNullOrEmpty()) {
        val locale = NSLocale(locales.first().toString())
        setLanguage(Language.fromCode(locale.languageCode))
    }
}

actual fun setAppOSLanguage(language: Language) {
    NSUserDefaults.standardUserDefaults.setObject(listOf(language.code), NSUserDefaultsLocalizationKey)
}
