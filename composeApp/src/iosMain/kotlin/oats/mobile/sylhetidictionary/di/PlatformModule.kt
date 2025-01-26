package oats.mobile.sylhetidictionary.di

import androidx.room.Room
import androidx.room.RoomDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import oats.mobile.sylhetidictionary.data.bookmarks.BookmarksDataSource
import oats.mobile.sylhetidictionary.data.bookmarks.BookmarksDatabase
import oats.mobile.sylhetidictionary.data.dictionary.DictionaryAsset
import oats.mobile.sylhetidictionary.data.recentsearches.RecentSearchesDatabase
import oats.mobile.sylhetidictionary.data.settings.PreferencesDataSource
import oats.mobile.sylhetidictionary.DictionaryDatabase
import org.koin.dsl.bind
import org.koin.dsl.module
import oats.mobile.sylhetidictionary.path
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
        oats.mobile.sylhetidictionary.data.recentsearches.RecentSearchesDataSource(
            roomDatabase<RecentSearchesDatabase>(RecentSearchesDatabase.FILENAME).dao()
        )
    }
}

inline fun <reified T : RoomDatabase> roomDatabase(filename: String) =
    Room.databaseBuilder<T>(NSDocumentDirectory.path.stringByAppendingPathComponent(filename)).init()
