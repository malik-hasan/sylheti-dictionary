package di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ui.screens.search.SearchViewModel

actual val platformModule = module {
    viewModelOf(::SearchViewModel)

    single { createDataStore(androidContext()) }
}

private fun createDataStore(context: Context) = createDataStore {
    context.filesDir.resolve(dataStoreFileName).absolutePath
}
