package oats.mobile.sylhetidictionary.di.utils

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import co.touchlab.kermit.koin.kermitLoggerModule
import oats.mobile.sylhetidictionary.di.platformModule
import oats.mobile.sylhetidictionary.di.sharedModule
import okio.Path.Companion.toPath
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration = {}) {
    startKoin {
        config()
        logger(KermitKoinLogger(Logger.withTag("Koin")))
        modules(
            kermitLoggerModule(Logger.withTag("Sylheti Dictionary")),
            platformModule,
            sharedModule
        )
    }
}

fun initDataStore(getPath: (fileName: String) -> String) = PreferenceDataStoreFactory.createWithPath {
    getPath("sylheti_dictionary.preferences_pb").toPath()
}

fun <T : RoomDatabase> RoomDatabase.Builder<T>.init() =
    setDriver(BundledSQLiteDriver()).build()
