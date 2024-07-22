package di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import data.dictionary.DictionaryAsset
import data.favorites.FavoritesDatabase
import data.favorites.FavoritesRepository
import oats.mobile.sylhetidictionary.DictionaryDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ui.screens.search.SearchViewModel

actual val platformModule = module {
    viewModelOf(::SearchViewModel)

    single { createDataStore(get()) }

    single {
        AndroidSqliteDriver(DictionaryDatabase.Schema, get(), DictionaryAsset)
    }.bind(SqlDriver::class)

    single {
        FavoritesRepository(get(),
            Room.databaseBuilder<FavoritesDatabase>(
                get(),
                androidContext().getDatabasePath("favorites.db").absolutePath
            )
                .setDriver(BundledSQLiteDriver())
                .build()
                .favoritesDao()
        )
    }
}

private fun createDataStore(context: Context) = initDataStore { fileName ->
    context.filesDir.resolve(fileName).absolutePath
}
