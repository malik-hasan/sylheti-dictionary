package di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import data.dictionary.DictionaryAsset
import documentsDirectoryPath
import oats.mobile.sylhetidictionary.DictionaryDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import platform.Foundation.stringByAppendingPathComponent
import ui.screens.search.SearchViewModel

actual val platformModule = module {
    singleOf(::SearchViewModel)

    single { createDataStore() }

    single {
        NativeSqliteDriver(DictionaryDatabase.Schema, DictionaryAsset)
    }.bind(SqlDriver::class)
}

private fun createDataStore() = initDataStore { fileName ->
    documentsDirectoryPath.stringByAppendingPathComponent(fileName)
}
