package oats.mobile.sylhetidictionary.di

import androidx.room.Room
import androidx.room.RoomDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import oats.mobile.sylhetidictionary.DictionaryDatabase
import oats.mobile.sylhetidictionary.data.bookmarks.BookmarksDatabase
import oats.mobile.sylhetidictionary.data.bookmarks.BookmarksRepository
import oats.mobile.sylhetidictionary.data.preferences.PreferencesRepository
import oats.mobile.sylhetidictionary.data.recentsearches.RecentSearchesDatabase
import oats.mobile.sylhetidictionary.data.recentsearches.RecentSearchesRepository
import oats.mobile.sylhetidictionary.di.utils.init
import oats.mobile.sylhetidictionary.di.utils.initDataStore
import oats.mobile.sylhetidictionary.utility.DictionaryAsset
import oats.mobile.sylhetidictionary.utility.applicationSupportDirectory
import org.koin.dsl.bind
import org.koin.dsl.module
import platform.Foundation.stringByAppendingPathComponent

actual val platformModule = module {

    single {
        PreferencesRepository(
            initDataStore { fileName ->
                applicationSupportDirectory.stringByAppendingPathComponent(fileName)
            }
        )
    }

    single {
        NativeSqliteDriver(DictionaryDatabase.Schema, DictionaryAsset)
    }.bind(SqlDriver::class)

    single {
        BookmarksRepository(
            roomDatabase<BookmarksDatabase>(BookmarksDatabase.FILENAME).dao()
        )
    }

    single {
        RecentSearchesRepository(
            roomDatabase<RecentSearchesDatabase>(RecentSearchesDatabase.FILENAME).dao()
        )
    }
}

inline fun <reified T : RoomDatabase> roomDatabase(filename: String) =
    Room.databaseBuilder<T>(applicationSupportDirectory.stringByAppendingPathComponent(filename)).init()
