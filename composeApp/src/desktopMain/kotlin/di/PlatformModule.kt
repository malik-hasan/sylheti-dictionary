package di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import oats.mobile.sylhetidictionary.DictionaryDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ui.screens.search.SearchViewModel

actual val platformModule = module {
    singleOf(::SearchViewModel)

    single { createDataStore { dataStoreFileName } }

    single {
        JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also {
            DictionaryDatabase.Schema.create(it)
        }
    }.bind(SqlDriver::class)
}
