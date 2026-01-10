package oats.mobile.sylhetidictionary.di

import android.content.Context
import androidx.datastore.dataStoreFile
import androidx.room.Room
import androidx.room.RoomDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import oats.mobile.sylhetidictionary.DictionaryDatabase
import oats.mobile.sylhetidictionary.data.bookmarks.BookmarksDatabase
import oats.mobile.sylhetidictionary.data.bookmarks.BookmarksRepository
import oats.mobile.sylhetidictionary.data.preferences.PreferencesRepository
import oats.mobile.sylhetidictionary.data.recentsearches.RecentSearchesDatabase
import oats.mobile.sylhetidictionary.data.recentsearches.RecentSearchesRepository
import oats.mobile.sylhetidictionary.di.utils.init
import oats.mobile.sylhetidictionary.di.utils.initDataStore
import oats.mobile.sylhetidictionary.utility.DictionaryAsset
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {

    single {
        PreferencesRepository(
            initDataStore { fileName ->
                androidContext().dataStoreFile(fileName).absolutePath
            }
        )
    }

    single {
        AndroidSqliteDriver(DictionaryDatabase.Schema, get(), DictionaryAsset)
    }.bind(SqlDriver::class)

    single {
        BookmarksRepository(
            roomDatabase<BookmarksDatabase>(get(), BookmarksDatabase.FILENAME).dao()
        )
    }

    single {
        RecentSearchesRepository(
            roomDatabase<RecentSearchesDatabase>(get(), RecentSearchesDatabase.FILENAME).dao()
        )
    }
}

inline fun <reified T : RoomDatabase> roomDatabase(context: Context, filename: String) =
    Room.databaseBuilder<T>(context, context.getDatabasePath(filename).absolutePath).init()
