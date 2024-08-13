package utility

import platform.Foundation.NSUserDefaults

actual fun setLocale(languageCode: String) {
    NSUserDefaults.standardUserDefaults.setObject(arrayListOf(languageCode), "AppleLanguages")
}
