package oats.mobile.sylhetidictionary.utility

import java.io.File

val dictionaryDatabasePath: String by lazy {
    File(System.getProperty("user.home"), ".sylhetidictionary")
        .apply { mkdirs() }
        .resolve(DictionaryAsset)
        .absolutePath
}
