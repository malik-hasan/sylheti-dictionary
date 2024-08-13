package utility

import java.util.Locale

actual fun setLocale(languageCode: String) {
    Locale.setDefault(Locale(languageCode))
}
