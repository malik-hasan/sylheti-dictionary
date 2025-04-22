package oats.mobile.sylhetidictionary.utility

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import co.touchlab.kermit.Logger
import oats.mobile.sylhetidictionary.data.settings.PreferencesRepository
import oats.mobile.sylhetidictionary.models.settings.Language
import java.util.Locale

actual suspend fun PreferencesRepository.refreshLanguage() {
    val locale = AppCompatDelegate.getApplicationLocales().get(0) ?: Locale.getDefault()
    Logger.withTag("refreshLanguage").d("LOCALE: refreshing language preference from: $locale")
    setLanguage(Language.fromCode(locale.language))
}

actual fun setAppOSLanguage(language: Language) {
    AppCompatDelegate.setApplicationLocales(
        LocaleListCompat.forLanguageTags(language.code)
    )
}
