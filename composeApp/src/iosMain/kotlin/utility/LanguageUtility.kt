package utility

import models.Language
import platform.Foundation.NSUserDefaults

actual fun setAppOSLanguage(language: Language) {
    NSUserDefaults.standardUserDefaults.setObject(arrayListOf(language.code), "AppleLanguages")
}
