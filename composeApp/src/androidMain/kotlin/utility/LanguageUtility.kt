package utility

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import models.Language

actual fun setAppOSLanguage(language: Language) {
    AppCompatDelegate.setApplicationLocales(
        LocaleListCompat.forLanguageTags(language.code)
    )
}
