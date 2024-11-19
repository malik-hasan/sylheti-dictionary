package di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import data.bookmarks.BookmarksDataSource
import data.bookmarks.BookmarksDatabase
import data.dictionary.DictionaryAsset
import data.recentsearches.RecentSearchesDataSource
import data.recentsearches.RecentSearchesDatabase
import data.settings.PreferencesDataSource
import oats.mobile.sylhetidictionary.DictionaryDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {

    single {
        PreferencesDataSource(
            initDataStore { fileName ->
                androidContext().filesDir.resolve(fileName).absolutePath
            }
        )
    }

    single {
        AndroidSqliteDriver(DictionaryDatabase.Schema, get(), DictionaryAsset)
    }.bind(SqlDriver::class)

    single {
        BookmarksDataSource(
            roomDatabase<BookmarksDatabase>(get(), BookmarksDatabase.FILENAME).dao()
        )
    }

    single {
        RecentSearchesDataSource(
            roomDatabase<RecentSearchesDatabase>(get(), RecentSearchesDatabase.FILENAME).dao()
        )
    }
}

inline fun <reified T : RoomDatabase> roomDatabase(context: Context, filename: String) =
    Room.databaseBuilder<T>(context, context.getDatabasePath(filename).absolutePath).init()
