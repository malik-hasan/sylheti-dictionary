package oats.mobile.sylhetidictionary.utility

import java.io.File

val databaseDirectory by lazy {
    val os = System.getProperty("os.name").lowercase()
    val userHome = System.getProperty("user.home")
    when {
        os.contains("mac") -> File(userHome, "Library/Application Support/SylhetiDictionary")
        os.contains("win") -> {
            val appData = System.getenv("LOCALAPPDATA") ?: "$userHome\\AppData\\Local"
            File(appData, "SylhetiDictionary")
        }

        else -> {
            val xdgDataHome = System.getenv("XDG_DATA_HOME") ?: "$userHome/.local/share"
            File(xdgDataHome, "sylhetidictionary")
        }
    }.apply { mkdirs() }
}

val dictionaryDatabasePath: String by lazy {
    databaseDirectory.resolve(DictionaryAsset).absolutePath
}
