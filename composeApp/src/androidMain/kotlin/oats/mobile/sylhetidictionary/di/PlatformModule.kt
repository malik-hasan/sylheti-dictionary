package oats.mobile.sylhetidictionary.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import oats.mobile.sylhetidictionary.DictionaryDatabase
import oats.mobile.sylhetidictionary.data.bookmarks.BookmarksRepository
import oats.mobile.sylhetidictionary.data.bookmarks.BookmarksDatabase
import oats.mobile.sylhetidictionary.data.dictionary.DictionaryAsset
import oats.mobile.sylhetidictionary.data.recentsearches.RecentSearchesDatabase
import oats.mobile.sylhetidictionary.data.settings.PreferencesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {

    single {
        PreferencesRepository(
            initDataStore { fileName ->
                androidContext().filesDir.resolve(fileName).absolutePath
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
        oats.mobile.sylhetidictionary.data.recentsearches.RecentSearchesRepository(
            roomDatabase<RecentSearchesDatabase>(get(), RecentSearchesDatabase.FILENAME).dao()
        )
    }
}

inline fun <reified T : RoomDatabase> roomDatabase(context: Context, filename: String) =
    Room.databaseBuilder<T>(context, context.getDatabasePath(filename).absolutePath).init()
