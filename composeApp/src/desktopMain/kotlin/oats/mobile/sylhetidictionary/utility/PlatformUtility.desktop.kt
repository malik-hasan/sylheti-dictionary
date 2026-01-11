package oats.mobile.sylhetidictionary.utility

import java.io.File

val databaseDirectory by lazy {
    val os = System.getProperty("os.name").lowercase()
    val userHome = System.getProperty("user.home")
    when {
        os.contains("mac") -> File(userHome, "Library/Application Support/SylhetiDictionary")
        os.contains("win") -> File(
            System.getenv("LOCALAPPDATA") ?: "$userHome\\AppData\\Local",
            "SylhetiDictionary"
        )

        else -> File(
            System.getenv("XDG_DATA_HOME") ?: "$userHome/.local/share",
            "sylhetidictionary"
        )
    }.apply { mkdirs() }
}

val dictionaryDatabasePath: String by lazy {
    databaseDirectory.resolve(DictionaryAsset).absolutePath
}
