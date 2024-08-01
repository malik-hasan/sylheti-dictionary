package di

import androidx.room.Room
import androidx.room.RoomDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import data.dictionary.DictionaryAsset
import data.favorites.FavoritesDatabase
import data.favorites.FavoritesRepository
import data.favorites.instantiateImpl
import data.recentsearches.RecentSearchesDatabase
import data.recentsearches.instantiateImpl
import data.settings.PreferencesRepository
import oats.mobile.sylhetidictionary.DictionaryDatabase
import org.koin.dsl.bind
import org.koin.dsl.module
import path
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSHomeDirectory
import platform.Foundation.stringByAppendingPathComponent

actual val platformModule = module {

    single {
        PreferencesRepository(
            initDataStore { fileName ->
                NSDocumentDirectory.path.stringByAppendingPathComponent(fileName)
            }
        )
    }

    single {
        NativeSqliteDriver(DictionaryDatabase.Schema, DictionaryAsset)
    }.bind(SqlDriver::class)

    single {
        FavoritesRepository(get(),
            roomDatabase<FavoritesDatabase>(FavoritesDatabase.FILENAME, FavoritesDatabase::class::instantiateImpl).dao()
        )
    }

    single {
        roomDatabase<RecentSearchesDatabase>(RecentSearchesDatabase.FILENAME, RecentSearchesDatabase::class::instantiateImpl).dao()
    }
}

inline fun <reified T : RoomDatabase> roomDatabase(filename: String, noinline instantiateImpl: () -> T) =
    Room.databaseBuilder<T>("${NSHomeDirectory()}/$filename", instantiateImpl).init()
