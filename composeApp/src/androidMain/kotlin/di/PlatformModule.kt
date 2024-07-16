package di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import data.dictionary.DictionaryAsset
import oats.mobile.sylhetidictionary.DictionaryDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ui.screens.search.SearchViewModel

actual val platformModule = module {
    viewModelOf(::SearchViewModel)

    single { createDataStore(androidContext()) }

    single {
        AndroidSqliteDriver(DictionaryDatabase.Schema, get(), DictionaryAsset)
    }.bind(SqlDriver::class)
}

private fun createDataStore(context: Context) = initDataStore { fileName ->
    context.filesDir.resolve(fileName).absolutePath
}
