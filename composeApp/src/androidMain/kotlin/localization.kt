
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

actual fun setLanguage(languageCode: String) {
    AppCompatDelegate.setApplicationLocales(
        LocaleListCompat.forLanguageTags(languageCode)
    )
}
