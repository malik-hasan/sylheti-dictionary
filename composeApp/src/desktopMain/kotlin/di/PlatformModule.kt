package di

import androidx.room.Room
import androidx.room.RoomDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import data.bookmarks.BookmarksDataSource
import data.bookmarks.BookmarksDatabase
import data.dictionary.DictionaryAsset
import data.recentsearches.RecentSearchesDataSource
import data.recentsearches.RecentSearchesDatabase
import data.settings.PreferencesDataSource
import oats.mobile.sylhetidictionary.DictionaryDatabase
import org.koin.dsl.bind
import org.koin.dsl.module
import java.io.File

actual val platformModule = module {

    single { PreferencesDataSource(initDataStore { it }) }

    single {
        JdbcSqliteDriver("jdbc:sqlite:$DictionaryAsset").also(DictionaryDatabase.Schema::create)
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
    Room.databaseBuilder<T>(File(System.getProperty("java.io.tmpdir"), filename).absolutePath).init()
