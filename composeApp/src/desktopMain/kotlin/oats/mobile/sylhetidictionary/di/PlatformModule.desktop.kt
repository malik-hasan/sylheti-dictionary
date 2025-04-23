package oats.mobile.sylhetidictionary.di

import androidx.room.Room
import androidx.room.RoomDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import oats.mobile.sylhetidictionary.DictionaryDatabase
import oats.mobile.sylhetidictionary.data.bookmarks.BookmarksRepository
import oats.mobile.sylhetidictionary.data.bookmarks.BookmarksDatabase
import oats.mobile.sylhetidictionary.data.dictionary.DictionaryAsset
import oats.mobile.sylhetidictionary.data.recentsearches.RecentSearchesRepository
import oats.mobile.sylhetidictionary.data.recentsearches.RecentSearchesDatabase
import oats.mobile.sylhetidictionary.data.preferences.PreferencesRepository
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
    Room.databaseBuilder<T>(File(System.getProperty("java.io.tmpdir"), filename).absolutePath).init()
