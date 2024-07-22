package di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import data.dictionary.DictionaryAsset
import data.favorites.FavoritesDatabase
import data.favorites.FavoritesRepository
import data.favorites.instantiateImpl
import oats.mobile.sylhetidictionary.DictionaryDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import path
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSHomeDirectory
import platform.Foundation.stringByAppendingPathComponent
import ui.screens.search.SearchViewModel

actual val platformModule = module {
    singleOf(::SearchViewModel)

    single { createDataStore() }

    single {
        NativeSqliteDriver(DictionaryDatabase.Schema, DictionaryAsset)
    }.bind(SqlDriver::class)

    single {
        FavoritesRepository(get(),
            Room.databaseBuilder<FavoritesDatabase>(
                "${NSHomeDirectory()}/favorites.db",
                FavoritesDatabase::class::instantiateImpl
            )
                .setDriver(BundledSQLiteDriver())
                .build()
                .favoritesDao()
        )
    }
}

private fun createDataStore() = initDataStore { fileName ->
    NSDocumentDirectory.path.stringByAppendingPathComponent(fileName)
}
