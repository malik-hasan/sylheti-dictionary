package oats.mobile.sylhetidictionary.di

import androidx.room.Room
import androidx.room.RoomDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import oats.mobile.sylhetidictionary.DictionaryDatabase
import oats.mobile.sylhetidictionary.data.bookmarks.BookmarksDatabase
import oats.mobile.sylhetidictionary.data.bookmarks.BookmarksRepository
import oats.mobile.sylhetidictionary.data.preferences.PreferencesRepository
import oats.mobile.sylhetidictionary.data.recentsearches.RecentSearchesDatabase
import oats.mobile.sylhetidictionary.data.recentsearches.RecentSearchesRepository
import oats.mobile.sylhetidictionary.di.utils.init
import oats.mobile.sylhetidictionary.di.utils.initDataStore
import oats.mobile.sylhetidictionary.utility.databaseDirectory
import oats.mobile.sylhetidictionary.utility.dictionaryDatabasePath
import org.koin.dsl.bind
import org.koin.dsl.module
import java.io.File

actual val platformModule = module {

    single {
        PreferencesRepository(
            initDataStore { fileName ->
                databaseDirectory.resolve(fileName).absolutePath
            }
        )
    }

    single {
        JdbcSqliteDriver("jdbc:sqlite:$dictionaryDatabasePath")
            .also(DictionaryDatabase.Schema::create)
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
    Room.databaseBuilder<T>(File(databaseDirectory, filename).absolutePath).init()
