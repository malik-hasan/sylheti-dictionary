package di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import oats.mobile.sylhetidictionary.DictionaryDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import ui.screens.search.SearchViewModel

actual val platformModule = module {
    singleOf(::SearchViewModel)

    single { createDataStore() }

    single {
//        val context = androidContext()
        val fileName = "sylhetiLexicon.db"
//        val database: File = context.getDatabasePath(fileName)
//
//        if (!database.exists()) {
//            val inputStream = context.assets.open(fileName)
//            Logger.d("MALIK: absolute: ${database.absolutePath}")
//            val outputStream = FileOutputStream(database.absolutePath)
//
//            inputStream.use { input ->
//                outputStream.use {
//                    input.copyTo(it)
//                }
//            }
//        }

        NativeSqliteDriver(DictionaryDatabase.Schema, fileName)
    }.bind(SqlDriver::class)
}

@OptIn(ExperimentalForeignApi::class)
private fun createDataStore() = initDataStore { fileName ->
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
    requireNotNull(documentDirectory).path + "/$fileName"
}
