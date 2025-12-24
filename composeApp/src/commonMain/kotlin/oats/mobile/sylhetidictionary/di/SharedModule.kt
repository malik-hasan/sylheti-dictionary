package oats.mobile.sylhetidictionary.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import oats.mobile.sylhetidictionary.DictionaryDatabase
import oats.mobile.sylhetidictionary.data.dictionary.DictionaryRepository
import oats.mobile.sylhetidictionary.ui.app.AppViewModel
import oats.mobile.sylhetidictionary.ui.screens.debug.DebugViewModel
import oats.mobile.sylhetidictionary.ui.screens.search.entry.EntryViewModel
import oats.mobile.sylhetidictionary.ui.screens.search.search.SearchViewModel
import oats.mobile.sylhetidictionary.ui.screens.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    viewModelOf(::AppViewModel)
    viewModel { (processTextSearchTerm: String?) ->
        SearchViewModel(processTextSearchTerm, get(), get(), get(), get())
    }
    viewModelOf(::SettingsViewModel)
    viewModelOf(::EntryViewModel)
    viewModelOf(::DebugViewModel)

    single { DictionaryRepository(DictionaryDatabase(get()).dictionaryDatabaseQueries, get()) }

    single { CoroutineScope(Dispatchers.Default + SupervisorJob()) }
}
