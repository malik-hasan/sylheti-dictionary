package utility

import models.Language
import java.util.Locale

actual fun setAppOSLanguage(language: Language) {
    Locale.setDefault(Locale(language.code))
}
