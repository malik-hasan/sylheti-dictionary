package di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ui.screens.search.SearchViewModel

actual val platformModule = module {
    singleOf(::SearchViewModel)

    single { createDataStore { dataStoreFileName } }
}
