package di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import okio.Path.Companion.toPath
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration = {}) {
    startKoin {
        config()
        modules(platformModule, sharedModule)
    }
}

fun initDataStore(producePath: (fileName: String) -> String): DataStore<Preferences> {
    val dataStoreFileName = "preferences.preferences_pb"
    return PreferenceDataStoreFactory.createWithPath { producePath(dataStoreFileName).toPath() }
}

fun <T : RoomDatabase> RoomDatabase.Builder<T>.init() =
    setDriver(BundledSQLiteDriver()).build()
