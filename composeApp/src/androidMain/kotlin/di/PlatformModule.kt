package di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import co.touchlab.kermit.Logger
import oats.mobile.sylhetidictionary.DictionaryDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ui.screens.search.SearchViewModel
import java.io.File
import java.io.FileOutputStream

actual val platformModule = module {
    viewModelOf(::SearchViewModel)

    single { createDataStore(androidContext()) }

    single {
        val context = androidContext()
        val fileName = "sylhetiLexicon2.db"
        val database: File = context.getDatabasePath(fileName)

        if (!database.exists()) {
            val inputStream = context.assets.open(fileName)
            Logger.d("MALIK: absolute: ${database.absolutePath}")
            val outputStream = FileOutputStream(database.absolutePath)

            inputStream.use { input ->
                outputStream.use {
                    input.copyTo(it)
                }
            }
        }

        AndroidSqliteDriver(DictionaryDatabase.Schema, context, fileName)
    }.bind(SqlDriver::class)
}

private fun createDataStore(context: Context) = createDataStore {
    context.filesDir.resolve(dataStoreFileName).absolutePath
}
