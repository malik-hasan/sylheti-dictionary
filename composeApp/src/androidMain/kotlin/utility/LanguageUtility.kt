package utility

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import co.touchlab.kermit.Logger
import data.settings.PreferencesRepository
import models.Language
import java.util.Locale

actual suspend fun PreferencesRepository.refreshLanguage() {
    val locale = AppCompatDelegate.getApplicationLocales().get(0) ?: Locale.getDefault()
    Logger.d("LOCALE: refreshing language preference from: $locale")
    setLanguage(Language.fromCode(locale.language))
}

actual fun setAppOSLanguage(language: Language) {
    AppCompatDelegate.setApplicationLocales(
        LocaleListCompat.forLanguageTags(language.code)
    )
}
