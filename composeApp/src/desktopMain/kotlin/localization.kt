import java.util.Locale

actual fun setLanguage(languageCode: String) {
    Locale.setDefault(Locale(languageCode))
}
