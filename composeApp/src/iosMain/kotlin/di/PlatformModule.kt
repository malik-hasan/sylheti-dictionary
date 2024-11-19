package di

import androidx.room.Room
import androidx.room.RoomDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import data.bookmarks.BookmarksDataSource
import data.bookmarks.BookmarksDatabase
import data.dictionary.DictionaryAsset
import data.recentsearches.RecentSearchesDataSource
import data.recentsearches.RecentSearchesDatabase
import data.settings.PreferencesDataSource
import oats.mobile.sylhetidictionary.DictionaryDatabase
import org.koin.dsl.bind
import org.koin.dsl.module
import path
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.stringByAppendingPathComponent

actual val platformModule = module {

    single {
        PreferencesDataSource(
            initDataStore { fileName ->
                NSDocumentDirectory.path.stringByAppendingPathComponent(fileName)
            }
        )
    }

    single {
        NativeSqliteDriver(DictionaryDatabase.Schema, DictionaryAsset)
    }.bind(SqlDriver::class)

    single {
        BookmarksDataSource(
            roomDatabase<BookmarksDatabase>(BookmarksDatabase.FILENAME).dao()
        )
    }

    single {
        RecentSearchesDataSource(
            roomDatabase<RecentSearchesDatabase>(RecentSearchesDatabase.FILENAME).dao()
        )
    }
}

inline fun <reified T : RoomDatabase> roomDatabase(filename: String) =
    Room.databaseBuilder<T>(NSDocumentDirectory.path.stringByAppendingPathComponent(filename)).init()
