package di

import androidx.room.Room
import androidx.room.RoomDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import data.dictionary.DictionaryAsset
import data.bookmarks.BookmarksDatabase
import data.bookmarks.BookmarksRepository
import data.recentsearches.RecentSearchesDatabase
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
        BookmarksRepository(get(),
            roomDatabase<BookmarksDatabase>(BookmarksDatabase.FILENAME).dao()
        )
    }

    single { roomDatabase<RecentSearchesDatabase>(RecentSearchesDatabase.FILENAME).dao() }
}

inline fun <reified T : RoomDatabase> roomDatabase(filename: String) =
    Room.databaseBuilder<T>(File(System.getProperty("java.io.tmpdir"), filename).absolutePath).init()
