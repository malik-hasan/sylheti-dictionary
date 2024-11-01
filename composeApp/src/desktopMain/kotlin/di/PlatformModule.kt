package di

import androidx.room.Room
import androidx.room.RoomDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import data.bookmarks.BookmarksDatabase
import data.bookmarks.BookmarksRepository
import data.dictionary.DictionaryAsset
import data.recentsearches.RecentSearchesDatabase
import data.recentsearches.RecentSearchesRepository
import data.settings.PreferencesRepository
import oats.mobile.sylhetidictionary.DictionaryDatabase
import org.koin.dsl.bind
import org.koin.dsl.module
import java.io.File

actual val platformModule = module {

    single { PreferencesRepository(initDataStore { it }) }

    single {
        JdbcSqliteDriver("jdbc:sqlite:$DictionaryAsset").also(DictionaryDatabase.Schema::create)
    }.bind(SqlDriver::class)

    single {
        BookmarksRepository(
            roomDatabase<BookmarksDatabase>(BookmarksDatabase.FILENAME).dao(),
            get()
        )
    }

    single {
        RecentSearchesRepository(
            roomDatabase<RecentSearchesDatabase>(RecentSearchesDatabase.FILENAME).dao()
        )
    }
}

inline fun <reified T : RoomDatabase> roomDatabase(filename: String) =
    Room.databaseBuilder<T>(File(System.getProperty("java.io.tmpdir"), filename).absolutePath).init()
