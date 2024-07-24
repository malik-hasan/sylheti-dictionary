
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import co.touchlab.kermit.Logger

actual fun setLanguage(languageCode: String) {
    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
    Logger.d("Before: ${AppCompatDelegate.getApplicationLocales()}")
    AppCompatDelegate.setApplicationLocales(appLocale)
    Logger.d("After: ${AppCompatDelegate.getApplicationLocales()}")

}
