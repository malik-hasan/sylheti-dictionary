package oats.mobile.sylhetidictionary.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
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

fun initDataStore(getPath: (fileName: String) -> String) = PreferenceDataStoreFactory.createWithPath {
    getPath("preferences.preferences_pb").toPath()
}

fun <T : RoomDatabase> RoomDatabase.Builder<T>.init() =
    setDriver(BundledSQLiteDriver()).build()
