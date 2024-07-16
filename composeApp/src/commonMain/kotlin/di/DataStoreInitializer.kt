package di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

fun initDataStore(producePath: (fileName: String) -> String): DataStore<Preferences> {
    val dataStoreFileName = "preferences.preferences_pb"
    return PreferenceDataStoreFactory.createWithPath { producePath(dataStoreFileName).toPath() }
}
