import platform.Foundation.NSUserDefaults

actual fun setLanguage(languageCode: String) {
    NSUserDefaults.standardUserDefaults.setObject(arrayListOf(languageCode), "AppleLanguages")
}
